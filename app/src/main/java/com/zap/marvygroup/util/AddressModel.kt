package com.zap.marvygroup.util

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddressModel : Serializable {
    @SerializedName("id")
    var id: Int? = null
    @SerializedName("user_id")
    var userId: Int? = null
    @SerializedName("company_id")
    var companyId: Int? = null
    @SerializedName("address_type")
    var doctypeList: String? = null
    @SerializedName("title")
    var title: String? = null
    @SerializedName("landmark")
    var landmark: String? = null
    @SerializedName("flat_no")
    var flatNumber: String? = null
    @SerializedName("street")
    var street: String? = null
    @SerializedName("city")
    var city: String? = null
    @SerializedName("state")
    var state: String? = null
    @SerializedName("county")
    var county: String? = null
    @SerializedName("zipcode")
    var zipcode: String? = null
    @SerializedName("latitude")
    var latitude: String? = null
    @SerializedName("longitude")
    var longitude: String? = null
}