package com.zap.marvygroup.ui.splash

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.zap.marvygroup.monitorinternet.MonitorInternet
import com.zap.marvygroup.HomeActivity
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.monitorinternet.InternetConnectivityListener
import com.zap.marvygroup.ui.auth.LoginActivity
import com.zap.marvygroup.util.NetCheckerReceiver
import com.zap.marvygroup.util.Utility
import kotlinx.android.synthetic.main.marvy_splash.*
import java.security.KeyStore
import java.util.*

class MarvySplash : AppCompatActivity(), NetCheckerReceiver.NetCheckerReceiverListener{
    private val TAG = "MarvySplash"
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 34
    private var isGPSEnabled = false
    private var prefs: PreferenceProvider? = null

    companion object {
        private const val BACKGROUND_ANIMATION_DURATION = 5000L

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(R.style.Base_Theme_Sunflower)
        setContentView(R.layout.marvy_splash)

        prefs = PreferenceProvider(this)
        background.animate()
            .alpha(1f)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {

                }
            })
            .duration = BACKGROUND_ANIMATION_DURATION

        //Checking Internet
        registerReceiver(NetCheckerReceiver(), IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        showMessage(false)
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_NETWORK_STATE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) {
                        // do you work now
                        startNewActivity()
                    }

                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).withErrorListener {
                Toast.makeText(getApplicationContext(), "Error occurred! " + it.toString(), Toast.LENGTH_SHORT).show();
            }
            .onSameThread()
            .check()
    }

    private fun showMessage(isConnected: Boolean) {
        if (!isConnected) {

            val message = "Check the Internet Connection ! "
           // Utility.showToast(this,message,false)
        } else {
            //if has connection -> snackbar will disappear

        }


    }

    override fun onStart() {
        super.onStart()
       // invokeLocationAction()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GPS_REQUEST) {
                isGPSEnabled = true
               // invokeLocationAction()
            }
        }
    }
//    private fun invokeLocationAction() {
//        when {
//            !isGPSEnabled -> latLong.text = getString(R.string.enable_gps)
//
//            isPermissionsGranted() -> startLocationUpdate()
//            isLocationFound() -> startNewActivity()
//            shouldShowRequestPermissionRationale() -> showSnakebar()
//
//            else -> ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
//                LOCATION_REQUEST
//            )
//        }
//    }




    private fun startNewActivity(){
        if (prefs?.getUserToken()!= null){
            Intent(this, HomeActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }else {
            Intent(this, LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }

    }

    /*
   * when connection changed, this will be called
   */
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

    override fun onResume() {
        super.onResume()
        NetCheckerReceiver.netConnectionCheckerReceiver = this
    }


//    private fun showSnakebar() {
//        val builder = AlertDialog.Builder(this)
//        builder.setMessage("Permission to access the Location is required for this app to show Location.")
//                .setTitle("Permission required")
//
//                    builder.setPositiveButton("OK"
//                    ) { dialog, id ->
//                Log.i(TAG, "Clicked")
//                        checkFromSettings()
//                    }
//
//            val dialog = builder.create()
//        dialog.show()
//    }


//    private fun checkFromSettings() {
//
//                // Build intent that displays the App settings screen.
//                val intent = Intent().apply {
//                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
//                    data = Uri.fromParts("package", APPLICATION_ID, null)
//                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                }
//                startActivity(intent)
//
//    }



//    private fun startLocationUpdate() {
//        locationViewModel.getLocationData().observe(this, Observer {
//            latLong.text =  getString(R.string.latLong, it.longitude, it.latitude)
//        })
//    }
//    private fun isPermissionsGranted() =
//        ActivityCompat.checkSelfPermission(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) == PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) == PackageManager.PERMISSION_GRANTED
//
//    private fun shouldShowRequestPermissionRationale() =
//        ActivityCompat.shouldShowRequestPermissionRationale(
//            this,
//            Manifest.permission.ACCESS_FINE_LOCATION
//        ) && ActivityCompat.shouldShowRequestPermissionRationale(
//            this,
//            Manifest.permission.ACCESS_COARSE_LOCATION
//        )
//
//    @SuppressLint("MissingPermission")
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            LOCATION_REQUEST -> {
//                invokeLocationAction()
//            }
//        }
//    }


}


const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101



