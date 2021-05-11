package com.zap.marvygroup.ui.profile

import android.R
import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.zap.marvygroup.data.db.entities.Profile
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import java.net.SocketTimeoutException

class ProfileViewModel(
    private val repository: UserRepository
): ViewModel() {
    var aadhar:String = ""
    var acHolder: String = ""
    var ac_holder = ObservableField<CharSequence>()
    var ac_no: String? = null
    var address: String? = null
    var bank_branch_add: String? = null
    var bank_name: String? = null
    var basic: String? = null
    var bonus_add: String? = null
    var city: String? = null
    var company_id: String? = null
    var conveyance: String? = null
    var da: String? = null
    var dedesi: String? = null
    var dedpf: String? = null
    var dedtds: String? = null
    var deduction: String? = null
    var deduction1: String? = null
    var des_name: String? = null
    var dob: String? = null
    var doj: String? = null
    var dol: String? = null
    var dp_name: String? = null
    var driver: String? = null
    var education: String? = null
    var email: String? = null
    var emp_code: String? = null
    var emp_name = ObservableField<CharSequence>()
    var emp_type: String? = null
    var emp_una: String? = null
    var esic: String? = null
    var esic_clinic: String? = null
    var fatherhusband: String? = null
    var grade_all: String? = null
    var gratualty: String? = null
    var helper: String? = null
    var hra: String? = null
    var id: String? = null
    var ifsc: String? = null
    var loan_installment: String? = null
    var loc_name: String? = null
    var lta: String? = null
    var medical: String? = null
    var medical_rebm: String? = null
    var other_deduction1: String? = null
    var other_deduction2: String? = null
    var otheradd1: String? = null
    var otheradd2: String? = null
    var pan: String? = null
    var payment_mode: String? = null
    var pension: String? = null
    var permanent_address: String? = null
    var pf: String? = null
    var pf_withdran: String? = null
    var phone: String? = null
    var photo: String? = null
    var pin: String? = null
    var probation: String? = null
    var qualification: String? = null
    var registration_date: String? = null
    var reset_password_token: String? = null
    var resigned_date: String? = null
    var sex: String? = null
    var splallowance: String? = null
    var statusech: String? = null
    var ta: String? = null
    var uniform: String? = null
    var washing: String? = null
    var profileListener: ProfileListener?=null
    var employee_code: String?=null
    var token:String?=null
    var project: ObservableField<Profile> = ObservableField()
    val data = MutableLiveData<Profile>()


    fun setProject(project: Profile) {
        this.project.set(project)
    }

    suspend fun getProfile() {
        Coroutines.main {
            try {
                profileListener?.onStarted()
                val response = repository.getProfileDetails(employee_code!!, token!!)
                response.profile.let {
                    profileListener?.onSuccess(it)
                    project.set(it)
                    data.value=response.profile
                    aadhar = it?.aadhar.toString()
                    acHolder = it?.ac_holder.toString()
                    photo = it?.photo
                    it.photo?.let { it1 -> repository.saveUserImage(it1) }
                    return@main
                }
                profileListener?.onFailure("Please logout from other device")
            }catch (e: ApiException){
                profileListener?.onFailure("Please Logout from other device")
            }catch (e: NoInternetException){
                profileListener?.onFailure(e.message!!)
            }catch (e:SocketTimeoutException){
                profileListener?.onFailure(e.message!!)
            }

        }
    }
    @BindingAdapter("bind:imageUrl")
    fun loadImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.getContext())
            .load(imageUrl)
            .into(view)
    }



}

