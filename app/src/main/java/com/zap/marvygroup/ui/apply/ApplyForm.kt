package com.zap.marvygroup.ui.apply

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.afollestad.inlineactivityresult.startActivityForResult
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.zap.marvygroup.R
import com.zap.marvygroup.data.db.entities.GuestUser
import com.zap.marvygroup.databinding.ActivityApplyFormBinding
import com.zap.marvygroup.ui.FileUtils
import com.zap.marvygroup.util.hide
import com.zap.marvygroup.util.show
import com.zap.marvygroup.util.toast
import kotlinx.android.synthetic.main.activity_apply_form.*
import kotlinx.android.synthetic.main.activity_apply_form.toolbar
import kotlinx.android.synthetic.main.activity_documents.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File


class ApplyForm : AppCompatActivity(), ApplyFormListener, KodeinAware {

    override val kodein by kodein()
    private val factory: ApplyViewModelFactory by instance()
//    private var file:File?= null
//    private var name:String?=null
//    private var email:String?= null
//    private var subject:String?= null
//    private var mobile:String?= null
//    private var message

    fun onPdfClick(view: View?, task: ApplyViewModel) {
        println("MainActivity.onPdfClick")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("application/pdf")
        startActivityForResult(intent) { success, data ->

            if (success) {

                val selectedImage = data.data
                val pdfpath = FileUtils.getRealPathFromURI_API19(this, selectedImage)
                if (pdfpath != null) {
                    img_text.setText(pdfpath)
                    task.resume = File(pdfpath)
                    // Log.e("uri", "" + pdfpath.substring(pdfpath.lastIndexOf("/") + 1))
                }

            }

        }
    }

    fun onWordClick(view: View?, task: ApplyViewModel) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)

        intent.setType("application/msword")
        val mimetypes = arrayOf(
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "application/msword"
        )
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes)
        startActivityForResult(intent) { success, data ->
            // Do something
            if (success) {
                //  String PDFPATH = FilePath.getPath(MainActivity.this,uri);
                val selectedImage = data.data
                val pdfpath = FileUtils.getRealPathFromURI_API19(this, selectedImage)
                if (pdfpath != null) {
                    img_text.setText(pdfpath)
                    task.resume = File(pdfpath)
                    //  Log.e("uri", "" + pdfpath.substring(pdfpath.lastIndexOf("/") + 1))
                }

            }

        }
    }

    @NonNull
    private fun PrepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {

        val file = FileUtils.getRealPathFromURI_API19(this, fileUri);

        // create RequestBody instance from file
        val requestFile =
            RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file)

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file, requestFile);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityApplyFormBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_apply_form)
        val viewModel: ApplyViewModel =
            ViewModelProviders.of(this, factory).get(ApplyViewModel::class.java)
        binding.applyViewModel = viewModel
        viewModel.applyFormListener = this
        binding.activity = this

        toolbar.setTitle(" ")
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (savedInstanceState == null) {
            rootLayout.setVisibility(View.INVISIBLE)
            val viewTreeObserver: ViewTreeObserver = rootLayout.getViewTreeObserver()

            if (viewTreeObserver.isAlive) {
                viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onGlobalLayout() {
                        circularRevealActivity()
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                        } else {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this)
                        }
                    }
                })
            }
        }


//        button_register.setOnClickListener {
//
//            uploadFilesOnServer()
//
//        }
    }

//    private fun uploadFilesOnServer(){
//        doc_progress_bar.show()
//        AndroidNetworking.upload(" http://online.marvygroup.com/marvy_payroll/apiv2/document/add?")
//            .addMultipartFile("file",file)
//            .addMultipartParameter("name",name)
//            .addMultipartParameter("mobile",mobile)
//            .addMultipartParameter("email",email)
//            .addMultipartParameter("subject",subject)
//            .addMultipartParameter("message",message)
//            .setPriority(Priority.MEDIUM)
//            .build()
//            .setUploadProgressListener { bytesUploaded, totalBytes ->
//
//            }
//            .getAsJSONObject(object: JSONObjectRequestListener {
//                override fun onResponse(response: JSONObject?) {
//                    doc_progress_bar.hide()
//                    val jsonObject = JSONObject(response.toString())
//                    val isSuccessful = jsonObject.getString("isSuccessful")
//                    val message = jsonObject.getString("message")
//                    toast(message)
//                    Log.e("uploadServerResponse",response.toString())
//                }
//
//                override fun onError(anError: ANError?) {
//                    Log.e("uploadServerResponse",anError?.message)
//                }
//
//            })
//    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun circularRevealActivity() {
        val cx = rootLayout.width / 2
        val cy = rootLayout.height / 2

        val finalRadius = Math.max(rootLayout.width, rootLayout.height).toFloat()

        // create the animator for this view (the start radius is zero)
        val circularReveal =
            ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0f, finalRadius)

        circularReveal.duration = 1000
        // make the view visible and start the animation
        rootLayout.visibility = View.VISIBLE
        circularReveal.start()
    }

    override fun onStarted() {
        progress_circular.show()
    }

    override fun onSuccess(applyForm: String) {
//        if (!applyForm.message.isNullOrEmpty()) {
//
//        }
        toast(applyForm)
        progress_circular.hide()
       // onBackPressed()
    }

    override fun onFailure(message: String) {
        progress_circular.hide()
        et_message.error = message
        toast(message)
    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

}
