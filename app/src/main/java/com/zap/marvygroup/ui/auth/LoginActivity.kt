package com.zap.marvygroup.ui.auth

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zap.marvygroup.HomeActivity
import com.zap.marvygroup.R
import com.zap.marvygroup.data.db.entities.User
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.databinding.ActivityLoginBinding
import com.zap.marvygroup.util.*

import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

const val ARG_REVEAL_START_LOCATION = "reveal_start_location"

class LoginActivity : AppCompatActivity(), AuthListener , KodeinAware {
    private lateinit var sharedImageViewModel: SharedImageViewModel
    private lateinit var locationViewModel: SharedLocationViewModel
    var base_url: String = "http://online.marvygroup.com/marvy_payroll/Image_Gallery/"
    private var pref: PreferenceProvider?=null
    override val kodein by kodein()
    private val factory : AuthViewModelFactory by instance()
    val anim:Animation = AlphaAnimation(0.0f,1.0f)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
      val viewModel:AuthViewModel = ViewModelProviders.of(this,factory).get(AuthViewModel::class.java)
        locationViewModel = this?.run {
            ViewModelProviders.of(this)[SharedLocationViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedImageViewModel = this?.run {
            ViewModelProviders.of(this)[SharedImageViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedImageViewModel = ViewModelProviders.of(this).get(SharedImageViewModel::class.java)

        binding.viewModel = viewModel
        viewModel.authListener = this
        pref = PreferenceProvider(this)
       // Log.e("lat",""+ pref?.getSavedLatValue())
        anim.setDuration(400); //You can manage the blinking time with this parameter
        anim.setStartOffset(10);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        val spannable = SpannableString("Apply For a Job !")
        spannable.setSpan(
            ForegroundColorSpan(Color.MAGENTA),
            0, 9,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        txt_apply.text = spannable
        txt_apply.startAnimation(anim)
        viewModel.getLoggedInUser().observe(this, Observer {user ->
            if (pref?.getUserToken()!= null)
               Intent(this, HomeActivity::class.java).also {
                   it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                   startActivity(it)

           }
        })

    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(user: User) {
       progress_bar.hide()
        root_layout.snackbar("${user.name}  is logged in")
        sharedImageViewModel.photo = user.photo
       // locationViewModel.photo = user.photo
      //  Log.e("user",""+locationViewModel.photo)
    }

    override fun onFailure(message: String) {
      progress_bar.hide()
        et_password.error = message
       // root_layout.snackbar(message)
    }
    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }
}
