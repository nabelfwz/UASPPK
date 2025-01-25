package com.polstat.projekuas.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun EditKuesionerScreen(kuesionerId: Long, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxHeight() // Kolom akan mengisi tinggi penuh
            .fillMaxWidth(),  // Kolom akan mengisi lebar penu
    verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        MyTopAppBar(navController,"Edit Kuesioner")
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp)
                .clickable {
                    navController.navigate(
                        Screens.BlokI.route
                            .replace("{kuesionerId}", "$kuesionerId")
                            .replace("{isEdit}", "1")
                    )
                }
        ) {
            Text(text = "BLOK I")
        }
        Divider()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp)
                .clickable {
                    navController.navigate(
                        Screens.BlokIII.route
                            .replace("{kuesionerId}", "$kuesionerId")
                            .replace("{isEdit}", "1")
                    )
                }
        ) {
            Text(text = "BLOK II")
        }
        Divider()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .padding(8.dp)
                .clickable {
                    navController.navigate(
                        Screens.EditBlokIV.route
                            .replace("{kuesionerId}", "$kuesionerId")
                    )
                }
        ) {
            Text(text = "BLOK III")
        }
        Divider()
    }
}

@Preview
@Composable
fun PreviewEditKuesionerScreen() {
    EditKuesionerScreen(kuesionerId = 55, navController = rememberNavController())
}
