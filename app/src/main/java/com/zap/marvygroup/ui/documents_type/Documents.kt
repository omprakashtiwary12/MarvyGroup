package com.zap.marvygroup.ui.documents_type

import android.R.attr.path
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.inlineactivityresult.startActivityForResult
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.androidnetworking.interfaces.UploadProgressListener
import com.google.gson.Gson
import com.obsez.android.lib.filechooser.ChooserDialog
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.databinding.ActivityDocumentsBinding
import com.zap.marvygroup.ui.FileUtils
import com.zap.marvygroup.ui.documents.DocumentListener
import com.zap.marvygroup.util.*
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.activity_documents.*
import kotlinx.android.synthetic.main.fragment_add_documents.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.shouheng.compress.Compress
import me.shouheng.compress.listener.CompressListener
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.File


class Documents : AppCompatActivity(), KodeinAware,DocumentListener {
    override val kodein by kodein()
    private var prefs: PreferenceProvider? = null
    private var mAddressModel = AddressModel()
    private var doctypeList:List<String>? = null
    private lateinit var docTypeViewModel:DocumentTypeViewModel
    private lateinit var viewModel: DocViewModel
    private var token:String? = null
    private var file:File?= null
    private var compressedImageFile:File? = null
    private var employeeCode:String?=null
    private var fileType:String?=null
    private val factory:DocumentTypeFactory by instance()
    private val documentViewTypeFactory:DocumentViewTypeFactory by instance()
    var onItemSelected = false
    var compress:Compress? = null
    var resultFile:File?=null
    var objectList:List<Record> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityDocumentsBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_documents
        )
        toolbar.setTitle("Documents")
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val builder = VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        docTypeViewModel = ViewModelProvider(this, factory).get(DocumentTypeViewModel::class.java)
        viewModel = ViewModelProvider(this, documentViewTypeFactory).get(DocViewModel::class.java)
        prefs = PreferenceProvider(applicationContext)
        binding.viewModel = viewModel
         token = prefs!!.getUserToken()
        viewModel.token = token
        employeeCode = prefs!!.getEmployeeCode()
        viewModel.employee_code = employeeCode
        viewModel.documentListener = this
        getPdfDocumentList(employeeCode)
        itemsswipetorefresh.setOnRefreshListener {
            getPdfDocumentList(employeeCode)
            itemsswipetorefresh.isRefreshing = false
        }
        CoroutineScope(Main).launch {
           // initRecyclerView(objectList)
            val list = docTypeViewModel.getQuotes()
            doctypeList = list.doctype
            Toast.makeText(applicationContext, doctypeList.toString(), Toast.LENGTH_SHORT).show()
            Log.e("responseList", doctypeList.toString())
            btn_upload_doc.setOnClickListener {
                if (file == null){
                    toast("You must select a file")
                }else{
                    uploadFilesOnServer()
                }

            }
            val adapter = ArrayAdapter(
                applicationContext, // Context
                android.R.layout.simple_spinner_item,
                // Layout
                doctypeList!! // Array
            )
            adapter.setDropDownViewResource(R.layout.simple_drop_down)
            address_tag_type.adapter = adapter
            var position = doctypeList!!.map { it.toLowerCase() }.indexOf(mAddressModel.doctypeList?.toLowerCase())
            if(position == -1){
                position = doctypeList!!.size - 1
            }

            address_tag_type.post {
                address_tag_type.setSelection(position)
            }
            // Set an on item selected listener for spinner object
            address_tag_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    // Display the selected item text on text view
                    // reqAddAddressViewModel.addressType = parent.getItemAtPosition(position).toString()
                    val selectionName = parent.getItemAtPosition(position).toString()
                    onItemSelected = selectionName != "Select Document Type"
                    txt_save_as.setText(parent.getItemAtPosition(position).toString())
                    viewModel.type = parent.getItemAtPosition(position).toString()
                    fileType = parent.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // Another interface callback

                }
            }
        }
        txt_save_as.setOnClickListener(View.OnClickListener {
            address_tag_type.performClick()
        })

        // Set the drop down view resource
        selct_text_view.setOnClickListener {
            chooseFile(onItemSelected)
        }
    }
    private fun getPdfDocumentList(employeeCode: String?) {
        AndroidNetworking.post("http://online.marvygroup.com/marvy_payroll/apiv2/get_documentlist.php")
            .addBodyParameter("employee_code", employeeCode)
            .setPriority(Priority.MEDIUM)
            .build()

            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonObject = JSONObject(response.toString())

                    val records = jsonObject.getJSONArray("records")

                    val gson = Gson()

                     objectList = gson.fromJson(records.toString(), Array<Record>::class.java).asList()
                    Log.e("records", objectList.toString())

                    initRecyclerView(objectList.reversed())
                }

                override fun onError(anError: ANError?) {
                    // Log.e("response", anError?.message)
                }

            })

    }

    private fun initRecyclerView(objectList: List<Record>) {
        rv_list_doc.also {
            it.layoutManager = LinearLayoutManager(this)
            it.setHasFixedSize(true)
            it.adapter = DocListAdapter(this, objectList)
            it.scheduleLayoutAnimation()
        }
    }

    private fun chooseFile(onItemSelected: Boolean) {
        if (onItemSelected){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent){
                    success, data ->
                if (success){
                    val selectedImageResource = data.data
                    val imagePath = FileUtils.getRealPathFromURI_API19(this,selectedImageResource)
                    file_name.setText(imagePath.substring(imagePath.lastIndexOf("/") + 1))
                    file = File(imagePath)
                    Log.e("file",file.toString())
                }
            }

        }else{
            //selct_text_view.isClickable = false
            toast("Please select document type")
        }
    }

    private fun uploadFilesOnServer(){
        doc_progress_bar.show()
        AndroidNetworking.upload(" http://online.marvygroup.com/marvy_payroll/apiv2/document/add?")
            .addMultipartFile("file",file)
            .addMultipartParameter("employee_code",employeeCode)
            .addMultipartParameter("access_token",token)
            .addMultipartParameter("type",fileType)
            .setPriority(Priority.MEDIUM)
            .build()
            .setUploadProgressListener { bytesUploaded, totalBytes ->

            }
            .getAsJSONObject(object:JSONObjectRequestListener{
                override fun onResponse(response: JSONObject?) {
                    doc_progress_bar.hide()
                    val jsonObject = JSONObject(response.toString())
                    val isSuccessful = jsonObject.getString("isSuccessful")
                    val message = jsonObject.getString("message")
                    toast(response.toString())
                   // Log.e("uploadServerResponse",response.toString())
                }

                override fun onError(anError: ANError?) {
                    Log.e("uploadServerResponse",anError?.message)
                }

            })
    }
    override fun onStarted() {
        doc_progress_bar.show()
    }

    override fun onSuccess(uploadFile: String) {
        doc_progress_bar.hide()
       toast(uploadFile)

    }

    override fun onFailure(message: String?) {
        doc_progress_bar.hide()
        if (message != null) {
            toast(message)
        }
    }
}


