package com.zap.marvygroup.data.preferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.zap.marvygroup.MarvyGroupApplication


private const val LAT_KEY = "lat_value"
private const val LAN_KEY = "lan_value"
private const val USER_TOKEN = "token_value"
private const val PAST_DATE = "current_date"
private const val OUT_DATE = "out_date"
private const val OUT_ADDRESS = "out_address"
private const val ADDRESS = "current_address"
private const val USER_IMAGE = "image_value"
private const val EMPLOYEE_CODE = "employee_code"
private const val USER_NAME = "user_name"
private const val SG_SHARED_PREFERENCES = "shared_preferences"
private const val PREFERENCES_FILE_NAME = "PREFERENCES_FILE_NAME"
class PreferenceProvider (
    context: Context
){

    private val preference = context.getSharedPreferences("marvy_pref",0)

    fun saveLat(lattitudeVal: Double){
        preference.edit().putFloat(
            LAT_KEY,
            lattitudeVal.toFloat()
        ).apply()
    }


    fun getSavedLatValue(): Float {
        return preference.getFloat(LAT_KEY,0.0f)
    }


    fun with(application: Application) {
       val preferences = application.getSharedPreferences(PREFERENCES_FILE_NAME, Context.MODE_PRIVATE)
    }

    fun getSavedLanValue():Float?{
        return preference.getFloat(LAN_KEY,0.0f)
    }
    fun saveUserToken(token: String){
        preference.edit().putString(
            USER_TOKEN,
            token
        ).apply()
    }
    fun getUserToken():String?{
        return preference.getString(USER_TOKEN,null)
    }
    fun saveDate(date: String?){
        preference.edit().putString(PAST_DATE,date).apply()
    }
    fun getPastDate():String?{
        return preference.getString(PAST_DATE,null)
    }
    fun saveAddress(address:String?){
        preference.edit().putString(ADDRESS,address).apply()
    }
    fun getAddress():String?{
        return preference.getString(ADDRESS,null)
    }
    fun saveOutAddress(address_out: String?){
        preference.edit().putString(OUT_ADDRESS,address_out).apply()
    }
    fun getOutAddress():String?{
        return preference.getString(OUT_ADDRESS,null)
    }
    fun saveOutDate(out_date: String?){
        preference.edit().putString(OUT_DATE,out_date).apply()
    }
    fun getOutDate():String?{
        return preference.getString(OUT_DATE,null)
    }
    fun saveUserImage(image: String){
        preference.edit().putString(
            USER_IMAGE,
            image
        ).apply()
    }
    fun getUserImage():String?{
        return preference.getString(USER_IMAGE,null)
    }
    fun clearUserImage(){
        preference.edit().remove(USER_IMAGE).apply()

    }

    fun saveEmpCode(empCode: String) {
        preference.edit().putString(
            EMPLOYEE_CODE,
            empCode
        ).apply()
    }
    fun getEmployeeCode():String? {
        return preference.getString(EMPLOYEE_CODE,null)
    }

    fun saveUser(userName: String){
        preference.edit().putString(
            USER_NAME,
            userName
        ).apply()
    }
    fun getUserName():String?{
        return preference.getString(USER_NAME,null)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    fun clearUserToken() {
        val sharedPreferences:SharedPreferences = MarvyGroupApplication()
            .getInstance().getSharedPreferences(SG_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            sharedPreferences.edit().clear().commit()
    }

    fun clear(){
        preference.edit().clear().apply()
    }



}