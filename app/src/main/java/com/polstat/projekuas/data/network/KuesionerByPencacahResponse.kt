package com.polstat.projekuas.data.network

import com.google.gson.annotations.SerializedName

data class KuesionerByPencacahResponse(
    @SerializedName("data")
    val data: List<KuesionerItem>,
    @SerializedName("httpStatus")
    val httpStatus: String,
    @SerializedName("message")
    val message: String
)

data class KuesionerItem(
    @SerializedName("id")
    val id: Long,
    @SerializedName("pencacah")
    val pencacah: Pencacah,
    @SerializedName("status")
    val status: String
)

data class Pencacah(
    @SerializedName("id")
    val id: Int,
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

data class Role(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
