package com.zap.marvygroup.ui.uploaded_documents

import android.view.View



interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, movie: DocResponse)
}