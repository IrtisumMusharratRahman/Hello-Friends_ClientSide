package com.example.hellofriends.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.hellofriends.Greeting

@Composable
fun Navigation(getNavController: (navController: NavController)-> Unit) {
    val navController = rememberNavController()
    getNavController(navController)

    NavHost(navController = navController, startDestination = NavScreens.ChatScreen.route ){

        composable(route = NavScreens.ChatScreen.route){
            Greeting("Android")
        }

    }
}