package com.polstat.projekuas.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.polstat.projekuas.data.network.BlokIIIResponse
import com.polstat.projekuas.data.network.BlokIResponse
import com.polstat.projekuas.data.network.ErrorResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlokIIIScreen(kuesId: Long, navController: NavController, isEditMode: Int) {
    var blokIIIData by remember { mutableStateOf<BlokIIIResponse?>(null) }

    val context = LocalContext.current
    var id by remember { mutableStateOf<Long?>(null) }
    // rumah
    val statusRumah = arrayOf("Milik sendiri", "Kontrak/sewa", "Bebas sewa (menumpang)", "Dinas", "Lainnya")
    var expandedstatusRumah by remember { mutableStateOf(false) }
    var kepemilikanRumah by remember { mutableStateOf(statusRumah[0]) }

    // listrik
    val optionListrik = arrayOf("Ya, PLN dengan daya lebih dari 900 watt", "Ya, PLN dengan daya 450 watt atau 900 watt", "Ya, PLN tanpa meteran", "Ya, Listrik non PLN", "Tidak menggunakan listrik")
    var expandedoptionListrik by remember { mutableStateOf(false) }
    var penggunaanListrik by remember { mutableStateOf(optionListrik[0]) }

    // air minum
    val optionAirMinum = arrayOf("Air kemasan/air isi ulang", "Leding", "Pompa/sumur bor", "Sumur", "Mata air", "Sungai/danau", "Air hujan", "Lainnya")
    var expandedOptionAirMinum by remember { mutableStateOf(false) }
    var airMinumUtama by remember { mutableStateOf(optionAirMinum[0]) }

    // septic tank
    val optionSepticTank = arrayOf("Ya, dengan septic tank", "Ya, tanpa septic tank", "Tidak")
    var expandedOptionSepticTank by remember { mutableStateOf(false) }
    var fasilitasBAB by remember { mutableStateOf(optionSepticTank[0]) }

    // lantai
    var optionLantaiTerluas = arrayOf("Keramik/marmer/granit", "Ubin/tegel/teraso", "Semen", "Kayu/papan", "Bambu", "Tanah", "Lainnya")
    var expandedOptionLantaiTerluas by remember { mutableStateOf(false) }
    var lantaiTerluas by remember { mutableStateOf(optionLantaiTerluas[0]) }

    var errorSnackbarMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Log.d("MyTag", isEditMode.toString())
    LaunchedEffect(key1 = true) {
        isLoading = true
        if (isEditMode == 1) {
            try {
                // Panggil fungsi untuk mendapatkan data Blok I
                blokIIIData = getBlokIIIData(kuesId, context)

                id = blokIIIData?.data?.id?.toLong()
                kepemilikanRumah = statusRumah[statusRumah.indexOf(blokIIIData?.data?.kepemilikanRumah)]
                penggunaanListrik = optionListrik[optionListrik.indexOf(blokIIIData?.data?.penggunaanListrik)]
                airMinumUtama = optionAirMinum[optionAirMinum.indexOf(blokIIIData?.data?.airMinumUtama)]
                fasilitasBAB = optionSepticTank[optionSepticTank.indexOf(blokIIIData?.data?.fasilitasBAB)]
                lantaiTerluas = optionLantaiTerluas[optionLantaiTerluas.indexOf(blokIIIData?.data?.lantaiTerluas)]
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
            .fillMaxSize(),
    ) {
        if (isEditMode == 1) {
            MyTopAppBar(navController,"Edit Blok II")
        } else {
            TopAppBar(
                title = { Text("Blok II") }
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
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedstatusRumah,
                        onExpandedChange = {
                            expandedstatusRumah = it
                            if (it) {
                                expandedoptionListrik = false
                                expandedOptionAirMinum = false
                                expandedOptionSepticTank = false
                                expandedOptionLantaiTerluas = false
                            }
                        }
                    ) {
                        TextField(
                            value = kepemilikanRumah,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status kepemilikan rumah yang ditempati") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedstatusRumah) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedstatusRumah,
                            onDismissRequest = { expandedstatusRumah = false }
                        ) {
                            statusRumah.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        kepemilikanRumah = item
                                        expandedstatusRumah = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedoptionListrik,
                        onExpandedChange = {
                            expandedoptionListrik = it
                            if (it) {
                                expandedstatusRumah = false
                                expandedOptionAirMinum = false
                                expandedOptionSepticTank = false
                            }
                        }
                    ) {
                        TextField(
                            value = penggunaanListrik,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Apakah menggunakan listrik?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedoptionListrik) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedoptionListrik,
                            onDismissRequest = { expandedoptionListrik = false }
                        ) {
                            optionListrik.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        penggunaanListrik = item
                                        expandedoptionListrik = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedOptionAirMinum,
                        onExpandedChange = {
                            expandedOptionAirMinum = it
                            if (it) {
                                expandedstatusRumah = false
                                expandedoptionListrik = false
                                expandedOptionSepticTank = false
                                expandedOptionLantaiTerluas = false
                            }
                        }
                    ) {
                        TextField(
                            value = airMinumUtama,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Apa sumber air minum utama yang digunakan keluarga ini?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOptionAirMinum) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOptionAirMinum,
                            onDismissRequest = { expandedOptionAirMinum = false }
                        ) {
                            optionAirMinum.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        airMinumUtama = item
                                        expandedOptionAirMinum = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedOptionSepticTank,
                        onExpandedChange = {
                            expandedOptionSepticTank = it
                            if (it) {
                                expandedstatusRumah = false
                                expandedoptionListrik = false
                                expandedOptionAirMinum = false
                                expandedOptionLantaiTerluas = false
                            }
                        }
                    ) {
                        TextField(
                            value = fasilitasBAB,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Apakah di rumah ini ada fasilitas jamban/tempat buang air besar dan septic tank?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOptionSepticTank) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOptionSepticTank,
                            onDismissRequest = { expandedOptionSepticTank = false }
                        ) {
                            optionSepticTank.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        fasilitasBAB = item
                                        expandedOptionSepticTank = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedOptionLantaiTerluas,
                        onExpandedChange = {
                            expandedOptionLantaiTerluas = it
                            if (it) {
                                expandedstatusRumah = false
                                expandedoptionListrik = false
                                expandedOptionAirMinum = false
                                expandedOptionSepticTank = false
                            }
                        }
                    ) {
                        TextField(
                            value = lantaiTerluas,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Apakah jenis lantai terluas rumah ini?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOptionLantaiTerluas) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOptionLantaiTerluas,
                            onDismissRequest = { expandedOptionLantaiTerluas = false }
                        ) {
                            optionLantaiTerluas.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        lantaiTerluas = item
                                        expandedOptionLantaiTerluas = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    }
                                )
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (isEditMode == 0) {
                                postBlokIII(
                                    kepemilikanRumah,
                                    penggunaanListrik,
                                    airMinumUtama,
                                    fasilitasBAB,
                                    lantaiTerluas,
                                    kuesId,
                                    context,
                                    { errorMessage -> errorSnackbarMessage = errorMessage },
                                    navController,
                                )
                            } else {
                                putBlokIII(
                                    id!!,
                                    kepemilikanRumah,
                                    penggunaanListrik,
                                    airMinumUtama,
                                    fasilitasBAB,
                                    lantaiTerluas,
                                    context,
                                    { errorMessage -> errorSnackbarMessage = errorMessage },
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text("Submit")
                    }
                }
            }
        }
    }
}

fun postBlokIII(
    kepemilikanRumah: String,
    penggunaanListrik: String,
    airMinumUtama: String,
    fasilitasBAB: String,
    lantaiTerluas: String,
    kuesionerId: Long,
    context: Context,
    errorCallback: (String) -> Unit,
    navController: NavController
) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)

    val call = retro.postBlokIII(kepemilikanRumah, penggunaanListrik, airMinumUtama, fasilitasBAB, lantaiTerluas, kuesionerId)

    call.enqueue(object : Callback<BlokIIIResponse> {
        override fun onResponse(call: Call<BlokIIIResponse>, response: Response<BlokIIIResponse>) {
            if (response.isSuccessful) {
                navController.navigate(
                    Screens.BlokIV.route
                        .replace("{kuesionerId}", "$kuesionerId")
                        .replace("{count}", "1")
                        .replace("{isEdit}", "0")
                        .replace("{blokIVid}", "0")
                )
            } else if (response.code() == 400) {
                // Handle 400 Bad Request response
                try {
                    val errorResponse =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    Log.e("MyTag", "Error Message: ${errorResponse?.message}")
                    errorCallback.invoke(errorResponse?.message ?: "${errorResponse?.message}")
                } catch (e: JsonSyntaxException) {
                    Log.e("MyTag", "Error parsing JSON: ${response.errorBody()?.string()}")
                }
            } else {
                Log.e(
                    "MyTag",
                    "Unsuccessful response: ${response.code()}, ${response.errorBody()?.string()}"
                )
            }
        }

        override fun onFailure(call: Call<BlokIIIResponse>, t: Throwable) {
            Log.e("MyTag", "Error occurred", t)
        }
    })
}

fun putBlokIII(
    id: Long,
    kepemilikanRumah: String,
    penggunaanListrik: String,
    airMinumUtama: String,
    fasilitasBAB: String,
    lantaiTerluas: String,
    context: Context,
    errorCallback: (String) -> Unit,
) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)

    val call = retro.putBlokIII(
        id,
        kepemilikanRumah,
        penggunaanListrik,
        airMinumUtama,
        fasilitasBAB,
        lantaiTerluas,
    )

    call.enqueue(object : Callback<BlokIIIResponse> {
        override fun onResponse(call: Call<BlokIIIResponse>, response: Response<BlokIIIResponse>) {
            if (response.isSuccessful) {
                Log.d("MyTag", "Message: ${response.body()?.message}")
                Log.d("MyTag", "Data: ${response.body()?.data}")
                Log.d("MyTag", "Status: ${response.body()?.httpStatus}")
                Toast.makeText(context, "Blok II berhasil diperbarui", Toast.LENGTH_SHORT).show()
            } else if (response.code() == 400) {
                // Handle 400 Bad Request response
                try {
                    val errorResponse =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    Log.e("MyTag", "Error Message: ${errorResponse?.message}")
                    errorCallback.invoke(errorResponse?.message ?: "${errorResponse?.message}")
                } catch (e: JsonSyntaxException) {
                    Log.e("MyTag", "Error parsing JSON: ${response.errorBody()?.string()}")
                }
            } else {
                Log.e(
                    "MyTag",
                    "Unsuccessful response: ${response.code()}, ${response.errorBody()?.string()}"
                )
            }
        }

        override fun onFailure(call: Call<BlokIIIResponse>, t: Throwable) {
            Log.e("MyTag", "Error occurred", t)
        }
    })
}

@Preview
@Composable
fun PreviewBlokIIIScreen() {
    BlokIIIScreen(kuesId = 28, navController = rememberNavController(), 0)
}

