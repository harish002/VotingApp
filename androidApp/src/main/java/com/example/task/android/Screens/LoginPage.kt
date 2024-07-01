package com.example.task.android.Screens

import android.content.Context
import android.text.TextUtils
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson


@Composable
fun LoginScreen(votesList: MutableMap<String, Boolean>, navController: NavHostController) {

    var username by remember {        mutableStateOf("")}
    var Password by remember {        mutableStateOf("")}
    val context= LocalContext.current
    val  mAuth = FirebaseAuth.getInstance()

Column(
    modifier = Modifier
        .fillMaxSize(1f)
        .padding(10.dp),
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

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center) {
        Button(onClick = {
            loginUserAccount(context,navController,
                user = User(username,Password),mAuth
            )
        })
        {
            Text("Login")
        }

        Spacer(modifier = Modifier.padding(20.dp))

        Button(onClick = {
            navController.navigate("register")
            Toast.makeText(context, "Register pressed", Toast.LENGTH_SHORT).show()
        }) {
            Text("Register")
        }
        Spacer(modifier = Modifier.padding(20.dp))

        Button(onClick = {
            navController.navigate("admin")

            Toast.makeText(context, "Admin pressed", Toast.LENGTH_SHORT).show()
        }) {
            Text("Admin")
        }
    }
    
}


}


private fun loginUserAccount(context: Context,
                             navController: NavHostController,
                             user: User,mAuth: FirebaseAuth) {


    // validations for input email and password
    if (TextUtils.isEmpty(user.username)) {
        Toast.makeText(
            context,
            "Please enter email!!",
            Toast.LENGTH_LONG
        )
            .show()
        return
    }
    if (TextUtils.isEmpty(user.password)) {
        Toast.makeText(
            context,
            "Please enter password!!",
            Toast.LENGTH_LONG
        )
            .show()
        return
    }

    // signin existing user
    mAuth.signInWithEmailAndPassword(user.username, user.password)
        .addOnCompleteListener(
            OnCompleteListener<AuthResult?> { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Login successful!!",
                        Toast.LENGTH_LONG
                    )
                        .show()

                    val isvoteData = IsvoteData(user.username,true)
                    val votesJson = Gson().toJson(isvoteData)

                    save_votes(context,votesJson)

                   navController.navigate("voting")
                } else {

                    // sign-in failed
                    Toast.makeText(
                        context,
                        "Login failed!!",
                        Toast.LENGTH_LONG
                    )
                        .show()
                }
            })
}


fun registerNewUser(
    context: Context,
    mAuth: FirebaseAuth,
    userRegister: UserRegister,
    navController: NavHostController
) {
    // Validations for input email and password
    if (TextUtils.isEmpty(userRegister.username)) {
        Toast.makeText(
            context,
            "Please enter username!!",
            Toast.LENGTH_LONG
        ).show()
        return
    }
    if (TextUtils.isEmpty(userRegister.password)) {
        Toast.makeText(
            context,
            "Please enter password!!",
            Toast.LENGTH_LONG
        ).show()
        return
    }
    if (TextUtils.isEmpty(userRegister.email)) {
        Toast.makeText(
            context,
            "Please enter email!!",
            Toast.LENGTH_LONG
        ).show()
        return
    }
    if (userRegister.phone == null) {
        Toast.makeText(
            context,
            "Please enter phone number!!",
            Toast.LENGTH_LONG
        ).show()
        return
    }

    // create new user or register new user
    mAuth
        .createUserWithEmailAndPassword(userRegister.email, userRegister.password)
        .addOnCompleteListener(OnCompleteListener<AuthResult?> { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    "Registration successful!",
                    Toast.LENGTH_LONG
                ).show()

                // if the user created intent to login activity
                navController.navigate("login")
            } else {
                // Registration failed
                Toast.makeText(
                    context, "Registration failed!! Please try again later",
                    Toast.LENGTH_LONG
                ).show()
            }
        })
}

data class User(
    val username:String,
    val password:String
)
data class UserRegister(
    val username:String,
    val password:String,
    val phone:String,
    val email:String
)
data class UserRegisterCheck(
    val username:String,
    val phone:String,
    val isVoted: Boolean
)



