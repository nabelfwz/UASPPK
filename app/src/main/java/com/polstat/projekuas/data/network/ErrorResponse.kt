package com.polstat.projekuas.data.network

data class ErrorResponse(
    val message: String?,
    val throwable: String?,
    val httpStatus: String?
)