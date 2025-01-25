package com.polstat.projekuas.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.polstat.projekuas.data.network.KuesionerByPencacahResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun KuesionerPencacahByPengawasScreen(pencacahId:Long, navController: NavController) {
    var kuesionerResponse by remember { mutableStateOf<KuesionerByPencacahResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        isLoading = true
        getKuesionerPencacahByPengawas(
            pencacahId = pencacahId,
            context = context,
            onSuccess = { data ->
                isLoading = false
                kuesionerResponse = data
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    Column {
        MyTopAppBar(navController,"Kuesioner Pencacah")

        // UI yang menampilkan data atau pesan kesalahan
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> CircularProgressIndicator()
                kuesionerResponse != null -> KuesionerPengawasListView(kuesionerResponse!!, navController)
                else -> Text(text = "error")
            }
        }
    }
}

fun getKuesionerPencacahByPengawas(pencacahId: Long,context: Context, onSuccess: (KuesionerByPencacahResponse) -> Unit, onError: (String) -> Unit) {
    val retroInstance = Retro(context).getRetroClientInstance()
    val userApi = retroInstance.create(UserApi::class.java)

    userApi.getKuesionerPencacahByPengawas(pencacahId).enqueue(object : Callback<KuesionerByPencacahResponse> {
        override fun onResponse(call: Call<KuesionerByPencacahResponse>, response: Response<KuesionerByPencacahResponse>) {
            if (response.isSuccessful && response.body() != null) {
                onSuccess(response.body()!!)
            } else {
                onError("Response not successful: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<KuesionerByPencacahResponse>, t: Throwable) {
            onError("Error occurred: ${t.message}")
        }
    })
}