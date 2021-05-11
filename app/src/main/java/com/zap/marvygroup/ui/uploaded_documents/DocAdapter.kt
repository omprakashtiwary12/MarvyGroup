package com.zap.marvygroup.ui.uploaded_documents

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zap.marvygroup.R
import com.zap.marvygroup.databinding.DocItemBinding


class DocAdapter (
    private val dataList: ArrayList<DocData>
) : RecyclerView.Adapter<DocAdapter.MoviesViewHolder>(){

    override fun getItemCount() = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.doc_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.docItemBinding.docData = dataList[position]
       // holder.recyclerviewMovieBinding.docImage = dataList[position]
//        holder.recyclerviewMovieBinding.buttonBook.setOnClickListener {
//            listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.buttonBook, movies[position])
//        }
//        holder.recyclerviewMovieBinding.layoutLike.setOnClickListener {
//            listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.layoutLike, movies[position])
//        }
    }

    fun removeAt(adapterPosition: Int) {
        dataList.removeAt(adapterPosition)
    }


    inner class MoviesViewHolder(
       val docItemBinding: DocItemBinding
    ) : RecyclerView.ViewHolder(docItemBinding.root)

}