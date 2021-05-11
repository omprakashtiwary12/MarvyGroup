package com.zap.marvygroup.ui.uploaded_pdf

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.ui.uploaded_documents.DocData
import com.zap.marvygroup.ui.uploaded_documents.DocListener
import com.zap.marvygroup.ui.uploaded_documents.SwipeToDeleteCallback
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.hide
import com.zap.marvygroup.util.show
import com.zap.marvygroup.util.toast

import kotlinx.android.synthetic.main.uploaded_pdf_fragment.*
import org.json.JSONObject
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class UploadedPdf : Fragment(), KodeinAware, DocListener {
    private var prefs: PreferenceProvider? = null
    override val kodein by kodein()
    private val factory: PdfViewModelFactory by instance()
    private lateinit var viewModel: UploadedPdfViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.uploaded_pdf_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prefs = PreferenceProvider(context!!.applicationContext)
        viewModel = ViewModelProvider(this,factory).get(UploadedPdfViewModel::class.java)
        var employeeCode = prefs!!.getEmployeeCode()
        viewModel.employee_code = employeeCode
        viewModel.docListener = this
        bindUI()
    }

    private fun bindUI() = Coroutines.main {
        viewModel.getPdfList()
    }



    override fun onStarted() {
        pdfprogress_bar.show()
    }

    override fun onSuccess(docData: ArrayList<DocData>) {
        pdfprogress_bar.hide()
        initRecyclerView(docData)
    }

    private fun initRecyclerView(docData: ArrayList<DocData>) {
        activity?.toast("Swipe left right to delete record")
        pdf_list.also {
            it.layoutManager = LinearLayoutManager(requireContext())
            it.setHasFixedSize(true)
            it.adapter = PdfAdapter(docData)


            val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val pos = viewHolder.adapterPosition
                    removeFromApi(docData.get(pos).id)
                    docData.removeAt(pos)
                    (it.adapter as PdfAdapter).notifyItemRemoved(pos)
                }
            }

            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(pdf_list)
        }
    }
    private fun removeFromApi(id: String) {
        AndroidNetworking.post(" http://online.marvygroup.com/marvy_payroll/apiv2/showlist_delete.php")
            .addBodyParameter("id", id)
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    try {
                        val jsonObject = JSONObject(response.toString())
                        Log.e("jsonresponse",jsonObject.toString())
                        var isSuccessful = jsonObject.get("isSuccessful")
                        var message:String = jsonObject.get("message").toString()
                        activity?.toast(message)

                    }catch (e:NullPointerException){
                        e.printStackTrace()
                        activity?.toast("Documents Not found")
                    }


                }

                override fun onError(anError: ANError?) {
                    activity?.toast("Documents Not found")
                }

            })
    }


    override fun onFailure(message: String) {
        pdfprogress_bar.hide()
    }

}