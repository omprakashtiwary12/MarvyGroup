package com.zap.marvygroup.ui.reset

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.zap.marvygroup.R
import com.zap.marvygroup.data.db.entities.User
import com.zap.marvygroup.databinding.ActivityForgotPasswordBinding
import com.zap.marvygroup.ui.auth.AuthListener
import com.zap.marvygroup.ui.auth.LoginActivity
import com.zap.marvygroup.util.hide
import com.zap.marvygroup.util.show
import com.zap.marvygroup.util.snackbar
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.root_layout
import kotlinx.android.synthetic.main.app_toolbar.toolbar
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class ForgotPassword : AppCompatActivity() , KodeinAware, AuthListener {
    override val kodein by kodein()
    private val factory : ResetViewModelFactory by instance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        val binding: ActivityForgotPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password)
        val viewModel:ForgetViewModel = ViewModelProviders.of(this,factory).get(ForgetViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.authListener = this

        toolbar.title = "Forgot Password"
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val slidedownAnimation: Animation = AnimationUtils.loadAnimation(
            applicationContext,
            R.anim.slide_up
        )
        toolbar?.startAnimation(slidedownAnimation)
        toolbar?.setNavigationOnClickListener { onBackPressed() }
        toolbar.setOnClickListener {
            val intent: Intent = Intent(this,
                ForgotPassword::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.bottom_up, R.anim.nothing)
        }

    }



    override fun onStarted() {
        circularProgressBar.show()
    }

    override fun onSuccess(user: User) {
        circularProgressBar.hide()
        root_layout.snackbar("${user.name}  is logged in")

    }

    override fun onFailure(message: String) {
        circularProgressBar.hide()
        text_employee_code.error = message
         root_layout.snackbar(message)
        if (message!= null){
            Handler().postDelayed({
                /* Create an Intent that will start the Menu-Activity. */
                val mainIntent = Intent(this, LoginActivity::class.java)
                startActivity(mainIntent)
            }, 5000)
        }


    }
    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

}

