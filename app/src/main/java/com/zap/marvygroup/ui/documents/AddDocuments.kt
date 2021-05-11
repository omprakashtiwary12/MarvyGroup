package com.zap.marvygroup.ui.documents


import android.content.ContentUris
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.afollestad.inlineactivityresult.startActivityForResult
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.databinding.FragmentAddDocumentsBinding
import com.zap.marvygroup.ui.FileUtils
import com.zap.marvygroup.util.hide
import com.zap.marvygroup.util.show
import com.zap.marvygroup.util.toast
import kotlinx.android.synthetic.main.fragment_add_documents.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.File


/**
 * A simple [Fragment] subclass.
 */
class AddDocuments : Fragment() , KodeinAware,DocumentListener{
    override val kodein by kodein()
    private lateinit var viewModel: DocumentsViewModel
    private var prefs: PreferenceProvider? = null
    private val factory: DocumentsViewModelFactory by instance()
    private var type = null
    fun onPdfButtonClick(view: View,documentsViewModel: DocumentsViewModel){
        documentsViewModel.type = "pdf"
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("application/pdf")
        startActivityForResult(intent) {
                success,data ->
            if (success){
                val selectedImage = data.data
               //val pdfpath = getPDFPath(selectedImage)

                val pdfpath = FileUtils.getRealPathFromURI_API19(activity,selectedImage)
                if (pdfpath!= null){
                    btn_upload.visibility = View.VISIBLE
                    text_file.visibility = View.VISIBLE
                    diaryImage.visibility = View.GONE
                    val realPath = pdfpath?.substring(pdfpath.lastIndexOf("/")+1)
                    text_file.setText(realPath)
                    documentsViewModel.myFile = File(pdfpath)
                    Log.e("uri",""+ pdfpath?.substring(pdfpath.lastIndexOf("/")+1))
                }else{
                    activity?.toast("No actual path is found")
                }

            }
        }
    }
    fun onImageButtonClick(view: View, documentsViewModel: DocumentsViewModel){
        documentsViewModel.type = "image"
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent){
                success, data ->
            if (success){
                val selectedImageResource = data.data
                val imagePath = FileUtils.getRealPathFromURI_API19(activity,selectedImageResource)
              //  Log.e("imagePath",imagePath)
                documentsViewModel.myFile = File(imagePath)
                val bitmap =  MediaStore.Images.Media.getBitmap(activity?.contentResolver,selectedImageResource)
                if (bitmap!= null){
                    btn_upload.visibility = View.VISIBLE
                    diaryImage.visibility = View.VISIBLE
                    text_file.visibility = View.GONE
                    diaryImage.setImageBitmap(bitmap)
                }

            }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: FragmentAddDocumentsBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_add_documents,container,false)
        viewModel = ViewModelProvider(this,factory).get(DocumentsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.fragment = this
        prefs = PreferenceProvider(context!!.applicationContext)
        var token = prefs!!.getUserToken()
        viewModel.token = token
        var employeeCode = prefs!!.getEmployeeCode()
        viewModel.employee_code = employeeCode
        viewModel.documentListener = this
        return binding.root
    }

        fun getMimeType(path: String): String? {
            val extension = MimeTypeMap.getFileExtensionFromUrl(path)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    fun getPDFPath(uri: Uri?): String? {
        val id = DocumentsContract.getDocumentId(uri)
        val contentUri: Uri = ContentUris.withAppendedId(
            Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
        )
        val projection =
            arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor? = context?.getContentResolver()?.query(contentUri, projection, null, null, null)
        val column_index: Int? = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        return cursor?.getString(column_index!!)
    }

    override fun onStarted() {
        add_document_progress.show()
    }

    override fun onSuccess(uploadFile: String) {
        add_document_progress.hide()
        activity?.toast("File $uploadFile uploaded successfully")
    }

    override fun onFailure(message: String?) {
        add_document_progress.hide()
        activity?.toast(message!!)
    }
}






