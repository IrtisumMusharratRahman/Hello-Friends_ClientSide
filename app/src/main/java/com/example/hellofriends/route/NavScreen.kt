package com.example.hellofriends.route

sealed class NavScreens(val route:String){

    object ChatScreen: NavScreens("chatScreen")

}