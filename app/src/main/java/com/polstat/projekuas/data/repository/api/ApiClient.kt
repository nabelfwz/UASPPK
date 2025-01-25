package com.polstat.projekuas.data.repository.api

import com.polstat.projekuas.data.network.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiClient(private val api: UserApi) {

    fun getUserProfile(onResult: (ProfileResponse?) -> Unit) {
        api.getUserProfile().enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                onResult(response.body())
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                onResult(null)
            }
        })
    }
}