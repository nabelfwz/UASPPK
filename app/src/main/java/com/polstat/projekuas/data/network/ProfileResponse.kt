package com.polstat.projekuas.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("data")
    @Expose
    val data: UserData,

    @SerializedName("httpStatus")
    @Expose
    val httpStatus: String,

    @SerializedName("message")
    @Expose
    val message: String
)

data class EditPasswordResponse(
    @SerializedName("httpStatus")
    @Expose
    val httpStatus: String,

    @SerializedName("message")
    @Expose
    val message: String
)

class ProfileRequest {
    @SerializedName("namaPencacah")
    @Expose
    var namaPencacah: String? = null

    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null
}

data class UserData(
    @SerializedName("id")
    @Expose
    val id: Long,

    @SerializedName("namaPencacah")
    @Expose
    var namaPencacah: String,

    @SerializedName("namaPengawas")
    @Expose
    var namaPengawas: String,

    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("email")
    @Expose
    var email: String
)