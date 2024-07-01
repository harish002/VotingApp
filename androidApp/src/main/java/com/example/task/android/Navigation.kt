package com.example.task.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.task.android.Screens.LoginScreen
import com.example.task.android.Screens.RegisterScreen
import com.example.task.android.Screens.admin
import com.example.task.android.Screens.voting

@Composable
fun NavGraph(navController:NavHostController){
    var votesList = mutableMapOf<String,Boolean>()

    Surface(modifier = Modifier.fillMaxSize()) {
            NavHost(
                navController = navController,
                startDestination = "login"
            ){
                composable("login"){
                    LoginScreen(votesList, navController)
                }
                composable("register"){
                    RegisterScreen(navController)
                }
                composable("voting"){
                    voting(navController)
                }
                composable("admin"){
                    admin()
                }
                composable("Voted Successfully"){
                    Box(Modifier.fillMaxSize(1f),
                        contentAlignment = Alignment.Center){
                        Text(text = "Voted Succefully")
                    }
                }
            }
        }

}








