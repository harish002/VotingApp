package com.example.task.android.Screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.task.android.Components.custTextInput
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore


@Composable
fun RegisterScreen(navController: NavHostController) {
    var username by remember {        mutableStateOf("") }
    var Password by remember {        mutableStateOf("") }
    var email by remember {        mutableStateOf("") }
    var phone by remember {        mutableStateOf("") }
    val context= LocalContext.current
    val  mAuth = FirebaseAuth.getInstance()


    // below line is used to get reference for our database.

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        custTextInput(lable = "Usename",
            value = username,
            onValueChange = {username =it})
        Spacer(modifier = Modifier.padding(20.dp))
        custTextInput(lable = "Password",
            value = Password,
            onValueChange = {Password =it})
        Spacer(modifier = Modifier.padding(20.dp))

        custTextInput(lable = "email",
            value = email,
            onValueChange = {email =it})
        Spacer(modifier = Modifier.padding(20.dp))
        custTextInput(lable = "Phone",
            value = phone,
            onValueChange = {phone =it})
        Spacer(modifier = Modifier.padding(20.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center) {
            Button(onClick = {
                navController.navigate("login")
                Toast.makeText(context, "Login pressed", Toast.LENGTH_SHORT).show() })
            {
                Text("Login")
            }

            Spacer(modifier = Modifier.padding(20.dp))

            Button(onClick = {
                registerNewUser(context,mAuth,
                    UserRegister(username, Password,phone,email),navController)

            }) {
                Text("Register")
            }
        }

    }
}


