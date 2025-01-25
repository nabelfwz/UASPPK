package com.polstat.projekuas.data.network

import com.google.gson.annotations.SerializedName

data class PencacahByPengawasResponse(
    @SerializedName("data")
    val data: List<PencacahByPengawasItem>,

    @SerializedName("httpStatus")
    val httpStatus: String,

    @SerializedName("message")
    val message: String
)
data class PencacahByPengawasItem(
    @SerializedName("id")
    val id: Long,

    @SerializedName("namaPencacah")
    val namaPencacah: String,

    @SerializedName("namaPengawas")
    val namaPengawas: String,

    @SerializedName("username")
    val username: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("roles")
    val roles: List<Role>
)
