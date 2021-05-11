package com.zap.marvygroup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.monitorinternet.InternetConnectivityListener
import com.zap.marvygroup.monitorinternet.MonitorInternet
import com.zap.marvygroup.ui.auth.LoginActivity
import com.zap.marvygroup.ui.notification.NotificationActivity
import com.zap.marvygroup.ui.splash.MarvySplash
import com.zap.marvygroup.util.*
import de.hdodenhof.circleimageview.CircleImageView
import eu.dkaratzas.android.inapp.update.Constants
import eu.dkaratzas.android.inapp.update.InAppUpdateManager
import eu.dkaratzas.android.inapp.update.InAppUpdateStatus
import kotlinx.android.synthetic.main.activity_apply_form.toolbar
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class HomeActivity : AppCompatActivity(), InAppUpdateManager.InAppUpdateHandler,NetCheckerReceiver.NetCheckerReceiverListener {

    private lateinit var locationViewModel: SharedLocationViewModel
    private lateinit var sharedImageViewModel: SharedImageViewModel
    private var isGPSEnabled = false
    private lateinit var inAppUpdateManager:InAppUpdateManager
    var imageString: String? = null
    private var dateString:String? = null
    lateinit var nav_view: NavigationView
    lateinit var navController: NavController
    private lateinit var preferenceProvider: PreferenceProvider
    private lateinit var appBarConfig: AppBarConfiguration
    lateinit var photoOfMe: CircleImageView
    lateinit var name: TextView
    lateinit var imageView:CircleImageView
    lateinit var employee_code: TextView
    var employeeCode: String? = null
    var notifyArrVal:String?=null
    private lateinit var mNoInternetDialog: Dialog
    companion object{
        private val monitorInternetLiveData = MutableLiveData<Boolean>()
        val getInternetMonitorLiveData: MutableLiveData<Boolean> get() = monitorInternetLiveData
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        nav_view = findViewById(R.id.nav_view)
        locationViewModel = this?.run {
            ViewModelProviders.of(this)[SharedLocationViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedImageViewModel = this?.run {
            ViewModelProviders.of(this)[SharedImageViewModel::class.java]
        }
        //Checking Internet
        registerReceiver(NetCheckerReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        showMessage(false)
        // Show notification count
        getNotificationCount()

        val headerView : View = nav_view.getHeaderView(0)
        photoOfMe = headerView.findViewById(R.id.profile_image)

        name = headerView.findViewById(R.id.headername)
        employee_code = headerView.findViewById(R.id.employee_code)
        navController = findNavController(R.id.nav_host_fragment)

        appBarConfig = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.my_profile,
                R.id.contact_us,
                R.id.documents,
                R.id.export_img,
                R.id.logout
            ), drawer_layout
        )

        nav_view.setupWithNavController(navController).also {
            Handler().postDelayed({
                // do something after 1 second
                setupActionBarWithNavController(navController, appBarConfig)
            }, 200)
        }

        notificationPanel.setOnClickListener {
            val intent = Intent(this,NotificationActivity::class.java)
            startActivity(intent)
        }
        val navigationView: NavigationView = nav_view

        navigationView.menu.findItem(R.id.logout)
            .setOnMenuItemClickListener { menuItem: MenuItem? ->
                preferenceProvider.clear()
                logout()
                true
            }


        headerView.setOnClickListener {
            navController.navigate(R.id.action_nav_home_to_changeProfileImage)
            drawer_layout.closeDrawers()
        }
        preferenceProvider = PreferenceProvider(applicationContext)
        employeeCode = preferenceProvider.getEmployeeCode()
        locationViewModel = ViewModelProviders.of(this).get(SharedLocationViewModel::class.java)
        GpsUtils(this).turnGPSOn(object : GpsUtils.OnGpsListener {

            override fun gpsStatus(isGPSEnable: Boolean) {
                this@HomeActivity.isGPSEnabled = isGPSEnable
            }
        })
        val inAppUpdate = InAppUpdateManager.Builder(this, 4)
            .resumeUpdates(true)
            .mode(Constants.UpdateMode.IMMEDIATE)
            .snackBarMessage("An update has just been downloaded.")
            .snackBarAction("RESTART")
            .handler(this);
             inAppUpdate.checkForAppUpdate()

         getDateAndTime()

    }

    private fun showMessage(isConnected: Boolean) {
        if (!isConnected) {

            val message = "Check the Internet Connection ! "
          //  Utility.showToast(this,message,false)
//            mNoInternetDialog = Dialog(this)
//            mNoInternetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//            mNoInternetDialog.setCancelable(true)
//            mNoInternetDialog.setContentView(R.layout.dialog_no_internet)
//            mNoInternetDialog.show()
        } else {
            //if has connection -> snackbar will disappear
            val message = "Connected to Internet "
           // Utility.showToast(this,message,true)
        }
    }

    private fun getNotificationCount() {
        AndroidNetworking.post("http://online.marvygroup.com/marvy_payroll/apiv2/message_update.php")
            .addBodyParameter("empcode", employeeCode)
            .addBodyParameter("action", "getMarkedAllReadNotificationinfo")
            .setPriority(Priority.MEDIUM)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    Log.e("response",response.toString())
                    val responseString = response?.getString("response")
                    val status = response?.getString("status")
                    notifyArrVal = response?.getString("notifyArrVal")
                    if (notifyArrVal.isNullOrEmpty()){

                    }else{
                        notification.setText(notifyArrVal)
                    }

                }

                override fun onError(anError: ANError?) {
                    Log.e("response", anError?.message)
                }

            })

    }

    private fun getDateAndTime() {
        //pre-condition: variable "context" is already defined as the Context object in this scope
        val dateString: String = DateFormat.format("MM/dd/yyyy", Date(Date().getTime())).toString()
        val sp: SharedPreferences =
            applicationContext.getSharedPreferences("com.zap.marvygroup", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sp.edit()
        editor.putString("dateAndTime", dateString)
        editor.commit()
        Log.e("dateString",dateString)
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }
    override fun onResume() {
        super.onResume()
        NetCheckerReceiver.netConnectionCheckerReceiver = this
        getPrefs()
    }
//employeeCode = preferenceProvider!!.getEmployeeCode()
    private fun getPrefs() {

        if (preferenceProvider.getUserImage()!= null){
            Glide.with(this).load(preferenceProvider.getUserImage()).into(photoOfMe)
        }else{
            Glide.with(this).load(sharedImageViewModel.photo) .placeholder(R.drawable.peter).into(
                photoOfMe
            )
        }
        if (preferenceProvider.getUserName()!= null){
            name.setText(preferenceProvider.getUserName())
        }
        if (preferenceProvider.getEmployeeCode()!= null){
            employee_code.setText(preferenceProvider.getEmployeeCode())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
                invokeLocationAction()
            }
        }
    }
    private fun invokeLocationAction() {
        when {
            //  !isGPSEnabled -> latLong.text = getString(R.string.enable_gps)

            isPermissionsGranted() -> startLocationUpdate()

            // shouldShowRequestPermissionRationale() -> latLong.text = getString(R.string.permission_request)

            else -> ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        locationViewModel.getLocationData().observe(this, Observer {
            // latLong.text =  getString(R.string.latLong, it.longitude, it.latitude)
            locationViewModel.latValue = it.latitude
            locationViewModel.longValue = it.longitude
        })
    }

    /** Ask the NavController to handle "navigate up" events. */
    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfig) || super.onSupportNavigateUp()
    }
    private fun logout() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    /** Close the drawer when hardware back is pressed. */
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    private fun isPermissionsGranted() =
        ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }
    /*
     * when connection changed, this will be called
     */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }


    override fun onDestroy() {
        super.onDestroy()
        viewModelStore.clear()
    }

    override fun onInAppUpdateError(code: Int, error: Throwable?) {
        // Log.e("message", error!!.message)
    }

    override fun onInAppUpdateStatus(status: InAppUpdateStatus?) {
        if (status!!.isDownloaded) {
            val rootView =
                window.decorView.findViewById<View>(android.R.id.content)
            val snackbar = Snackbar.make(
                rootView,
                "An update has just been downloaded.",
                Snackbar.LENGTH_INDEFINITE
            )
            snackbar.setAction(
                "RESTART"
            ) { view: View? ->
                // Triggers the completion of the update of the app for the flexible flow.
                inAppUpdateManager.completeUpdate()
            }
            snackbar.show()
        }
    }



}
const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101