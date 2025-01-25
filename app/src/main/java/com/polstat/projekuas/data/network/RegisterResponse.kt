package com.polstat.projekuas.data.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("namaPencacah") val namaPencacah: String,
    @SerializedName("namaPengawas") val namaPengawas: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("authorities") val authorities: List<Authority>? = null,
    @SerializedName("enabled") val enabled: Boolean,
    @SerializedName("credentialsNonExpired") val credentialsNonExpired: Boolean,
    @SerializedName("accountNonExpired") val accountNonExpired: Boolean,
    @SerializedName("accountNonLocked") val accountNonLocked: Boolean
)

class RegisterRequest {
    @SerializedName("email")
    @Expose
    var email: String? = null

    @SerializedName("namaPencacah")
    @Expose
    var namaPencacah: String? = null

    @SerializedName("namaPengawas")
    @Expose
    var namaPengawas: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null

    @SerializedName("role")
    @Expose
    var role: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null
}

data class Authority(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String
)
