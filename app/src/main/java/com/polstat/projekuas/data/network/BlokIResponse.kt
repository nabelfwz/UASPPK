package com.polstat.projekuas.data.network

import com.google.gson.annotations.SerializedName

data class BlokIResponse(
    @SerializedName("data")
    val data: BlokIData,

    @SerializedName("httpStatus")
    val httpStatus: String,

    @SerializedName("message")
    val message: String
)

data class BlokIData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("alamat")
    val alamat: String,

    @SerializedName("kodeDesaKelurahan")
    val kodeDesaKelurahan: String,

    @SerializedName("kodeKabupatenKota")
    val kodeKabupatenKota: String,

    @SerializedName("kodeKecamatan")
    val kodeKecamatan: String,

    @SerializedName("kodeProvinsi")
    val kodeProvinsi: String,

    @SerializedName("jmlAnggotaKeluarga")
    val jmlAnggotaKeluarga: String,

    @SerializedName("namaKK")
    val namaKK: String,

    @SerializedName("namaSLS")
    val namaSLS: String,

    @SerializedName("noBangunan")
    val noBangunan: String,

    @SerializedName("noHpKK")
    val noHpKK: String,

    @SerializedName("noUrutKeluarga")
    val noUrutKeluarga: String
)
