package com.polstat.projekuas.data.network

import com.google.gson.annotations.SerializedName
data class BlokIVResponse(
    @SerializedName("data")
    val data: AnggotaKeluargaData,

    @SerializedName("httpStatus")
    val httpStatus: String,

    @SerializedName("message")
    val message: String
)

data class AnggotaKeluargaData(
    @SerializedName("id")
    val id: Int,

    @SerializedName("namaLengkap")
    val namaLengkap: String,

    @SerializedName("jenisKelamin")
    val jenisKelamin: String,

    @SerializedName("alamatSesuaiKK")
    val alamatSesuaiKK: String,

    @SerializedName("tempatLahirProvinsi")
    val tempatLahirProvinsi: String,

    @SerializedName("tempatLahirkabKota")
    val tempatLahirkabKota: String,

    @SerializedName("tglLahirSesuaiKK")
    val tglLahirSesuaiKK: String,

    @SerializedName("tglLahir")
    val tglLahir: String,

    @SerializedName("punyaAktaLahir")
    val punyaAktaLahir: String,

    @SerializedName("tidakPunyaAkta")
    val tidakPunyaAkta: String,

    @SerializedName("kewarganegaraan")
    val kewarganegaraan: String,

    @SerializedName("agama")
    val agama: String,

    @SerializedName("statusPerkawinan")
    val statusPerkawinan: String,

    @SerializedName("hubunganKK")
    val hubunganKK: String,

    @SerializedName("lamaTinggalTahun")
    val lamaTinggalTahun: Int,

    @SerializedName("lamaTinggalBulan")
    val lamaTinggalBulan: Int,

    @SerializedName("ijazahTertinggi")
    val ijazahTertinggi: String,

    @SerializedName("nik")
    val nik: String
)
