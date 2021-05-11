package com.zap.marvygroup.ui.notification

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zap.marvygroup.R
import kotlinx.android.synthetic.main.notification_row.view.*

class NotificationListAdapter(private val list: List<String>)
    : RecyclerView.Adapter<NotificationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.notification_row, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
       holder.notificationText.text = list.get(position)
    }

    override fun getItemCount(): Int {
     return list.size
    }
}

class NotificationViewHolder(view:View): RecyclerView.ViewHolder(view) {
     val notificationText = view.txt_notification
}

