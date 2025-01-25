package com.polstat.projekuas.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.polstat.projekuas.data.network.BlokIIResponse
import com.polstat.projekuas.data.network.KuesionerByPencacahResponse
import com.polstat.projekuas.data.network.KuesionerItem
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuesionerPencacahScreen(navController: NavController) {
    var kuesionerResponse by remember { mutableStateOf<KuesionerByPencacahResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        isLoading = true
        getKuesionerByPencacah(context = context,
            onSuccess = { data -> isLoading = false
                kuesionerResponse = data
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    Column {
        MyTopAppBar(navController,"Riwayat Kuesioner")

        // UI yang menampilkan data atau pesan kesalahan
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                isLoading -> CircularProgressIndicator()
                kuesionerResponse != null -> KuesionerListView(kuesionerResponse!!, navController)
                else -> Text(text = "error")
            }
        }
    }
}

@Composable
fun KuesionerListView(response: KuesionerByPencacahResponse, navController: NavController) {
    LazyColumn {
        items(response.data) { kuesionerItem ->
            KuesionerItemView(kuesionerItem,navController)
        }
    }
}

@Composable
fun KuesionerItemView(kuesionerItem: KuesionerItem,navController: NavController) {
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
            Text(text = "Kuesioner ID: ${kuesionerItem.id}", fontWeight = FontWeight.Bold, fontSize = 22.sp, modifier = Modifier.padding(bottom = 8.dp))
            val boldText = AnnotatedString.Builder().apply {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("${kuesionerItem.status}")
                }
            }.toAnnotatedString()

            Text(text = buildAnnotatedString {
                append("Status: ")
                append(boldText)
            })

            // Tambahkan lebih banyak UI sesuai kebutuhan

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Button(
                    onClick = { navController.navigate(Screens.DetailKuesioner.route.replace("{kuesionerId}", "${kuesionerItem.id}")) },
                    modifier = Modifier
                        .weight(1f)
                        .height(30.dp),
                ) {
                    Text(text = "Lihat",fontSize = 13.sp)
                }
                Spacer(modifier = Modifier.width(8.dp))
                if (kuesionerItem.status != "diterima") {
                    Button(
                        onClick = { navController.navigate(Screens.EditKuesioner.route.replace("{kuesionerId}", "${kuesionerItem.id}")) },
                        modifier = Modifier
                            .weight(1f)
                            .height(30.dp)
                    ) {
                        Text(text = "Edit",fontSize = 13.sp)
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(navController: NavController? = null, title: String = "Title") {
    TopAppBar(
        navigationIcon = {
            if (navController != null) {
                // Jika NavController disediakan, tampilkan tombol kembali
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        title = { Text(title) }
    )
}

fun getKuesionerByPencacah(context: Context, onSuccess: (KuesionerByPencacahResponse) -> Unit, onError: (String) -> Unit) {
    val retroInstance = Retro(context).getRetroClientInstance()
    val userApi = retroInstance.create(UserApi::class.java)

    userApi.getKuesionerByPencacah().enqueue(object : Callback<KuesionerByPencacahResponse> {
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
