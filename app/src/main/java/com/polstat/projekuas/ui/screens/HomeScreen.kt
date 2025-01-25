package com.polstat.projekuas.ui.screens

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.polstat.projekuas.R
import com.polstat.projekuas.data.network.BlokIIIResponse
import com.polstat.projekuas.data.network.BlokIIResponse
import com.polstat.projekuas.data.network.CreateKuesionerResponse
import com.polstat.projekuas.data.network.ErrorResponse
import com.polstat.projekuas.data.network.ProfileResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.network.UserData
import com.polstat.projekuas.data.network.UserResponse
import com.polstat.projekuas.data.network.clearAuthToken
import com.polstat.projekuas.data.repository.api.UserApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun HomeScreen(navController: NavController) {
    var userProfile by remember { mutableStateOf<UserData?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        isLoading = true
        getUserProfile(
            context = context,
            onSuccess = { data ->
                isLoading = false
                userProfile = data
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.sp_home), // Gantilah dengan sumber daya gambar yang sesuai
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Sesuaikan tinggi gambar sesuai kebutuhan
                .padding(horizontal = 20.dp)
        )

        // Baris Pertama
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
//                .padding(bottom = 8.dp)
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .clickable {
                        userProfile?.let {
                            coroutineScope.launch {
                                postKuesioner(it, context, navController)
                            }
                        }
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.hsv(195f, 0.07f, 0.91f),
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Icon di atas Text
//                    Icon(
//                        painter = painterResource(id = R.drawable.forooo), // Ganti dengan sumber daya ikon yang sesuai
//                        contentDescription = null,
//                        modifier = Modifier.height(80.dp)
//                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Text di bawah Icon
                    Text(
                        text = "KUESIONER BARU",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 19.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(align = Alignment.Center)
                    )
                }
            }


            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .clickable {
                        navController.navigate(Screens.KuesionerPencacah.route)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.hsv(195f, 0.07f, 0.91f), //Card background color
                    //Card content color,e.g.text
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "RIWAYAT KUESIONER",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }

        // Baris Kedua
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 8.dp)
        ) {
            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .clickable {
                        navController.navigate(Screens.Profile.route)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.hsv(195f, 0.07f, 0.91f), //Card background color
                    //Card content color,e.g.text
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "PROFIL",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }

            ElevatedCard(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
                    .clickable {
                        logout(context, navController)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.hsv(195f, 0.07f, 0.91f), //Card background color
                    //Card content color,e.g.text
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "LOGOUT",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize(align = Alignment.Center)
                )
            }
        }
    }

}

fun postKuesioner(user: UserData, context: Context, navController: NavController) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)
    Log.d("MyTag", user.id.toString())

    val call = retro.postKuesioner(user.id)

    call.enqueue(object : Callback<CreateKuesionerResponse> {
        override fun onResponse(
            call: Call<CreateKuesionerResponse>,
            response: Response<CreateKuesionerResponse>
        ) {
            if (response.isSuccessful) {
                Log.d("MyTag", "Message: ${response.body()?.message}")
                Log.d("MyTag", "Id: ${response.body()?.data}")
                val kuesionerId = response.body()?.data
                postBlokII(user.namaPencacah, user.namaPengawas, kuesionerId!!, context, navController)
            } else {
                Log.e("MyTag", "Unsuccessful response: ${response.code()}, ${response.message()}")
            }
        }

        override fun onFailure(call: Call<CreateKuesionerResponse>, t: Throwable) {
            // Handle failure here
            Log.e("MyTag", "Error occurred", t)
        }
    })
}

fun postBlokII(
    namaPencacah: String,
    namaPengawas: String,
    kuesionerId: Long,
    context: Context,
    navController: NavController
) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)

    val call = retro.postBlokII(
        namaPencacah,
        namaPengawas,
        kuesionerId
    )

    call.enqueue(object : Callback<BlokIIResponse> {
        override fun onResponse(call: Call<BlokIIResponse>, response: Response<BlokIIResponse>) {
            if (response.isSuccessful) {
                Log.d("MyTag", "Message: ${response.body()?.message}")
                Log.d("MyTag", "Data: ${response.body()?.data}")
                Log.d("MyTag", "Status: ${response.body()?.httpStatus}")
                navController.navigate(
                    Screens.BlokI.route
                        .replace("{kuesionerId}", "$kuesionerId")
                        .replace("{isEdit}", "0")
                )
            } else if (response.code() == 400) {
                // Handle 400 Bad Request response
                try {
                    val errorResponse =
                        Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    Log.e("MyTag", "Error Message: ${errorResponse?.message}")
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

        override fun onFailure(call: Call<BlokIIResponse>, t: Throwable) {
            Log.e("MyTag", "Error occurred", t)
        }
    })
}

fun logout(context: Context, navController: NavController) {
    context.clearAuthToken()

    // Navigasi ke halaman login atau halaman awal aplikasi
    navController.navigate(Screens.Login.route)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
