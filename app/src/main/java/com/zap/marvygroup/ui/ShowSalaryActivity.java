package com.zap.marvygroup.ui;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.github.barteksc.pdfviewer.PDFView;
import com.zap.marvygroup.R;
import com.zap.marvygroup.util.ApiException;

import java.io.File;

public class ShowSalaryActivity extends AppCompatActivity {
   private String PDF_LINK ;
   private String MY_PDF = "my_pdf.pdf";
   private SeekBar seekbar;
   private TextView txtView;
   private PDFView pdfView;
    private long downloadID;

    private BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Fetching the download id received with the broadcast
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                openDownloadedAttachment(context, downloadId);
            }
            //Checking if the received broadcast is for our enqueued download by matching download id
            if (downloadID == id) {
              //  openFile(intent.getData().toString());
                Toast.makeText(ShowSalaryActivity.this, "Opening ...", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Used to open the downloaded attachment.
     *
     * @param context    Content.
     * @param downloadId Id of the downloaded file to open.
     */
    private void openDownloadedAttachment(final Context context, final long downloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            String downloadLocalUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
            String downloadMimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            if ((downloadStatus == DownloadManager.STATUS_SUCCESSFUL) && downloadLocalUri != null) {
                openDownloadedAttachment(context, Uri.parse(downloadLocalUri), downloadMimeType);
            }
        }
        cursor.close();
    }
    /**
     * Used to open the downloaded attachment.
     * <p/>
     * 1. Fire intent to open download file using external application.
     *
     * 2. Note:
     * 2.a. We can't share fileUri directly to other application (because we will get FileUriExposedException from Android7.0).
     * 2.b. Hence we can only share content uri with other application.
     * 2.c. We must have declared FileProvider in manifest.
     * 2.c. Refer - https://developer.android.com/reference/android/support/v4/content/FileProvider.html
     *
     * @param context            Context.
     * @param attachmentUri      Uri of the downloaded attachment to be opened.
     * @param attachmentMimeType MimeType of the downloaded attachment.
     */
    private void openDownloadedAttachment(final Context context, Uri attachmentUri, final String attachmentMimeType) {
        if(attachmentUri!=null) {
            // Get Content Uri.
            if (ContentResolver.SCHEME_FILE.equals(attachmentUri.getScheme())) {
                // FileUri - Convert it to contentUri.
                File file = new File(attachmentUri.getPath());


                attachmentUri = FileProvider.getUriForFile(ShowSalaryActivity.this, "com.freshdesk.helpdesk.provider", file);
                //openPdf(attachmentUri.getPath());
            }

            Intent openAttachmentIntent = new Intent(Intent.ACTION_VIEW);
            openAttachmentIntent.setDataAndType(attachmentUri, attachmentMimeType);
            openAttachmentIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try {
                context.startActivity(openAttachmentIntent);
                finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, context.getString(R.string.unable_to_open_file), Toast.LENGTH_LONG).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_salary);

        pdfView = findViewById(R.id.pdfView);
        registerReceiver(onDownloadComplete,new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        if (getIntent().getStringExtra("key_url")!= null){
            PDF_LINK = getIntent().getStringExtra("key_url");
            Log.e("key",PDF_LINK);
        }
        //initSeekBar();

       // downloadPdf(MY_PDF);

        beginDownload(PDF_LINK);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void beginDownload(String pdf_link) {
        File file=new File(getExternalFilesDir(null),"Salary Detail");
         if (file.toURI()!= null){
             try{
                 DownloadManager.Request request=new DownloadManager.Request(Uri.parse(pdf_link))
                         .setTitle("salary Detail")// Title of the Download Notification
                         .setDescription("opening ...")// Description of the Download Notification
                         .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)// Visibility of the download Notification
                         .setDestinationUri(Uri.fromFile(file))// Uri of the destination file
                         .setRequiresCharging(false)// Set if charging is required to begin the download
                         .setAllowedOverMetered(true)// Set if download is allowed on Mobile network
                         .setAllowedOverRoaming(true);// Set if download is allowed on roaming network
                 DownloadManager downloadManager= (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
                 downloadID = downloadManager.enqueue(request);// enqueue puts the download request in the queue.
             } catch (IllegalArgumentException e) {
                 e.getMessage();
                 Log.e("message",e.getMessage());
                 if (e.getMessage()=="Can only download HTTP/HTTPS URIs: salarynotfound"){
                     Toast.makeText(this,"Salary not found",Toast.LENGTH_SHORT).show();
                 }
             }
         }else{
             Toast.makeText(this,"Salary not found",Toast.LENGTH_SHORT).show();
         }
        /*
        Create a DownloadManager.Request with all the information necessary to start the download
         */

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(onDownloadComplete);
    }

    private void openPdf(String fileName) {
        try {
            File file = getFileStreamPath(fileName);
            Log.e("myfile" , file.getAbsolutePath());
            seekbar.setVisibility(View.GONE);
            pdfView.setVisibility(View.VISIBLE);
            pdfView.fromFile(file)
                    .enableSwipe(true)
                    .swipeHorizontal(false)
                    .onError(t -> {
                      Log.e("ABCfile", t.toString());
                    }).enableAntialiasing(true)
              .spacing(0).load();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}