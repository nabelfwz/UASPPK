package com.polstat.projekuas.ui.screens

import android.app.DatePickerDialog
import android.content.Context
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.polstat.projekuas.data.network.BlokIIIResponse
import com.polstat.projekuas.data.network.BlokIResponse
import com.polstat.projekuas.data.network.BlokIVResponse
import com.polstat.projekuas.data.network.ErrorResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlokIVScreen(kuesId: Long, count: Int, navController: NavController, isEditMode: Int, blokIVid: Long) {
    var blokIVDataItem by remember { mutableStateOf<BlokIVResponse?>(null) }

    var id by remember { mutableStateOf<Long?>(null) }
    // State untuk menyimpan nilai setiap TextField
    var namaLengkap by remember { mutableStateOf("") }

    val optionJk = arrayOf("Laki-laki", "Perempuan")
    var expandedOptionJk by remember { mutableStateOf(false) }
    var jenisKelamin by remember { mutableStateOf(optionJk[0]) }

    var NIK by remember { mutableStateOf("") }

    val optionAlamatSesuaiKK = arrayOf("Ya", "Tidak")
    var expandedOptionAlamatSesuaiKK by remember { mutableStateOf(false) }
    var alamatSesuaiKK by remember { mutableStateOf(optionAlamatSesuaiKK[0]) }

    var tempatLahirProvinsi by remember { mutableStateOf("") }
    var tempatLahirkabKota by remember { mutableStateOf("") }

    val optionTglLahirSesuaiKK = arrayOf("Ya", "Tidak")
    var expandedoptionTglLahirSesuaiKK by remember { mutableStateOf(false) }
    var tglLahirSesuaiKK by remember { mutableStateOf(optionTglLahirSesuaiKK[0]) }

    var tglLahir by remember { mutableStateOf(null as Date?) }
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    val optionpunyaAktaLahir = arrayOf("Ya", "Tidak memiliki", "Tidak tahu")
    var expandedOptionPunyaAktaLahir by remember { mutableStateOf(false) }
    var punyaAktaLahir by remember { mutableStateOf(optionpunyaAktaLahir[0]) }

    val optionKewarganegaraan = arrayOf("WNI", "WNA")
    var expandedOptionKewarganegaraan by remember { mutableStateOf(false) }
    var kewarganegaraan  by remember { mutableStateOf(optionKewarganegaraan[0]) }

    val optionAgama = arrayOf("Islam", "Kristen", "Katolik", "Hindu", "Buddha", "Khonghucu", " Penghayat Kepercayaan", "Lainnya")
    var expandedoptionAgama by remember { mutableStateOf(false) }
    var agama  by remember { mutableStateOf(optionAgama[0]) }

    val optionstatusPerkawinan = arrayOf("Belum kawin", "Kawin", "Cerai hidup", "Cerai mati")
    var expandedoptionstatusPerkawinan by remember { mutableStateOf(false) }
    var statusPerkawinan  by remember { mutableStateOf(optionstatusPerkawinan[0]) }

    val optionhubunganKK = arrayOf("Kepala keluarga", "Suami", "Istri", "Anak", "Menantu", "Cucu", "Orang tua", "Mertua", "Famili lain", "Pembantu", "Lainnya")
    var expandedoptionhubunganKK by remember { mutableStateOf(false) }
    var hubunganKK  by remember { mutableStateOf(optionhubunganKK[0]) }

    var lamaTinggalTahun by remember { mutableStateOf(0) }
    var lamaTinggalBulan by remember { mutableStateOf(0) }

    val optionijazahTertinggi = arrayOf("Tidak/belum sekolah", "Belum tamat SD/sederajat", "Tamat SD/sederajat", " SLTP/sederajat", "SLTA/sederajat", "Diploma I/II", "Akademi/Diploma III/Sarjana Muda", "Diploma IV/Strata I", "Strata II", "Strata III")
    var expandedoptionijazahTertinggi by remember { mutableStateOf(false) }
    var ijazahTertinggi  by remember { mutableStateOf(optionijazahTertinggi[0]) }

    val context = LocalContext.current
    var errorSnackbarMessage by remember { mutableStateOf<String?>(null) }

    var jumlahAnggotaKeluarga by remember { mutableStateOf(0) }
    var isLoading by remember { mutableStateOf(false) }

    // Fetching current year, month and day
    val calendar = Calendar.getInstance()
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    Log.d("MyTag", "Disini $isEditMode")
    if (isEditMode == 1) {
        LaunchedEffect(key1 = true) {
            isLoading = true
            try {
                // Panggil fungsi untuk mendapatkan data Blok I
                blokIVDataItem = getBlokIVDataItem(blokIVid, context)

                Log.d("MyTag", "${blokIVDataItem!!.data.toString()}")
                id = blokIVDataItem?.data?.id?.toLong()
                namaLengkap = blokIVDataItem?.data?.namaLengkap.orEmpty()
                jenisKelamin = optionJk[optionJk.indexOf(blokIVDataItem?.data?.jenisKelamin)]
                NIK = blokIVDataItem?.data?.nik.orEmpty()
                alamatSesuaiKK = optionAlamatSesuaiKK[optionAlamatSesuaiKK.indexOf(blokIVDataItem?.data?.alamatSesuaiKK)]
                tempatLahirProvinsi = blokIVDataItem?.data?.tempatLahirProvinsi.orEmpty()
                tempatLahirkabKota = blokIVDataItem?.data?.tempatLahirkabKota.orEmpty()
                tglLahirSesuaiKK = optionTglLahirSesuaiKK[optionTglLahirSesuaiKK.indexOf(blokIVDataItem?.data?.tglLahirSesuaiKK)]
                tglLahir = dateFormat.parse(blokIVDataItem?.data?.tglLahir.orEmpty())
                punyaAktaLahir = optionpunyaAktaLahir[optionpunyaAktaLahir.indexOf(blokIVDataItem?.data?.punyaAktaLahir)]
                kewarganegaraan = optionKewarganegaraan[optionKewarganegaraan.indexOf(blokIVDataItem?.data?.kewarganegaraan)]
                agama = optionAgama[optionAgama.indexOf(blokIVDataItem?.data?.agama)]
                statusPerkawinan = optionstatusPerkawinan[optionstatusPerkawinan.indexOf(blokIVDataItem?.data?.statusPerkawinan)]
                hubunganKK = optionhubunganKK[optionhubunganKK.indexOf(blokIVDataItem?.data?.hubunganKK)]
                lamaTinggalTahun = blokIVDataItem?.data?.lamaTinggalTahun ?: 0
                lamaTinggalBulan = blokIVDataItem?.data?.lamaTinggalBulan ?: 0
                ijazahTertinggi = optionijazahTertinggi[optionijazahTertinggi.indexOf(blokIVDataItem?.data?.ijazahTertinggi)]
            } catch (e: Exception) {
                // Handle error, misalnya dengan menampilkan pesan kesalahan
                errorSnackbarMessage = e.message ?: "Error occurred"
            } finally {
                isLoading = false
            }
        }
    } else {
        // Dapatkan data Blok I saat komponen diinisialisasi
        LaunchedEffect(kuesId) {
            val blokIResponse = getBlokIData(kuesId, context)
            // Update jumlah anggota keluarga
            jumlahAnggotaKeluarga = blokIResponse.data.jmlAnggotaKeluarga.toIntOrNull() ?: 0
        }
    }

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            val calendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, selectedYear)
                set(Calendar.MONTH, selectedMonth)
                set(Calendar.DAY_OF_MONTH, selectedDayOfMonth)
            }
            tglLahir = calendar.time
        }, year, month, dayOfMonth
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isEditMode == 1) {
            MyTopAppBar(navController,"Edit Blok III")
        } else {
            TopAppBar(
                title = { Text("Blok III") }
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
                    if (isEditMode == 0) {
                        Text(text = "Data anggota keluarga $count")
                    }

                    // Isi dari LazyColumn
                    TextField(
                        value = namaLengkap,
                        onValueChange = { namaLengkap = it },
                        label = { Text("Nama lengkap sesuai KK/KTP") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedOptionJk,
                        onExpandedChange = {
                            expandedOptionJk = it
                            if (it) {
                                expandedOptionAlamatSesuaiKK = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionAgama = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionhubunganKK = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = jenisKelamin,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Jenis Kelamin") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOptionJk) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOptionJk,
                            onDismissRequest = { expandedOptionJk = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionJk.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        jenisKelamin = item
                                        expandedOptionJk = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    TextField(
                        value = NIK,
                        onValueChange = { NIK = it },
                        label = { Text("Nomor Induk Kependudukan (NIK)") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedOptionAlamatSesuaiKK,
                        onExpandedChange = {
                            expandedOptionAlamatSesuaiKK = it
                            if (it) {
                                expandedOptionJk = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionAgama = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionhubunganKK = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = alamatSesuaiKK,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Apakah alamat tempat tinggal sekarang (SLS) sesuai dengan alamat yang tertulis pada KK?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOptionAlamatSesuaiKK) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOptionAlamatSesuaiKK,
                            onDismissRequest = { expandedOptionAlamatSesuaiKK = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionAlamatSesuaiKK.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        alamatSesuaiKK = item
                                        expandedOptionAlamatSesuaiKK = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    TextField(
                        value = tempatLahirProvinsi,
                        onValueChange = { tempatLahirProvinsi = it },
                        label = { Text("Tempat lahir (provinsi)") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    TextField(
                        value = tempatLahirkabKota,
                        onValueChange = { tempatLahirkabKota = it },
                        label = { Text("Tempat lahir (kab/kota)") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedoptionTglLahirSesuaiKK,
                        onExpandedChange = {
                            expandedoptionTglLahirSesuaiKK = it
                            if (it) {
                                expandedOptionJk = false
                                expandedOptionAlamatSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionAgama = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionhubunganKK = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = tglLahirSesuaiKK,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Apakah tanggal lahir sama dengan KK/KTP?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedoptionTglLahirSesuaiKK) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedoptionTglLahirSesuaiKK,
                            onDismissRequest = { expandedoptionTglLahirSesuaiKK = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionTglLahirSesuaiKK.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        tglLahirSesuaiKK = item
                                        expandedoptionTglLahirSesuaiKK = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    TextField(
                        value = tglLahir?.let { dateFormat.format(it) } ?: "",
                        onValueChange = { /* Do nothing, as it's read only */ },
                        label = { Text("Tanggal/Bulan/Tahun lahir") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Use Box with clickable modifier to wrap the TextField
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { datePicker.show() }
                    ) {
                        Text(text = "Pilih tanggal")
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedOptionPunyaAktaLahir,
                        onExpandedChange = {
                            expandedOptionPunyaAktaLahir = it
                            if (it) {
                                expandedOptionJk = false
                                expandedOptionAlamatSesuaiKK = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionAgama = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionhubunganKK = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = punyaAktaLahir,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Apakah memiliki akta kelahiran dari kantor catatan sipil?") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOptionPunyaAktaLahir) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOptionPunyaAktaLahir,
                            onDismissRequest = { expandedOptionPunyaAktaLahir = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionpunyaAktaLahir.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        punyaAktaLahir = item
                                        expandedOptionPunyaAktaLahir = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedOptionKewarganegaraan,
                        onExpandedChange = {
                            expandedOptionKewarganegaraan = it
                            if (it) {
                                expandedOptionJk = false
                                expandedOptionAlamatSesuaiKK = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedoptionAgama = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionhubunganKK = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = kewarganegaraan,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Kewarganegaraan") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedOptionKewarganegaraan) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedOptionKewarganegaraan,
                            onDismissRequest = { expandedOptionKewarganegaraan = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionKewarganegaraan.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        kewarganegaraan = item
                                        expandedOptionKewarganegaraan = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedoptionAgama,
                        onExpandedChange = {
                            expandedoptionAgama = it
                            if (it) {
                                expandedOptionJk = false
                                expandedOptionAlamatSesuaiKK = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionhubunganKK = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = agama,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Agama/Kepercayaan") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedoptionAgama) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedoptionAgama,
                            onDismissRequest = { expandedoptionAgama = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionAgama.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        agama = item
                                        expandedoptionAgama = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedoptionstatusPerkawinan,
                        onExpandedChange = {
                            expandedoptionstatusPerkawinan = it
                            if (it) {
                                expandedOptionJk = false
                                expandedOptionAlamatSesuaiKK = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionAgama = false
                                expandedoptionhubunganKK = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = statusPerkawinan,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status perkawinan") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedoptionstatusPerkawinan) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedoptionstatusPerkawinan,
                            onDismissRequest = { expandedoptionstatusPerkawinan = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionstatusPerkawinan.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        statusPerkawinan = item
                                        expandedoptionstatusPerkawinan = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    ExposedDropdownMenuBox(
                        expanded = expandedoptionhubunganKK,
                        onExpandedChange = {
                            expandedoptionhubunganKK = it
                            if (it) {
                                expandedOptionJk = false
                                expandedOptionAlamatSesuaiKK = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionAgama = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionijazahTertinggi = false
                            }
                        }
                    ) {
                        TextField(
                            value = hubunganKK,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Status hubungan dengan kepala keluarga") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedoptionhubunganKK) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedoptionhubunganKK,
                            onDismissRequest = { expandedoptionhubunganKK = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionhubunganKK.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        hubunganKK = item
                                        expandedoptionhubunganKK = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    TextField(
                        value = lamaTinggalTahun.toString(),
                        onValueChange = {
                            lamaTinggalTahun = it.toIntOrNull() ?: 0
                        },
                        label = { Text("Lama Tinggal (Tahun)") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    RedNoteText("kurang dari 1 tahun")

                    TextField(
                        value = lamaTinggalBulan.toString(),
                        onValueChange = {
                            lamaTinggalBulan = it.toIntOrNull() ?: 0
                        },
                        label = { Text("Lama Tinggal (Bulan)") },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    RedNoteText("lebih dari 1 tahun")

                    ExposedDropdownMenuBox(
                        expanded = expandedoptionijazahTertinggi,
                        onExpandedChange = {
                            expandedoptionijazahTertinggi = it
                            if (it) {
                                expandedOptionJk = false
                                expandedOptionAlamatSesuaiKK = false
                                expandedoptionTglLahirSesuaiKK = false
                                expandedOptionPunyaAktaLahir = false
                                expandedOptionKewarganegaraan = false
                                expandedoptionAgama = false
                                expandedoptionstatusPerkawinan = false
                                expandedoptionhubunganKK = false
                            }
                        }
                    ) {
                        TextField(
                            value = ijazahTertinggi,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ijazah/pendidikan tertinggi yang ditamatkan") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedoptionijazahTertinggi) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = expandedoptionijazahTertinggi,
                            onDismissRequest = { expandedoptionijazahTertinggi = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            optionijazahTertinggi.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        ijazahTertinggi = item
                                        expandedoptionijazahTertinggi = false
                                        Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    Button(onClick = {
                        if (isEditMode == 0) {
                            postBlokIV(
                                namaLengkap,
                                jenisKelamin,
                                NIK,
                                alamatSesuaiKK,
                                tempatLahirProvinsi,
                                tempatLahirkabKota,
                                tglLahirSesuaiKK,
                                tglLahir,
                                punyaAktaLahir,
                                kewarganegaraan,
                                agama,
                                statusPerkawinan,
                                hubunganKK,
                                lamaTinggalTahun,
                                lamaTinggalBulan,
                                ijazahTertinggi,
                                kuesId,
                                context,
                                count,
                                jumlahAnggotaKeluarga,
                                navController,
                                { errorMessage -> errorSnackbarMessage = errorMessage },
                            )
                        } else {
                            putBlokIV(
                                id!!,
                                namaLengkap,
                                jenisKelamin,
                                NIK,
                                alamatSesuaiKK,
                                tempatLahirProvinsi,
                                tempatLahirkabKota,
                                tglLahirSesuaiKK,
                                tglLahir,
                                punyaAktaLahir,
                                kewarganegaraan,
                                agama,
                                statusPerkawinan,
                                hubunganKK,
                                lamaTinggalTahun,
                                lamaTinggalBulan,
                                ijazahTertinggi,
                                kuesId,
                                context,
                                { errorMessage -> errorSnackbarMessage = errorMessage },
                            )
                        }
                    },modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Submit")
                    }
                }
            }
        }
    }
}

@Composable
fun RedNoteText(text: String) {
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("* ")
        }
        withStyle(style = SpanStyle(color = Color.Red)) {
            append("Isi 0 jika lama tinggal ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Red)) {
            append(text.substringAfter("Isi 0 jika lama tinggal "))
        }
    }

    Text(text = annotatedString)
}

fun postBlokIV(
    namaLengkap: String,
    jenisKelamin: String,
    NIK: String,
    alamatSesuaiKK: String,
    tempatLahirProvinsi: String,
    tempatLahirkabKota: String,
    tglLahirSesuaiKK: String,
    tglLahir: Date?,
    punyaAktaLahir: String,
    kewarganegaraan: String,
    agama: String,
    statusPerkawinan: String,
    hubunganKK: String,
    lamaTinggalTahun: Int,
    lamaTinggalBulan: Int,
    ijazahTertinggi: String,
    kuesionerId: Long,
    context: Context,
    count: Int,
    jumlahAnggotaKeluarga: Int,
    navController: NavController,
    errorCallback: (String) -> Unit,
) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)

    val call = retro.postBlokIV(namaLengkap, jenisKelamin, NIK, alamatSesuaiKK, tempatLahirProvinsi, tempatLahirkabKota, tglLahirSesuaiKK, formatTanggal(tglLahir!!), punyaAktaLahir, kewarganegaraan, agama, statusPerkawinan, hubunganKK, lamaTinggalTahun, lamaTinggalBulan, ijazahTertinggi, kuesionerId)

    call.enqueue(object : Callback<BlokIVResponse> {
        override fun onResponse(call: Call<BlokIVResponse>, response: Response<BlokIVResponse>) {
            if (response.isSuccessful) {
                Log.d("MyTag", "Message: ${response.body()?.message}")
                Log.d("MyTag", "Data: ${response.body()?.data}")
                Log.d("MyTag", "Status: ${response.body()?.httpStatus}")
                if (count < jumlahAnggotaKeluarga) {
                    navController.navigate(
                        Screens.BlokIV.route
                            .replace("{kuesionerId}", "$kuesionerId")
                            .replace("{count}", "${count+1}")
                            .replace("{isEdit}", "0")
                            .replace("{blokIVid}", "0")
                    )
                } else {
                    navController.navigate(Screens.Home.route)
                }
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

        override fun onFailure(call: Call<BlokIVResponse>, t: Throwable) {
            Log.e("MyTag", "Error occurred", t)
        }
    })
}

fun putBlokIV(
    id: Long,
    namaLengkap: String,
    jenisKelamin: String,
    NIK: String,
    alamatSesuaiKK: String,
    tempatLahirProvinsi: String,
    tempatLahirkabKota: String,
    tglLahirSesuaiKK: String,
    tglLahir: Date?,
    punyaAktaLahir: String,
    kewarganegaraan: String,
    agama: String,
    statusPerkawinan: String,
    hubunganKK: String,
    lamaTinggalTahun: Int,
    lamaTinggalBulan: Int,
    ijazahTertinggi: String,
    kuesionerId: Long,
    context: Context,
    errorCallback: (String) -> Unit,
) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)
    Log.d("MyTag", formatTanggal(tglLahir!!))
    val call = retro.putBlokIV(
        id,
        namaLengkap,
        jenisKelamin,
        NIK,
        alamatSesuaiKK,
        tempatLahirProvinsi,
        tempatLahirkabKota,
        tglLahirSesuaiKK,
        formatTanggal(tglLahir!!),
        punyaAktaLahir,
        kewarganegaraan,
        agama,
        statusPerkawinan,
        hubunganKK,
        lamaTinggalTahun,
        lamaTinggalBulan,
        ijazahTertinggi,
    )

    call.enqueue(object : Callback<BlokIVResponse> {
        override fun onResponse(call: Call<BlokIVResponse>, response: Response<BlokIVResponse>) {
            if (response.isSuccessful) {
                Log.d("MyTag", "Message: ${response.body()?.message}")
                Log.d("MyTag", "Data: ${response.body()?.data}")
                Log.d("MyTag", "Status: ${response.body()?.httpStatus}")
                Toast.makeText(context, "Blok III berhasil diperbarui", Toast.LENGTH_SHORT).show()
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

        override fun onFailure(call: Call<BlokIVResponse>, t: Throwable) {
            Log.e("MyTag", "Error occurred", t)
        }
    })
}

fun formatTanggal(date: Date): String {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(date)
}
