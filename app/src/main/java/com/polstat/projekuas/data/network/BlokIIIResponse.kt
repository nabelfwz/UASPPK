package com.polstat.projekuas.data.network

import com.google.gson.annotations.SerializedName

data class BlokIIIResponse(
    @SerializedName("data")
    val data: BlokIIIData,

    @SerializedName("httpStatus")
    val httpStatus: String,

    @SerializedName("message")
    val message: String
)

data class BlokIIIData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("kepemilikanRumah")
    val kepemilikanRumah: String,

    @SerializedName("penggunaanListrik")
    val penggunaanListrik: String,

    @SerializedName("airMinumUtama")
    val airMinumUtama: String,

    @SerializedName("fasilitasBAB")
    val fasilitasBAB: String,

    @SerializedName("lantaiTerluas")
    val lantaiTerluas: String
)
