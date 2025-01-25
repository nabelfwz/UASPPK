package com.polstat.projekuas.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CreateKuesionerResponse(
    @SerializedName("data")
    @Expose
    val data: Long,

    @SerializedName("httpStatus")
    @Expose
    val httpStatus: String,

    @SerializedName("message")
    @Expose
    val message: String
)