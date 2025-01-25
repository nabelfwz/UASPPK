package com.polstat.projekuas.ui.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.polstat.projekuas.data.network.EditPasswordResponse
import com.polstat.projekuas.data.network.ErrorResponse
import com.polstat.projekuas.data.network.ProfileRequest
import com.polstat.projekuas.data.network.ProfileResponse
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.network.UserData
import com.polstat.projekuas.data.network.UserRequest
import com.polstat.projekuas.data.network.UserResponse
import com.polstat.projekuas.data.network.saveAuthToken
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    var userProfile by remember { mutableStateOf<UserData?>(null) }
    var namaPencacah by remember { mutableStateOf("") }
    var namaPengawas by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var passLama by remember { mutableStateOf("") }
    var passBaru by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        isLoading = true
        getUserProfile(
            context = context,
            onSuccess = { data ->
                isLoading = false
                userProfile = data
                namaPencacah = userProfile!!.namaPencacah
                namaPengawas = userProfile!!.namaPengawas
                username = userProfile!!.username
                email = userProfile!!.email
            },
            onError = { error ->
                isLoading = false
                errorMessage = error
            }
        )
    }

    // UI yang menampilkan data atau pesan kesalahan
    Box(modifier = Modifier.fillMaxSize()) {

        when {
            isLoading -> CircularProgressIndicator()
            userProfile != null ->
                LazyColumn(
                    modifier = Modifier
//                .weight(1f)  // Menempatkan LazyColumn di sisa ruang yang tersedia
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    item {
                        MyTopAppBar(navController, "Profil")
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()  // Tambahkan fillMaxWidth agar dapat diatur secara penuh lebar
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "Edit Profil",fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
                            // Isi dari LazyColumn
                            TextField(
                                value = namaPencacah,
                                onValueChange = { namaPencacah = it },
                                label = { Text("Nama") },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            if (namaPengawas != null) {
                                TextField(
                                    value = namaPengawas,
                                    readOnly = true,
                                    onValueChange = { namaPengawas = it },
                                    label = { Text("Nama Pengawas") },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                            }
                            TextField(
                                value = username,
                                onValueChange = { username = it },
                                label = { Text("Username") },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            TextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Button(
                                onClick = { editProfile(namaPencacah,email,username,context) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text("Ubah Profil")
                            }

                            Text(text = "Edit Password",fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(bottom = 8.dp))
                            // Isi dari LazyColumn
                            TextField(
                                value = passLama,
                                onValueChange = { passLama = it },
                                label = { Text("Password lama") },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Password
                                )
                            )
                            TextField(
                                value = passBaru,
                                onValueChange = { passBaru = it },
                                label = { Text("Password baru") },
                                modifier = Modifier
                                    .fillMaxWidth(),
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    keyboardType = KeyboardType.Password
                                )
                            )
                            Button(
                                onClick = { editPassword(username,passLama,passBaru,context) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                Text("Ubah Password")
                            }
                        }
                    }
                }
            else -> Text(text = "error")
        }
    }
}




fun getUserProfile(context: Context, onSuccess: (UserData) -> Unit, onError: (String) -> Unit) {
    val retroInstance = Retro(context).getRetroClientInstance()
    val userApi = retroInstance.create(UserApi::class.java)

    userApi.getUserProfile().enqueue(object : Callback<ProfileResponse> {
        override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
            if (response.isSuccessful && response.body() != null) {
                onSuccess(response.body()!!.data)
            } else {
                onError("Response not successful: ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
            onError("Error occurred: ${t.message}")
        }
    })
}

fun editProfile(namaPencacah: String, email: String, username: String, context: Context) {
    val request = ProfileRequest()
    request.namaPencacah = namaPencacah
    request.email = email
    request.username = username

    Log.d("userTag", "${request.toString()}")
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)
    retro.editProfile(request).enqueue(object : Callback<ProfileResponse>{
        override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    Toast.makeText(context, "${user?.message}", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("MyTag", "Response body is null")
                }
            } else {
                Log.e("MyTag", "Unsuccessful response: ${response.code()}, ${response.message()}")
            }
        }

        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
            Log.e("Error", "Error occurred", t)
        }

    })
}

fun editPassword(username: String, passLama: String, passBaru: String, context: Context) {
    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)

    retro.changePassword(username,passLama,passBaru).enqueue(object : Callback<EditPasswordResponse>{
        override fun onResponse(call: Call<EditPasswordResponse>, response: Response<EditPasswordResponse>) {
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    Toast.makeText(context, "${user?.message}", Toast.LENGTH_SHORT).show()
                } else{
                    Log.e("MyTag", "Response body is null")
                }
            } else {
                try {
                    val errorResponse = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
                    Log.e("MyTag", "Error Message: ${errorResponse?.message}")
                    Toast.makeText(context, "${errorResponse?.message}", Toast.LENGTH_SHORT).show()
                } catch (e: JsonSyntaxException) {
                    Log.e("MyTag", "Error parsing JSON: ${response.errorBody()?.string()}")
                }
//                Log.e("MyTag", "Unsuccessful response: ${response.code()}, ${response.message()}")
            }
        }

        override fun onFailure(call: Call<EditPasswordResponse>, t: Throwable) {
            Log.e("Error", "Error occurred", t)
        }

    })
}