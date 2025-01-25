package com.polstat.projekuas.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.polstat.projekuas.data.network.BlokIVResponse
import com.polstat.projekuas.data.network.GetBlokIVResponse

@Composable
fun EditBlokIVScreen(kuesId: Long,navController: NavController) {
    var blokIVResponse by remember { mutableStateOf<GetBlokIVResponse?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorSnackbarMessage by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        isLoading = true
        try {
            // Panggil fungsi untuk mendapatkan data Blok I
            blokIVResponse = getBlokIVData(kuesId, context)
        } catch (e: Exception) {
            // Handle error, misalnya dengan menampilkan pesan kesalahan
            errorSnackbarMessage = e.message ?: "Error occurred"
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxHeight() // Kolom akan mengisi tinggi penuh
            .fillMaxWidth(),  // Kolom akan mengisi lebar penu
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MyTopAppBar(navController, "Edit Blok III")

        blokIVResponse?.data?.forEachIndexed { index, blokIVItem ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
                    .padding(8.dp)
                    .clickable {
                        navController.navigate(
                            Screens.BlokIV.route
                                .replace("{kuesionerId}", "$kuesId")
                                .replace("{count}", "0")
                                .replace("{isEdit}", "1")
                                .replace("{blokIVid}", "${blokIVItem.id}")
                        )
                    }
            ) {
                Text(text = "${index+1}. ${blokIVItem.namaLengkap}")
            }
            Divider()
        }
    }
}