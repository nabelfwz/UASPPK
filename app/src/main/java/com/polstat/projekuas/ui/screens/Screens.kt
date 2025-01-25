package com.polstat.projekuas.ui.screens;

sealed class Screens(val route: String) {
    object Login: Screens("login_screen")
    object Register: Screens("register_screen")
    object Home: Screens("home_screen")
    object Pengawas: Screens("pengawas_screen")
    object Profile: Screens("profile_screen")
    object BlokI: Screens("bloki_screen/{kuesionerId}/{isEdit}")
    object BlokIII: Screens("blokiii_screen/{kuesionerId}/{isEdit}")
    object BlokIV: Screens("blokiv_screen/{kuesionerId}/{count}/{isEdit}/{blokIVid}")
    object KuesionerPencacah: Screens("kuesioner_pencacah_screen")
    object KuesionerPengawas: Screens("kuesioner_pengawas_screen")
    object DetailKuesioner: Screens("detail_kuesioner/{kuesionerId}")
    object EditKuesioner: Screens("edit_kuesioner/{kuesionerId}")
    object EditBlokIV: Screens("edit_blokiv/{kuesionerId}")
    object PencacahByPengawas: Screens("pencacahbypengawas_screen")
    object KuesionerPencacahByPengawas: Screens("kues_pencacah_pengawas/{pencacahId}")
}
