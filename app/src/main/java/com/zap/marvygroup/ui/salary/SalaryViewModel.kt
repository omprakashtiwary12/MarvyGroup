package com.zap.marvygroup.ui.salary

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import java.net.SocketTimeoutException

class SalaryViewModel(
    private val repository: UserRepository
): ViewModel() {
    var employee_code: String?=null
    var salaryListener: SalaryListener?=null
    var token:String?=null

    suspend fun getMonthYear(){
        Coroutines.main {
            try {
                salaryListener?.onStarted()
                val salaryResponse = repository.getSalary(employee_code!!,token!!)
              //  Log.e("salaryResponse",""+salaryResponse)
            }catch (e: ApiException){
               // Log.e("api exception",e.message)
            }catch (e:SocketTimeoutException){
                salaryListener?.onFailure(e.message!!)
            }

        }
    }

    private fun getMonthValue() {

    }

    private fun getMonth(view:View){

    }
    private fun clicksListener(view: View){

    }
}
