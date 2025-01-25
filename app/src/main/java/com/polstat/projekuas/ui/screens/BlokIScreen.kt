package com.polstat.projekuas.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.polstat.projekuas.data.network.BlokIResponse
import com.polstat.projekuas.data.network.CreateKuesionerResponse
import com.polstat.projekuas.data.network.ErrorResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlokIScreen(kuesId: Long, navController: NavController, isEditMode: Int) {
    var blokIData by remember { mutableStateOf<BlokIResponse?>(null) }
    var id by remember { mutableStateOf<Long?>(null) }
    var kodeProvinsi by remember { mutableStateOf("") }
    var kodeKabupatenKota by remember { mutableStateOf("") }
    var kodeKecamatan by remember { mutableStateOf("") }
    var kodeDesaKelurahan by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var jmlAnggotaKeluarga by remember { mutableStateOf("") }
    var namaSLS by remember { mutableStateOf("") }
    var noBangunan by remember { mutableStateOf("") }
    var namaKK by remember { mutableStateOf("") }
    var noHpKK by remember { mutableStateOf("") }
    var noUrutKeluarga by remember { mutableStateOf("") }
    val context = LocalContext.current

    // You can get kuesionerId from your navigation argument or any other source
    val kuesionerId: Long = kuesId // Replace with your actual value

    var errorSnackbarMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Log.d("MyTag", isEditMode.toString())
    LaunchedEffect(key1 = true) {
        isLoading = true
        if (isEditMode == 1) {
            try {
                // Panggil fungsi untuk mendapatkan data Blok I
                blokIData = getBlokIData(kuesionerId, context)

                id = blokIData?.data?.id?.toLong()
                kodeProvinsi = blokIData?.data?.kodeProvinsi.orEmpty()
                kodeKabupatenKota = blokIData?.data?.kodeKabupatenKota.orEmpty()
                kodeKecamatan = blokIData?.data?.kodeKecamatan.orEmpty()
                kodeDesaKelurahan = blokIData?.data?.kodeDesaKelurahan.orEmpty()
                alamat = blokIData?.data?.alamat.orEmpty()
                jmlAnggotaKeluarga = blokIData?.data?.jmlAnggotaKeluarga.orEmpty()
                namaSLS = blokIData?.data?.namaSLS.orEmpty()
                noBangunan = blokIData?.data?.noBangunan.orEmpty()
                namaKK = blokIData?.data?.namaKK.orEmpty()
                noHpKK = blokIData?.data?.noHpKK.orEmpty()
                noUrutKeluarga = blokIData?.data?.noUrutKeluarga.orEmpty()
            } catch (e: Exception) {
                // Handle error, misalnya dengan menampilkan pesan kesalahan
                errorSnackbarMessage = e.message ?: "Error occurred"
            } finally {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isEditMode == 1) {
            MyTopAppBar(navController,"Edit Blok I")
        } else {
            TopAppBar(
                title = { Text("Blok I") }
            )
        }
        // Snackbar di bagian atas Column
        errorSnackbarMessage?.let { message ->
            Snackbar(
                containerColor = Color.Red,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color.Red),
                action = {
                    // Aksi untuk Snackbar
                }
            ) {
                Text(text = message, color = Color.White)
            }
        }

        // Content utama termasuk TextField
        LazyColumn(
            modifier = Modifier
//                .weight(1f)  // Menempatkan LazyColumn di sisa ruang yang tersedia
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()  // Tambahkan fillMaxWidth agar dapat diatur secara penuh lebar
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Isi dari LazyColumn
                    TextField(
                        value = kodeProvinsi,
                        onValueChange = { kodeProvinsi = it },
                        label = { Text("Kode Provinsi") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = kodeKabupatenKota,
                        onValueChange = { kodeKabupatenKota = it },
                        label = { Text("Kode Kabupaten/Kota") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = kodeKecamatan,
                        onValueChange = { kodeKecamatan = it },
                        label = { Text("Kode Kecamatan") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = kodeDesaKelurahan,
                        onValueChange = { kodeDesaKelurahan = it },
                        label = { Text("Kode Desa/Kelurahan") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = alamat,
                        onValueChange = { alamat = it },
                        label = { Text("Alamat") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = jmlAnggotaKeluarga,
                        onValueChange = { jmlAnggotaKeluarga = it },
                        label = { Text("Jumlah Anggota Keluarga") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = namaSLS,
                        onValueChange = { namaSLS = it },
                        label = { Text("Nama SLS") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = noBangunan,
                        onValueChange = { noBangunan = it },
                        label = { Text("Nomor Bangunan") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = namaKK,
                        onValueChange = { namaKK = it },
                        label = { Text("Nama Kepala Keluarga") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = noHpKK,
                        onValueChange = { noHpKK = it },
                        label = { Text("Nomor HP Kepala Keluarga") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    TextField(
                        value = noUrutKeluarga,
                        onValueChange = { noUrutKeluarga = it },
                        label = { Text("Nomor Urut Keluarga") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    // Button to submit the form
                    Button(
                        onClick = {
                            if (isEditMode == 0) {
                                postBlokI(
                                    kodeProvinsi,
                                    kodeKabupatenKota,
                                    kodeKecamatan,
                                    kodeDesaKelurahan,
                                    alamat,
                                    jmlAnggotaKeluarga,
                                    namaSLS,
                                    noBangunan,
                                    namaKK,
                                    noHpKK,
                                    noUrutKeluarga,
                                    kuesionerId,
                                    context,
                                    { errorMessage -> errorSnackbarMessage = errorMessage },
                                    navController
                                )
                            } else {
                                putBlokI(
                                    id!!,
                                    kodeProvinsi,
                                    kodeKabupatenKota,
                                    kodeKecamatan,
                                    kodeDesaKelurahan,
                                    alamat,
                                    jmlAnggotaKeluarga,
                                    namaSLS,
                                    noBangunan,
                                    namaKK,
                                    noHpKK,
                                    noUrutKeluarga,
                                    kuesionerId,
                                    context,
                                    { errorMessage -> errorSnackbarMessage = errorMessage },
                                    navController
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        if (isEditMode == 0) {
                            Text("Submit")
                        } else {
                            Text("Ubah")
                        }
                    }
                }
            }
        }
    }
}

fun postBlokI(
    kodeProvinsi: String,
    kodeKabupatenKota: String,
    kodeKecamatan: String,
    kodeDesaKelurahan: String,
    alamat: String,
    jmlAnggotaKeluarga: String,
    namaSLS: String,
    noBangunan: String,
    namaKK: String,
    noHpKK: String,
    noUrutKeluarga: String,
    kuesionerId: Long,
    context: Context,
    errorCallback: (String) -> Unit,
    navController: NavController
) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)

    val call = retro.postBlokI(kodeProvinsi, kodeKabupatenKota, kodeKecamatan, kodeDesaKelurahan, alamat, jmlAnggotaKeluarga, namaSLS, noBangunan, namaKK, noHpKK, noUrutKeluarga, kuesionerId,)

    call.enqueue(object : Callback<BlokIResponse>{
        override fun onResponse(call: Call<BlokIResponse>, response: Response<BlokIResponse>) {
            if (response.isSuccessful) {
                navController.navigate(
                    Screens.BlokIII.route
                        .replace("{kuesionerId}", "$kuesionerId")
                        .replace("{isEdit}", "0")
                )
            } else if (response.code() == 400) {
                // Handle 400 Bad Request response
                try {
                    val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    Log.e("MyTag", "Error Message: ${errorResponse?.message}")
                    errorCallback.invoke(errorResponse?.message ?: "${errorResponse?.message}")
                } catch (e: JsonSyntaxException) {
                    Log.e("MyTag", "Error parsing JSON: ${response.errorBody()?.string()}")
                }
            } else {
                Log.e("MyTag", "Unsuccessful response: ${response.code()}, ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<BlokIResponse>, t: Throwable) {
            Log.e("MyTag", "Error occurred", t)
        }
    })
}

fun putBlokI(
    id: Long,
    kodeProvinsi: String,
    kodeKabupatenKota: String,
    kodeKecamatan: String,
    kodeDesaKelurahan: String,
    alamat: String,
    jmlAnggotaKeluarga: String,
    namaSLS: String,
    noBangunan: String,
    namaKK: String,
    noHpKK: String,
    noUrutKeluarga: String,
    kuesionerId: Long,
    context: Context,
    errorCallback: (String) -> Unit,
    navController: NavController
) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)

    val call = retro.putBlokI(
        id,
        kodeProvinsi,
        kodeKabupatenKota,
        kodeKecamatan,
        kodeDesaKelurahan,
        alamat,
        jmlAnggotaKeluarga,
        namaSLS,
        noBangunan,
        namaKK,
        noHpKK,
        noUrutKeluarga,
        kuesionerId,
    )

    call.enqueue(object : Callback<BlokIResponse>{
        override fun onResponse(call: Call<BlokIResponse>, response: Response<BlokIResponse>) {
            if (response.isSuccessful) {
                Log.d("MyTag", "Message: ${response.body()?.message}")
                Log.d("MyTag", "Data: ${response.body()?.data}")
                Log.d("MyTag", "Status: ${response.body()?.httpStatus}")
                Toast.makeText(context, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
            } else if (response.code() == 400) {
                // Handle 400 Bad Request response
                try {
                    val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    Log.e("MyTag", "Error Message: ${errorResponse?.message}")
                    errorCallback.invoke(errorResponse?.message ?: "${errorResponse?.message}")
                } catch (e: JsonSyntaxException) {
                    Log.e("MyTag", "Error parsing JSON: ${response.errorBody()?.string()}")
                }
            } else {
                Log.e("MyTag", "Unsuccessful response: ${response.code()}, ${response.errorBody()?.string()}")
            }
        }

        override fun onFailure(call: Call<BlokIResponse>, t: Throwable) {
            Log.e("MyTag", "Error occurred", t)
        }
    })
}