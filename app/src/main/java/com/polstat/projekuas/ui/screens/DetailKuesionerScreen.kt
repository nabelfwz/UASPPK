package com.polstat.projekuas.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.polstat.projekuas.data.network.BlokIIIResponse
import com.polstat.projekuas.data.network.BlokIIResponse
import com.polstat.projekuas.data.network.BlokIResponse
import com.polstat.projekuas.data.network.BlokIVResponse
import com.polstat.projekuas.data.network.GetBlokIVResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.repository.api.UserApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailKuesionerScreen(kuesionerId: Long, navController: NavController) {
    var blokIResponse by remember { mutableStateOf<BlokIResponse?>(null) }
    var blokIIIResponse by remember { mutableStateOf<BlokIIIResponse?>(null) }
    var blokIVResponse by remember { mutableStateOf<GetBlokIVResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        isLoading = true
        try {
            // Panggil fungsi untuk mendapatkan data Blok I
            blokIResponse = getBlokIData(kuesionerId, context)
            blokIIIResponse = getBlokIIIData(kuesionerId, context)
            blokIVResponse = getBlokIVData(kuesionerId, context)
        } catch (e: Exception) {
            // Handle error, misalnya dengan menampilkan pesan kesalahan
            errorMessage = e.message ?: "Error occurred"
        } finally {
            isLoading = false
        }
    }

    // UI yang menampilkan data atau pesan kesalahan
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when {
            isLoading -> CircularProgressIndicator()
            blokIIIResponse != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    item {
                        MyTopAppBar(navController, "Lihat Kuesioner")
                        Column (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            BlokITextField(blokIResponse!!)
                            Divider(modifier = Modifier.padding(vertical = 7.dp))
                            BlokIIITextField(blokIIIResponse!!)
                            Divider(modifier = Modifier.padding(vertical = 7.dp))
                            BlokIVTextField(blokIVResponse!!)
                        }
                    }
                }
            }
            else -> Text(text = errorMessage)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlokITextField(blokIResponse: BlokIResponse) {
    // Tampilkan TextField dan isi nilainya dari blokIResponse
    Text("BLOK I", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
    // Tambahkan TextField untuk setiap properti dari BlokIResponse
    TextField(value = blokIResponse.data.kodeProvinsi, onValueChange = {}, label = { Text("Kode provinsi") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.kodeKabupatenKota, onValueChange = {}, label = { Text("Kode kabupaten/kota") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.kodeKecamatan, onValueChange = {}, label = { Text("Kode kecamatan") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.kodeDesaKelurahan, onValueChange = {}, label = { Text("Kode desa/kelurahan") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.namaSLS, onValueChange = {}, label = { Text("Nama SLS") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.noBangunan, onValueChange = {}, label = { Text("Nomor bangunan") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.alamat, onValueChange = {}, label = { Text("Alamat") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.namaKK, onValueChange = {}, label = { Text("Nama kepala keluarga") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.jmlAnggotaKeluarga, onValueChange = {}, label = { Text("Jumlah anggota keluarga") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.noHpKK, onValueChange = {}, label = { Text("Nomo HP kepala keluarga") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIResponse.data.noUrutKeluarga, onValueChange = {}, label = { Text("Nomor urut keluarga") }, readOnly = true, modifier = Modifier.fillMaxWidth())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlokIIITextField(blokIIIResponse: BlokIIIResponse) {
    // Tampilkan TextField dan isi nilainya dari blokIResponse
    Text("BLOK II", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))
    // Tambahkan TextField untuk setiap properti dari BlokIResponse
    TextField(value = blokIIIResponse.data.kepemilikanRumah, onValueChange = {}, label = { Text("Status kepemilikan rumah yang ditempati") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIIIResponse.data.penggunaanListrik, onValueChange = {}, label = { Text("Penggunaan listrik") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIIIResponse.data.airMinumUtama, onValueChange = {}, label = { Text("Sumber air minum utama") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIIIResponse.data.fasilitasBAB, onValueChange = {}, label = { Text("Fasilitas jamban/tempat buang air besar dan septic tank") }, readOnly = true, modifier = Modifier.fillMaxWidth())
    TextField(value = blokIIIResponse.data.lantaiTerluas, onValueChange = {}, label = { Text("Jenis lantai terluas") }, readOnly = true, modifier = Modifier.fillMaxWidth())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlokIVTextField(blokIVResponse: GetBlokIVResponse) {
    // Tampilkan TextField dan isi nilainya dari blokIVResponse
    Text("BLOK III", fontSize = 20.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 10.dp))

    blokIVResponse.data.forEachIndexed { index, blokIVItem ->
        // Tampilkan TextField untuk setiap properti dari BlokIVItem
        Text(text = "Anggota Keluarga ${index + 1}", modifier = Modifier.padding(vertical = 10.dp))

        TextField(value = blokIVItem.namaLengkap, onValueChange = {}, label = { Text("Nama lengkap") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.jenisKelamin, onValueChange = {}, label = { Text("Jenis kelamin") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.nik, onValueChange = {}, label = { Text("Nomor Induk Kependudukan (NIK)") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.alamatSesuaiKK, onValueChange = {}, label = { Text("Alamat tempat tinggal sekarang sesuai dengan KK") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.tempatLahirProvinsi, onValueChange = {}, label = { Text("Tempat lahir (provinsi)") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.tempatLahirkabKota, onValueChange = {}, label = { Text("Tempat lahir (kabupaten/kota)") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.tglLahirSesuaiKK, onValueChange = {}, label = { Text("Tanggal lahir sama dengan KK/KTP") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.tglLahir, onValueChange = {}, label = { Text("Tanggal lahir") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.punyaAktaLahir, onValueChange = {}, label = { Text("Memiliki akta kelahiran dari kantor catatan sipil") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.kewarganegaraan, onValueChange = {}, label = { Text("Kewarganegaraan") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.agama, onValueChange = {}, label = { Text("Agama") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.statusPerkawinan, onValueChange = {}, label = { Text("Status Perkawinan") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.hubunganKK, onValueChange = {}, label = { Text("Status hubungan dengan kepala keluarga") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.lamaTinggalTahun.toString(), onValueChange = {}, label = { Text("Lama tinggal di alamat yang sekarang (dalam tahun)") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.lamaTinggalBulan.toString(), onValueChange = {}, label = { Text("Lama tinggal di alamat yang sekarang (dalam bulan)") }, readOnly = true, modifier = Modifier.fillMaxWidth())
        TextField(value = blokIVItem.ijazahTertinggi, onValueChange = {}, label = { Text("Ijazah tertinggi") }, readOnly = true, modifier = Modifier.fillMaxWidth())

            // Tambahkan TextField untuk properti lainnya sesuai kebutuhan
    }
}


suspend fun getBlokIData(kuesionerId: Long, context: Context): BlokIResponse {
    try {
        // Inisialisasi Retrofit menggunakan instance Retro
        val retrofit = Retro(context).getRetroClientInstance()

        // Buat instance dari service
        val service = retrofit.create(UserApi::class.java)

        // Panggil fungsi dari service untuk mendapatkan data Blok I
        return service.getBlokI(kuesionerId)
    } catch (e: Exception) {
        // Handle error, misalnya dengan log atau melempar exception
        throw e
    }
}

fun getBlokIIData(kuesionerId: Long, context: Context): BlokIIResponse {
    try {
        // Inisialisasi Retrofit menggunakan instance Retro
        val retrofit = Retro(context).getRetroClientInstance()

        // Buat instance dari service
        val service = retrofit.create(UserApi::class.java)

        // Panggil fungsi dari service untuk mendapatkan data Blok I
        return service.getBlokII(kuesionerId)
    } catch (e: Exception) {
        // Handle error, misalnya dengan log atau melempar exception
        throw e
    }
}

suspend fun getBlokIIIData(kuesionerId: Long, context: Context): BlokIIIResponse {
    try {
        // Inisialisasi Retrofit menggunakan instance Retro
        val retrofit = Retro(context).getRetroClientInstance()

        // Buat instance dari service
        val service = retrofit.create(UserApi::class.java)

        // Panggil fungsi dari service untuk mendapatkan data Blok I
        return service.getBlokIII(kuesionerId)
    } catch (e: Exception) {
        // Handle error, misalnya dengan log atau melempar exception
        throw e
    }
}

suspend fun getBlokIVData(kuesionerId: Long, context: Context): GetBlokIVResponse {
    try {
        // Inisialisasi Retrofit menggunakan instance Retro
        val retrofit = Retro(context).getRetroClientInstance()

        // Buat instance dari service
        val service = retrofit.create(UserApi::class.java)

        // Panggil fungsi dari service untuk mendapatkan data Blok I
        return service.getBlokIV(kuesionerId)
    } catch (e: Exception) {
        // Handle error, misalnya dengan log atau melempar exception
        throw e
    }
}

suspend fun getBlokIVDataItem(id: Long, context: Context): BlokIVResponse {
    try {
        // Inisialisasi Retrofit menggunakan instance Retro
        val retrofit = Retro(context).getRetroClientInstance()

        // Buat instance dari service
        val service = retrofit.create(UserApi::class.java)

        // Panggil fungsi dari service untuk mendapatkan data Blok I
        return service.getBlokIVById(id)
    } catch (e: Exception) {
        // Handle error, misalnya dengan log atau melempar exception
        throw e
    }
}