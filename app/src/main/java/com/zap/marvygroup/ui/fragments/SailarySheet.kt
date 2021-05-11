package com.zap.marvygroup.ui.fragments

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.DownloadListener
import com.androidnetworking.interfaces.DownloadProgressListener
import com.krishna.fileloader.FileLoader
import com.krishna.fileloader.listener.FileRequestListener
import com.krishna.fileloader.pojo.FileResponse
import com.krishna.fileloader.request.FileLoadRequest
import com.zap.marvygroup.R
import kotlinx.android.synthetic.main.pdfviewrenderer.*
import java.io.*
import java.lang.Exception
import java.net.URL
import java.net.URLConnection

class SailarySheet : Fragment() {
    lateinit var seekBar: SeekBar
    val myPdf = "my_pdf.pdf"

    lateinit var progress_bar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.pdfviewrenderer, container, false)
         progress_bar = view.findViewById(R.id.progress_bar)
        seekBar = view.findViewById(R.id.seekbar)
        val url = arguments?.getString("urlval")
        initSeekbar()
        val file: File = activity!!.getFileStreamPath(myPdf)
        val path = file.absolutePath
         val finalPath = activity!!.getFilesDir().toString() + File.separator + path
        downloadPdf(url)
//        if (url != null) {
//            GetWeatherAsyncTask(url).execute(url)
//        }
        downloadFile(url,finalPath)
        return view
    }

    private fun downloadFile(url: String?, path: String) {
        AndroidNetworking.download(url,path,myPdf)
                 .setTag("downloadTest")
                 .setPriority(Priority.MEDIUM)
                 .build()
                 .setDownloadProgressListener(object: DownloadProgressListener {
                     override fun onProgress(bytesDownloaded: Long, totalBytes: Long) {

                     }

                 }).startDownload(object: DownloadListener {
                override fun onDownloadComplete() {

                }

                override fun onError(anError: ANError?) {
                 Log.e("error",anError?.message)
                }

            })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



       // progress_bar.visibility = View.VISIBLE



    }



    fun downloadPdf(url: String?){
        try {
            object:AsyncTask<Void,Int,Boolean>(){
                override fun doInBackground(vararg params: Void?): Boolean? {
                    try {
                       val file: File = activity!!.getFileStreamPath(url)
                        if (file.exists()){
                            return true
                        }
                        try {
                            val fileOutputStream: FileOutputStream = activity!!.openFileOutput(url,Context.MODE_PRIVATE)
                            val u: URL = URL(myPdf)
                            val connection:URLConnection = u.openConnection()
                            val contentLength = connection.contentLength
                            val input: BufferedInputStream = BufferedInputStream(u.openStream())
                             val data:ByteArray = byteArrayOf(contentLength.toByte())
                             var total:Int = 0
                              var count: Int = input.read(data)
                            while (count != -1){
                              total = total + count
                                publishProgress((total * 100) / contentLength)
                                fileOutputStream.write(data,0,count)
                            }
                            fileOutputStream.flush()
                            fileOutputStream.close()
                            input.close()
                            return true
                        }catch (e:IOException){

                        }
                    }catch (e:Exception){

                    }
                    return false
                }

                override fun onProgressUpdate(vararg values: Int?) {
                    super.onProgressUpdate(*values)
                    seekBar.setProgress(values[0]!!)
                }

                override fun onPostExecute(result: Boolean?) {
                    super.onPostExecute(result)
                    if(result!!) {
                       openPdf(url)
                    }
                }


                private fun openPdf(url: String?) {
                    FileLoader.with(activity).load(url)
                        .fromDirectory("PDFFile",FileLoader.DIR_EXTERNAL_PUBLIC)
                        .asFile(object: FileRequestListener<File> {
                            override fun onLoad(p0: FileLoadRequest?, p1: FileResponse<File>?) {
                                progress_bar.visibility = View.GONE
                                val pdfFile = p1?.body
                                pdfView.fromFile(pdfFile)
                                    .password(null)
                                    .defaultPage(0)
                                    .enableSwipe(true)
                                    .swipeHorizontal(false)
                                    .onDraw{canvas,pageWidth,pageHeight,displayPage ->

                                    }.onDrawAll{canvas,pageWidth,pageHeight,displayedPage ->

                                    }.onPageChange{page, pageCount ->

                                    }.onPageError { page, t ->
                                        Toast.makeText(activity,"error while opening page"+page,Toast.LENGTH_SHORT).show()

                                    }.onTap { false }
                                    .onRender { nbPages, pageWidth, pageHeight ->
                                        pdfView.fitToWidth()
                                    }.enableAnnotationRendering(true)
                                    .invalidPageColor(Color.RED)
                                    .load()
                            }

                            override fun onError(request: FileLoadRequest?, t: Throwable?) {
                                Toast.makeText(activity,""+t?.message,Toast.LENGTH_SHORT).show()
                                progress_bar.visibility = View.GONE

                            }

                        })
                }
            }.execute()
        } catch (e: Exception) {
        }
    }

    private fun initSeekbar() {
        seekBar.progressDrawable.setColorFilter(Color.RED,PorterDuff.Mode.SRC_IN)
        seekBar.thumb.setColorFilter(Color.RED,PorterDuff.Mode.SRC_IN)
        seekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
              val seekbarProgress = progress * (seekBar!!.width - 3 * seekBar.thumbOffset) / seekBar.max
                txtView.setText(""+seekbarProgress)
                txtView.x = seekBar.x + seekbarProgress + seekBar.thumbOffset / 2
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }
}

//  class GetWeatherAsyncTask(val url: String) : AsyncTask<String,Int,Boolean>(){
//       lateinit var context: Context
//       val myPdf = "example.pdf"
//       override fun doInBackground(vararg params: String?): Boolean {
//        try {
//            val file: File = context!!.getFileStreamPath(url)
//            if (file.exists()){
//                return true
//            }
//            try {
//                val fileOutputStream: FileOutputStream = context!!.openFileOutput(url,Context.MODE_PRIVATE)
//                val u: URL = URL(myPdf)
//                val connection:URLConnection = u.openConnection()
//                val contentLength = connection.contentLength
//                val input: BufferedInputStream = BufferedInputStream(u.openStream())
//                val data:ByteArray = byteArrayOf(contentLength.toByte())
//                var total:Int = 0
//                var count: Int = input.read(data)
//                while (count != -1){
//                    total = total + count
//                    publishProgress((total * 100) / contentLength)
//                    fileOutputStream.write(data,0,count)
//                }
//                fileOutputStream.flush()
//                fileOutputStream.close()
//                input.close()
//                return true
//            }catch (e:IOException){
//
//            }
//        }catch (e:Exception){
//
//        }
//        return false
//    }
//       override fun onProgressUpdate(vararg values: Int?) {
//           super.onProgressUpdate(*values)
//           //seekBar.setProgress(values[0]!!)
//       }
//
//       override fun onPostExecute(result: Boolean?) {
//           super.onPostExecute(result)
//           if(result!!) {
//               openPdf(url)
//               Log.e("result",url)
//           }
//       }
//
//       private fun openPdf(url: String) {
//
//       }
//
//
//   }


