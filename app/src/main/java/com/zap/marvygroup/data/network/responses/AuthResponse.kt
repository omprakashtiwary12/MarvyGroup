package com.zap.marvygroup.data.network.responses

import com.zap.marvygroup.data.db.entities.*
import com.zap.marvygroup.ui.attendance.Address

data class AuthResponse(
    val isSuccessful: Boolean?,
    val message: String?,
    val user: User?

)

data class ApplyFormResponse(
    val isSuccessful: Boolean?,
    val message: String?,
    val applyForm: GuestUser
)

data class ResetResponse(
    val isSuccessful: Boolean?,
    val message: String?,
    val resetUser: ResetUser
)

data class ProfileResponse(
    val isSuccessful: Boolean,
    val message: String,
    val profile: Profile
)

data class ContactUserResponse(
    val isSuccessful: Boolean?,
    val message: String?,
    val contactUser: ContactUser?
)

data class SalaryResponse(
    val isSuccessful: Boolean,
    val message: String,
    val listOfYear: List<String>
)

data class DocumentResponse(
    val isSuccessful: Boolean,
    val message: String,
    val file_type:String,
    val datetime:String,
    val uploaded_file: String
)
data class ChangeImageResponse(
    val isSuccessful: Boolean,
    val message: String,
    val uploaded_file: String
)
data class AttendanceResponse(
    val isSuccessful: Boolean,
    val message: String,
    val address: Address
)
data class UploadDocResponse(
    val isSuccessful: Boolean,
    val message: String
   // val uploadedDoc: List<DocData>
)


