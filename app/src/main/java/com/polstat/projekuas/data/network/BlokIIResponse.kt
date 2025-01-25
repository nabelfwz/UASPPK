package com.polstat.projekuas.data.network

import com.google.gson.annotations.SerializedName
import java.util.Date

data class BlokIIResponse(
    @SerializedName("data")
    val data: BlokIIData,
    @SerializedName("httpStatus")
    val httpStatus: String,
    @SerializedName("message")
    val message: String
)

data class BlokIIData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("namaPencacah")
    val namaPencacah: String,
    @SerializedName("namaPengawas")
    val namaPengawas: String,
    @SerializedName("createdOn")
    val createdOn: Date,
    @SerializedName("checkedOn")
    val checkedOn: Date? // Menggunakan tipe Date yang dapat nullable
)
