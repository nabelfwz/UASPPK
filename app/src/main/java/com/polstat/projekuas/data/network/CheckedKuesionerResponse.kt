package com.polstat.projekuas.data.network

import com.google.gson.annotations.SerializedName

data class CheckedKuesionerResponse (
    @SerializedName("httpStatus")
    val httpStatus: String,

    @SerializedName("message")
    val message: String
)