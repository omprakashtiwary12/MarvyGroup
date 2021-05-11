package com.zap.marvygroup.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.zap.marvygroup.R
import com.zap.marvygroup.data.db.entities.Profile
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.databinding.FragmentMyProfileBinding
import com.zap.marvygroup.util.*

import kotlinx.android.synthetic.main.fragment_my_profile.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance



/**
 * A simple [Fragment] subclass.
 */
class MyProfile : Fragment(), KodeinAware , ProfileListener{
override val kodein by kodein()
    private lateinit var viewModel: ProfileViewModel
    private val factory: ProfileViewModelFactory by instance()
    private var prefs: PreferenceProvider? = null
    private lateinit var profile: Profile
    var ttb: Animation? = null
    var stb: Animation? = null
    var ftb: Animation? = null
    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding: FragmentMyProfileBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_my_profile,container,false)
        viewModel = ViewModelProvider(this,factory).get(ProfileViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        prefs = PreferenceProvider(context!!.applicationContext)
        var token = prefs!!.getUserToken()
        var employeeCode = prefs!!.getEmployeeCode()

        viewModel.token = token
      //  Log.e("token",viewModel.token)
        viewModel.employee_code = employeeCode
        viewModel.profileListener = this

         ttb = AnimationUtils.loadAnimation(activity, R.anim.ttb)
         stb = AnimationUtils.loadAnimation(activity,R.anim.stb)
         ftb = AnimationUtils.loadAnimation(activity,R.anim.ftb)

        Coroutines.main {
            val listData = viewModel.getProfile()
           // Log.e("profile Data",""+listData)
        }
        return binding.root
    }
    override fun onStarted() {
       // Log.e("started","onStarted")
        profileProgressbar.show()
    }

    override fun onSuccess(profile: Profile) {
        if (profile != null){
          //  profile_root.visibility = View.VISIBLE
            vUserProfileRoot.visibility=View.VISIBLE
            activityMain.visibility = View.VISIBLE
            nestedview.startAnimation(ftb)
            vUserProfileRoot.startAnimation(ttb)
            activityMain.startAnimation(ttb)
            profileProgressbar.hide()
        }else{
            profile_root.visibility = View.GONE
            vUserProfileRoot.visibility=View.GONE
            activityMain.visibility = View.GONE
            profile_root.visibility = View.GONE
        }
    }

    override fun onFailure(message: String) {
        profileProgressbar.hide()
       // activity?.toast(message)
        Utility.showToast(activity!!,"Please Logout from other device",false)
    }
}
