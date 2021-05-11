package com.zap.marvygroup.ui.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.android.material.appbar.MaterialToolbar
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.util.Utility
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_notification.*
import org.json.JSONObject

class NotificationActivity : AppCompatActivity() {
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter:NotificationListAdapter
    var employeeCode: String? = null
    var jsonObject:String?=null
    private lateinit var toolbar:MaterialToolbar
    val listOfNotification = arrayListOf<String>()
    private lateinit var preferenceProvider: PreferenceProvider
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)
        toolbar = findViewById(R.id.notification_toolbar)
        toolbar.setTitle("Notification")
        toolbar.setTitleTextColor(resources.getColor(R.color.white))
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        preferenceProvider = PreferenceProvider(applicationContext)
        employeeCode = preferenceProvider.getEmployeeCode()
        linearLayoutManager = LinearLayoutManager(this)
        rv_notification_list.layoutManager = linearLayoutManager
        getAllReadMessage()
        clearNotification()
    }
    private fun getAllReadMessage(){
        AndroidNetworking.post("http://online.marvygroup.com/marvy_payroll/marvy-api-ver-1/notification-api-all.php")
            .addBodyParameter("empcode", employeeCode)
            .addBodyParameter("action", "getMarkedAllReadNotificationinfo")
            .addBodyParameter("accessToken", "getMarkedAllReadNotificationinfoAuthenticate")
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    // Log.e("response",response.toString())
                    val status = response?.getInt("status")
                    if(status == 200){
                        val jsonArray = response!!.getJSONArray("notifymsg")
                        for (i in 0 until jsonArray.length()) {
                            jsonObject = jsonArray.getJSONObject(i).getString("Message")
                            listOfNotification.add(jsonObject.toString())
                        }
                        adapter = NotificationListAdapter(listOfNotification)
                        rv_notification_list.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }else {
                        val message = response?.getString("message")
                        Utility.showToast(this@NotificationActivity,message,false)
                    }
                }
                override fun onError(anError: ANError?) {

                }

            })

    }

    private fun clearNotification() {
        AndroidNetworking.post("http://online.marvygroup.com/marvy_payroll/apiv2/notification_count.php")
            //.addBodyParameter("empcode", employeeCode)
            .addBodyParameter("action", "getMarkedAllReadNotificationinfo")
           // .addBodyParameter("accessToken", "getMarkedAllReadNotificationinfoAuthenticate")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.e("responseremoval",response.toString())

                }

                override fun onError(anError: ANError?) {
                    Log.e("response", anError?.message)
                }

            })

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}