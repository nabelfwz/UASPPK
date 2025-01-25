package com.polstat.projekuas.data.network

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.gson.GsonBuilder
import com.polstat.projekuas.data.model.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun Context.saveAuthToken(token: String) {
    val sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        putString("auth_token", token)
        apply()
    }
}

fun Context.getSavedAuthToken(): String? {
    val sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    return sharedPref.getString("auth_token", null)
}

fun Context.clearAuthToken() {
    val sharedPref = getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    with(sharedPref.edit()) {
        remove("auth_token")
        apply()
    }
}

class Retro(private val context: Context) {
    fun getRetroClientInstance(): Retrofit {
        val gson = GsonBuilder().setLenient().create()
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val authToken = context.getSavedAuthToken()
                val builder = if (authToken != null) {
                    originalRequest.newBuilder()
                        .header("Authorization", "Bearer $authToken")
                } else {
                    originalRequest.newBuilder()
                }
                val newRequest = builder.build()
                chain.proceed(newRequest)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl("https://82d5-103-47-133-188.ngrok-free.app/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}