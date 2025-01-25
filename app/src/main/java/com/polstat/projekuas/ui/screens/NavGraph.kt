package com.polstat.projekuas.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavGraph (navController: NavHostController){
    val context = LocalContext.current
    NavHost(
        navController = navController,
        startDestination = Screens.Login.route)
    {
        composable(route = Screens.Profile.route){
            ProfileScreen(navController)
        }
        composable(route = Screens.Login.route){
            LoginScreen(navController)
        }
        composable(route = Screens.Register.route){
            RegisterScreen(navController)
        }
        composable(route = Screens.Home.route){
            HomeScreen(navController)
        }
        composable(route = Screens.Pengawas.route){
            PengawasScreen(navController)
        }
        composable(
            route = Screens.BlokI.route,
            arguments = listOf(
                navArgument("kuesionerId") { type = NavType.LongType },
                navArgument("isEdit") { type = NavType.IntType  }
            )
        ) { backStackEntry ->
            val kuesionerId = backStackEntry.arguments?.getLong("kuesionerId") ?: 0L
            val isEdit = backStackEntry.arguments?.getInt("isEdit") ?: 0
            BlokIScreen(kuesionerId, navController, isEdit)
        }
        composable(
            route = Screens.BlokIII.route,
            arguments = listOf(
                navArgument("kuesionerId") { type = NavType.LongType },
                navArgument("isEdit") { type = NavType.IntType  }
            )
        ) { backStackEntry ->
            val kuesionerId = backStackEntry.arguments?.getLong("kuesionerId") ?: 0L
            val isEdit = backStackEntry.arguments?.getInt("isEdit") ?: 0
            BlokIIIScreen(kuesionerId, navController, isEdit)
        }
        composable(
            route = Screens.BlokIV.route,
            arguments = listOf(
                navArgument("kuesionerId") { type = NavType.LongType },
                navArgument("count") { type = NavType.IntType  },
                navArgument("isEdit") { type = androidx.navigation.NavType.IntType  },
                navArgument("blokIVid") { type = androidx.navigation.NavType.LongType },
            )
        ) { backStackEntry ->
            val kuesionerId = backStackEntry.arguments?.getLong("kuesionerId") ?: 0L
            val count = backStackEntry.arguments?.getInt("count") ?: 0
            val isEdit = backStackEntry.arguments?.getInt("isEdit") ?: 0
            val blokIVid = backStackEntry.arguments?.getLong("blokIVid") ?: 0
            BlokIVScreen(kuesionerId, count, navController, isEdit, blokIVid)
        }
        composable(route = Screens.KuesionerPencacah.route){
            KuesionerPencacahScreen(navController)
        }
        composable(route = Screens.KuesionerPengawas.route){
            KuesionerPengawasScreen(navController)
        }
        composable(route = Screens.PencacahByPengawas.route){
            PencacahByPengawasScreen(navController)
        }
        composable(
            route = Screens.DetailKuesioner.route,
            arguments = listOf(navArgument("kuesionerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val kuesionerId = backStackEntry.arguments?.getLong("kuesionerId") ?: 0L
            DetailKuesionerScreen(kuesionerId, navController)
        }
        composable(
            route = Screens.KuesionerPencacahByPengawas.route,
            arguments = listOf(navArgument("pencacahId") { type = NavType.LongType })
        ) { backStackEntry ->
            val pencacahId = backStackEntry.arguments?.getLong("pencacahId") ?: 0L
            KuesionerPencacahByPengawasScreen(pencacahId, navController)
        }
        composable(
            route = Screens.EditKuesioner.route,
            arguments = listOf(navArgument("kuesionerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val kuesionerId = backStackEntry.arguments?.getLong("kuesionerId") ?: 0L
            EditKuesionerScreen(kuesionerId, navController)
        }
        composable(
            route = Screens.EditBlokIV.route,
            arguments = listOf(navArgument("kuesionerId") { type = NavType.LongType })
        ) { backStackEntry ->
            val kuesionerId = backStackEntry.arguments?.getLong("kuesionerId") ?: 0L
            EditBlokIVScreen(kuesionerId, navController)
        }
    }
}