package com.polstat.projekuas.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserResponse {
    @SerializedName("username")
    @Expose
    val username: String? = null

    @SerializedName("accessToken")
    @Expose
    val accessToken: String? = null

    @SerializedName("roles")
    @Expose
    val roles: List<String>? = null
}