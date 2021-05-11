package com.zap.marvygroup.ui.apply

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import com.zap.marvygroup.util.toast
import okhttp3.MultipartBody
import org.json.JSONObject
import java.io.File
import java.net.SocketTimeoutException
class ApplyViewModel(
    private val repository: UserRepository
) : ViewModel() {
    var name: String? = null
    var mobile: String? = null
    var email: String? = null
    var subject: String? = null
    var message: String? = null
    var resume: File? = null
    val mimType: String? = null
     // var resume: MultipartBody.Part? = null
    var applyFormListener: ApplyFormListener? = null

    fun applyButtonClick(view: View) {
        applyFormListener?.onStarted()
        if (name.isNullOrEmpty()) {
            applyFormListener?.onFailure("Name is required")
            return
        }
        if (mobile.isNullOrEmpty()) {
            applyFormListener?.onFailure("Mobile number is required")
            return
        }
        if (email.isNullOrEmpty()) {
            applyFormListener?.onFailure("Email is required")
            return
        }
        if (subject.isNullOrEmpty()) {
            applyFormListener?.onFailure("Subject is required")
            return
        }

        if (resume == null) {
            applyFormListener?.onFailure("Resume is required")
            return
        }
        Coroutines.main {
                try {
                    AndroidNetworking.upload(" http://online.marvygroup.com/marvy_payroll/apiv2/apply/add")
                        .addMultipartFile("file",resume)
                        .addMultipartParameter("name",name)
                        .addMultipartParameter("mobile",mobile)
                        .addMultipartParameter("email",email)
                        .addMultipartParameter("subject",subject)
                        .addMultipartParameter("message",message)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .setUploadProgressListener { bytesUploaded, totalBytes ->

                        }
                        .getAsJSONObject(object: JSONObjectRequestListener {
                            override fun onResponse(response: JSONObject?) {

                                // doc_progress_bar.hide()
                                val jsonObject = JSONObject(response.toString())
                                Log.e("response", response.toString())
                                val isSuccessful = jsonObject.getString("isSuccessful")
                                val message = jsonObject.getString("message")
                                applyFormListener?.onSuccess(message)
                                Log.e("uploadServerResponse",response.toString())
                            }

                            override fun onError(anError: ANError?) {
                                Log.e("uploadServerResponse",anError?.message)
                                applyFormListener?.onFailure(anError?.message!!)
                            }

                        })
                }catch (e: ApiException) {
                applyFormListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                applyFormListener?.onFailure(e.message!!)
            }catch (e: SocketTimeoutException){
                applyFormListener?.onFailure(e.message!!)
            }
                // doc_progress_bar.show()

            }
//            try {
//                val applyFormResponse =
//                    repository.applyForm(name!!, mobile!!, email!!, subject!!, message!!, resume!!)
//
//                applyFormResponse.applyForm.let {
//                    applyFormListener?.onSuccess(it)
//                    // repository.saveUser(it)
//                    // it.token?.let { it1 -> repository.saveToken(it1) }
//                    return@main
//                }
//                applyFormListener?.onFailure(applyFormResponse.message!!)
//            } catch (e: ApiException) {
//                applyFormListener?.onFailure(e.message!!)
//            } catch (e: NoInternetException) {
//                applyFormListener?.onFailure(e.message!!)
//            }catch (e: SocketTimeoutException){
//                applyFormListener?.onFailure(e.message!!)
//            }
        }


}