package com.zap.marvygroup.ui.attendance

import androidx.lifecycle.ViewModel
import com.zap.marvygroup.data.repositories.UserRepository
import com.zap.marvygroup.util.ApiException
import com.zap.marvygroup.util.Coroutines
import com.zap.marvygroup.util.NoInternetException
import java.net.SocketTimeoutException

class AttendanceViewModel (
    private val repository: UserRepository
):ViewModel(){
    var attendanceListener: AttendanceListener?=null
    var attendanceOutListener:AttendanceOutListener?=null
    var employee_code: String?=null
    var token:String?=null
    var lattitude:Double?= 0.0
    var longitude:Double? = 0.0
    var is_entry:Boolean? = false
    var name: String? = null
    var currentTime: String? = null
     fun onAttendanceInButtonClick(){
         attendanceListener?.onStarted()
         if (employee_code.isNullOrEmpty()){
             attendanceListener?.onFailure("Please enable your gps ")
             return
         }
         if (token.isNullOrEmpty()){
             attendanceListener?.onFailure("Token should not be null")
             return
         }
         if (lattitude == 0.0){
             attendanceListener?.onFailure("Unable to find latitude")
             return
         }

         if (longitude == 0.0){
             attendanceListener?.onFailure("Unable to find longitude")
             return
         }
         Coroutines.main {
             try {
                 val address = repository.addAttendance(employee_code!!,token!!,lattitude!!,longitude!!,true)
                 address.address?.let {
                     attendanceListener?.onSuccess(it)
                     return@main
                 }
             }catch (e: ApiException){
                 attendanceListener?.onFailure(e.message!!)
             }catch (e: NoInternetException){
                 attendanceListener?.onFailure(e.message!!)
             }catch (e: SocketTimeoutException){
                 attendanceListener?.onFailure(e.message!!)
             }
         }

    }
    fun onAttendanceOutButtonClick(){
        if (employee_code.isNullOrEmpty()){
            attendanceOutListener?.onFailureOut("Please enable your gps ")
            return
        }
        if (token.isNullOrEmpty()){
            attendanceOutListener?.onFailureOut("Token should not be null")
            return
        }
        if (lattitude == 0.0){
            attendanceOutListener?.onFailureOut("Unable to find latitude")
            return
        }
        if (longitude == 0.0){
            attendanceOutListener?.onFailureOut("Unable to find longitude")
            return
        }
        Coroutines.main {
            try {
                val attendanceResponse = repository.addAttendance(employee_code!!,token!!,lattitude!!,longitude!!,false)
                attendanceResponse.address?.let {
                    attendanceOutListener?.onSuccessOut(it)
                    return@main
                }
            }catch (e: ApiException){
                attendanceOutListener?.onFailureOut(e.message!!)
            }catch (e: NoInternetException){
                attendanceOutListener?.onFailureOut(e.message!!)
            }catch (e: SocketTimeoutException){
                attendanceOutListener?.onFailureOut(e.message!!)
            }
        }
    }

}