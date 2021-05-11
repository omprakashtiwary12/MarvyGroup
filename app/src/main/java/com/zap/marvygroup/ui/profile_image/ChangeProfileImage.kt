package com.zap.marvygroup.ui.profile_image

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.afollestad.inlineactivityresult.startActivityForResult
import com.zap.marvygroup.HomeActivity
import com.zap.marvygroup.R
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.databinding.ChangeProfileImageFragmentBinding
import com.zap.marvygroup.ui.FileUtils
import com.zap.marvygroup.util.*
import kotlinx.android.synthetic.main.change_profile_image_fragment.*
import org.kodein.di.KodeinAware
import java.io.File
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class ChangeProfileImage : Fragment(), KodeinAware,ProfileListener {
    override val kodein by kodein()
    private lateinit var viewModel: ChangeProfileImageViewModel
    private lateinit var locationViewModel: SharedLocationViewModel
    private lateinit var sharedImageViewModel: SharedImageViewModel
    private var prefs: PreferenceProvider? = null
    lateinit var globalVars: GlobalVars
    private val factory: ChangeProfileViewModelFactory by instance()

    fun onProfileImageClick(view: View, changeProfileImageViewModel: ChangeProfileImageViewModel){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("image/*")
        startActivityForResult(intent){
                success, data ->
            if (success){
                val selectedImageResource = data.data
                val imagePath = FileUtils.getRealPathFromURI_API19(activity,selectedImageResource)
              //  Log.e("imagePath",imagePath)

                changeProfileImageViewModel.imageFile = File(imagePath)
                val bitmap =  MediaStore.Images.Media.getBitmap(activity?.contentResolver,selectedImageResource)
                if (bitmap!= null){
                    profile_img.visibility = View.VISIBLE
                    profile_img.setImageBitmap(bitmap)
                }

            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val binding: ChangeProfileImageFragmentBinding = DataBindingUtil.inflate(inflater,
             R.layout.change_profile_image_fragment,container,false)
         viewModel = ViewModelProvider(this,factory).get(ChangeProfileImageViewModel::class.java)
         binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.fragment = this
        globalVars = GlobalVars()
        prefs = PreferenceProvider(context!!.applicationContext)
        var token = prefs!!.getUserToken()
        viewModel.token = token
        var employeeCode = prefs!!.getEmployeeCode()
        viewModel.employee_code = employeeCode
        viewModel.profileListener = this
        locationViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedLocationViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        sharedImageViewModel = activity?.run {
            ViewModelProviders.of(this)[SharedImageViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        return binding.root
    }


    override fun onStarted() {
        profile_progress.show()
    }

    override fun onSuccess(uploadFile: String) {
        profile_progress.hide()
        sharedImageViewModel.photo = uploadFile
        prefs?.saveUserImage(uploadFile)
        activity?.toast("File $uploadFile uploaded successfully")
        val intent = Intent(activity, HomeActivity::class.java)
        startActivity(intent)
        activity?.finish()

    }

    override fun onFailure(message: String?) {
        profile_progress.hide()
        activity?.toast(message!!)
    }

}