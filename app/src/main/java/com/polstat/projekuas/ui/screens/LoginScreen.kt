package com.polstat.projekuas.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.polstat.projekuas.R
import com.polstat.projekuas.data.network.Retro
import com.polstat.projekuas.data.network.UserRequest
import com.polstat.projekuas.data.network.UserResponse
import com.polstat.projekuas.data.network.saveAuthToken
import com.polstat.projekuas.data.repository.api.UserApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    val clickableText = buildAnnotatedString {
        append("Belum punya akun? Silakan register ")
        withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline, fontWeight = FontWeight.Bold, color = Color.Blue)) {
            append("disini")
            // Add a click handler to this part of the text
            addStringAnnotation(
                tag = "RegisterLink",
                start = length - 5, // Index of the first character of "disini"
                end = length,
                annotation = "Register"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // Menambahkan Card di tengah
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .zIndex(0f) // Menempatkan card di bawah
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SISPENDUK",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(0.dp, 16.dp)
                )
                TextField(
                    value = username,
                    onValueChange = { username = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Username") }
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    )
                )
                ClickableText(
                    text = clickableText,
                    modifier = Modifier.padding(16.dp),
                    onClick = {
                        navController.navigate(Screens.Register.route)
                    },
                    style = MaterialTheme.typography.bodySmall
                )
                Button(
                    onClick = { login(username, password, context, navController) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text("Login")
                }
            }
        }
    }

}

fun login(username: String, password: String, context: Context, navController: NavController) {
    val request = UserRequest()
    request.username = username
    request.password = password

    val retro = Retro(context).getRetroClientInstance().create(UserApi::class.java)
    retro.login(request).enqueue(object : Callback<UserResponse> {
        override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
            if (response.isSuccessful) {
                val user = response.body()
                if (user != null) {
                    user.accessToken?.let { context.saveAuthToken(it) }
                    if (user.roles?.get(0) == "ROLE_PENCACAH") {
                        navController.navigate(Screens.Home.route)
                    } else {
                        navController.navigate(Screens.Pengawas.route)
                    }
                } else {
                    Log.e("MyTag", "Response body is null")
                }
            } else {
                Log.e("MyTag", "Unsuccessful response: ${response.code()}, ${response.message()}")
            }
        }

        override fun onFailure(call: Call<UserResponse>, t: Throwable) {
            Log.e("Error", "Error occurred", t)
        }

    })
}

@Preview
@Composable
fun PreviewLoginForm() {
    LoginScreen(navController = rememberNavController())
}
