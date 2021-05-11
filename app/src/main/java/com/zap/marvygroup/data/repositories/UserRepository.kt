package com.zap.marvygroup.data.repositories

import android.provider.DocumentsContract
import android.webkit.MimeTypeMap
import com.zap.marvygroup.data.db.AppDatabase
import com.zap.marvygroup.data.db.entities.GuestUser
import com.zap.marvygroup.data.db.entities.User
import com.zap.marvygroup.data.network.MyApi
import com.zap.marvygroup.data.network.SafeApiRequest
import com.zap.marvygroup.data.network.responses.*
import com.zap.marvygroup.data.preferences.PreferenceProvider
import com.zap.marvygroup.ui.documents_type.DocListResponse
import com.zap.marvygroup.ui.documents_type.DocTypeResponse
import com.zap.marvygroup.ui.uploaded_documents.DocResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class UserRepository(
    private val api: MyApi,
    private val db: AppDatabase,
    private val prefs: PreferenceProvider

) : SafeApiRequest(){

    suspend fun userLogin(username: String, password: String): AuthResponse {
        return apiRequest { api.userLogin(username, password)
        }
    }

    suspend fun resetPassword(username: String):AuthResponse{
        return apiRequest { api.resetPasswod(username) }
    }
    suspend fun addContactInfo(name: String, email: String, subject: String, message: String): ContactUserResponse {
        return apiRequest { api.addContact(name, email, subject, message) }
    }
    suspend fun getProfileDetails(employee_code: String, access_token: String): ProfileResponse {
       return apiRequest {
           api.getUserDetails(employee_code, access_token)
       }
    }
    suspend fun getSalary(employee_code: String, access_token: String): SalaryResponse {
        return apiRequest { api.getSalary(employee_code, access_token)
        }
    }
    suspend fun getDocumentType(): DocTypeResponse {
        return apiRequest { api.getDocType() }
    }
    suspend fun addAttendance(
        employeeCode: String,
        access_token: String,
        latitude: Double,
        longitude: Double,
        is_entry: Boolean
    ): AttendanceResponse {
        return apiRequest { api.addAttendance(
            employeeCode,
            access_token,
            latitude,
            longitude,
            is_entry
        ) }
    }
    suspend fun addUploadDoc(employeeCode: String): DocResponse {
        return apiRequest { api.addUploadDoc(employeeCode) }
    }
    suspend fun showPdfUploadDoc(employee_code: String):DocResponse {
        return apiRequest { api.showPdfDoc(employee_code) }
    }
    suspend fun showDocListApi(employee_code: String): DocListResponse {
        return apiRequest { api.showDocList(employee_code) }
    }

    suspend fun attachDocFile(
        employee_code: String,
        access_token: String,
        fileType: String,
        myFile: File
    ):DocumentResponse{
        val file = File(myFile.path)
            val content_type = getMimeType(file.getPath())
            val mFile = RequestBody.create(MediaType.parse(content_type), file)
            val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
            val employee_code = RequestBody.create(MediaType.parse("text/plain"), employee_code)
            val access_token = RequestBody.create(MediaType.parse("text/plain"), access_token)
            val fileType = RequestBody.create(MediaType.parse("text/plain"), fileType)

            return apiRequest {
                api.attachDocFile(
                    employee_code,
                    access_token,
                    fileType,
                    fileToUpload
                )
            }

    }
    suspend fun changeProfileImage(employee_code: String, access_token: String, imageProfile: File):ChangeImageResponse {
        val file = File(imageProfile.path)
        val content_type = getMimeType(file.getPath())
        val mFile = RequestBody.create(MediaType.parse(content_type), file)
        val fileToUpload = MultipartBody.Part.createFormData("file", file.name, mFile)
        val employee_code =  RequestBody.create(MediaType.parse("text/plain"), employee_code)
        val access_token=  RequestBody.create(MediaType.parse("text/plain"), access_token)
        return apiRequest {api.changeProfileImage(employee_code, access_token, fileToUpload)}
    }
    suspend fun applyForm(
        name: String,
        mobile: String,
        email: String,
        subject: String,
        message: String,
        resume: File
    ): ApplyFormResponse {
        val file = File(resume.toURI())
        val content_type = getMimeType(file.getPath())
        val mFile = RequestBody.create(MediaType.parse(content_type), file)
        val fileToUpload = MultipartBody.Part.createFormData("resume", file.name, mFile)
        val name =  RequestBody.create(MediaType.parse("text/plain"), name)
        val mobile=  RequestBody.create(MediaType.parse("text/plain"), mobile)
        val email= RequestBody.create(MediaType.parse("text/plain"), email)
        val subject= RequestBody.create(MediaType.parse("text/plain"), subject)
        val message= RequestBody.create(MediaType.parse("text/plain"), message)
        return apiRequest { api.applyForm(name, mobile, email, subject, message, fileToUpload) } }

//    fun getMimeType(path: String): String {
//        val extension = MimeTypeMap.getFileExtensionFromUrl(path)
//        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)!!
//    }
    fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
         return type
        }else{
            throw IllegalAccessException("No such file exists")
        }

    }


    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getUser()

     suspend fun saveToken(token: String){
         prefs.saveUserToken(token)
     }
    suspend fun saveUserImage(image: String){
        prefs.saveUserImage(image)
    }

    suspend fun saveUserName(userName: String){
        prefs.saveUser(userName)
    }

    suspend fun saveEcode(empCode: String){
        prefs.saveEmpCode(empCode)
    }
    suspend fun saveBankName(bankName: String?): String? {
        return bankName
    }

    fun getPdf() = GuestUser().resume
}