package com.zap.marvygroup.ui.documents_type

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zap.marvygroup.R
import com.zap.marvygroup.databinding.PdfItemBinding
import com.zap.marvygroup.ui.ShowSalaryActivity
import java.io.File


class DocListAdapter(
    private val context: Context,
    private val dataList: List<Record>
) : RecyclerView.Adapter<DocListAdapter.MoviesViewHolder>(){

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.pdf_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.docItemBinding.textPdf.text = dataList[position].documentType
        holder.docItemBinding.textPdfDate.text = dataList[position].updateDate
        val url = dataList[position].documentName
        Log.e("url",url)
        //val file = File(url)
        holder.docItemBinding.img.setOnClickListener {
            beginDownload(url)
        }
    }

    private fun beginDownload(url: String) {
        if (!url.isNullOrEmpty()){
            var bundle = bundleOf("urlval" to url)
            val intent = Intent(context, ShowSalaryActivity::class.java)
            intent.putExtra("key_url",url)
            Log.e("link",url)
            context.startActivity(intent)

        }else{

        }
    }

    private fun getIntentForFile(filePath: String): Intent {
        val intent = Intent()

        val uri = FileProvider.getUriForFile(
            context,
            context.applicationContext.packageName + ".provider",
            File(filePath)
        )
        intent.action = Intent.ACTION_VIEW
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.setDataAndType(uri, getFileContentType(filePath))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent
    }

    fun getFileContentType(filePath: String): String? {
        val file = File(filePath)
        val map = MimeTypeMap.getSingleton()
        val ext = MimeTypeMap.getFileExtensionFromUrl(file.name)
        var type = map.getMimeTypeFromExtension(ext)
        if (type == null) type = "*/*"
        return type
    }

    inner class MoviesViewHolder(
        val docItemBinding: PdfItemBinding
    ) : RecyclerView.ViewHolder(docItemBinding.root)

}