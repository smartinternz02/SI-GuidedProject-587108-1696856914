package com.example.letschat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun About(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = primary)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = secondary)
            .padding(8.dp)) {
            Text(text = "About", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textcolor)
        }
        Text(text = "Letzchat:\n" +
                "\"Letzchat\" is a cutting-edge mobile application designed to enhance your daily communication with your friends. With a wide range of features and a user-friendly interface, \"Letzchat\" is the go-to app for Chatting.\n" +
                "\n" +
                "Version:\n" +
                "Current Version: 1.0\n" +
                "\n" +
                "Description:\n" +
                "\"AppName\" is a versatile and intuitive app that provides basic chatting interface. Whether you're looking for simple chatting app or user-friendly app, \"Letzchat\" has got you covered.\n" +
                "\n" +
                "Key Features:\n" +
                "You can change your account password anytime\n" +
                "You can change app themes and save it\n" +
                "You also have a internal option to share the app", color = textcolor)
    }
}