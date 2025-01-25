package com.polstat.projekuas.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.polstat.projekuas.data.network.KuesionerByPencacahResponse
import com.polstat.projekuas.data.network.KuesionerItem
import com.polstat.projekuas.data.network.PencacahByPengawasItem
import com.polstat.projekuas.data.network.PencacahByPengawasResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PencacahByPengawasScreen(navController: NavController) {
    var pencacahByPengawasResponse by remember { mutableStateOf<PencacahByPengawasResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        isLoading = true
        getPencacahByPengawas(
            context = context,
            onSuccess = { data ->
                isLoading = false
                pencacahByPengawasResponse = data
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    Column {
        MyTopAppBar(navController,"Daftar Pencacah")

        // UI yang menampilkan data atau pesan kesalahan
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> CircularProgressIndicator()
                pencacahByPengawasResponse != null -> PencacahListView(pencacahByPengawasResponse!!, navController)
                else -> Text(text = "error")
            }
        }
    }
}

@Composable
fun PencacahListView(response: PencacahByPengawasResponse, navController: NavController) {
    // Buat UI yang menampilkan daftar kuesioner
//    var blokIIResponse by remember { mutableStateOf<BlokIIResponse?>(null) }
//    var context = LocalContext.current

    LazyColumn {
        items(response.data) { pencacahByPengawasItem ->
//            blokIIResponse = getBlokIIData(kuesionerItem.id, context)
            PencacahItemView(pencacahByPengawasItem,navController)
        }
    }
}

@Composable
fun PencacahItemView(pencacahByPengawasItem: PencacahByPengawasItem, navController: NavController) {

    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "${pencacahByPengawasItem.namaPencacah}", fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
            Text(text =  "Email : ${pencacahByPengawasItem.email}")

            // Tambahkan lebih banyak UI sesuai kebutuhan

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = {
                        navController.navigate(
                            Screens.KuesionerPencacahByPengawas.route
                                .replace("{pencacahId}", "${pencacahByPengawasItem.id}")
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp),

                ) {
                    Text(text = "Kuesioner",fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

fun getPencacahByPengawas(context: Context, onSuccess: (PencacahByPengawasResponse) -> Unit, onError: (String) -> Unit) {
    val retroInstance = Retro(context).getRetroClientInstance()
    val userApi = retroInstance.create(UserApi::class.java)

    userApi.getPencacahByPengawas().enqueue(object : Callback<PencacahByPengawasResponse> {
        override fun onResponse(call: Call<PencacahByPengawasResponse>, response: Response<PencacahByPengawasResponse>) {
            if (response.isSuccessful && response.body() != null) {
                onSuccess(response.body()!!)
            } else {
                onError("Response not successful: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<PencacahByPengawasResponse>, t: Throwable) {
            onError("Error occurred: ${t.message}")
        }
    })
}