package com.example.letschat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChPass(navController: NavHostController) {
Column(modifier = Modifier
    .fillMaxSize()
    .background(primary), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
    Text(text = "Change Password", fontFamily = FontFamily.Cursive, fontSize = 48.sp, color = textcolor)
    Spacer(modifier = Modifier.padding(32.dp))
    var text by remember {
        mutableStateOf("")
    }
    Text(text = text, color = Color.Red)
    Spacer(modifier = Modifier.padding(4.dp))
    var oldpass by remember {
        mutableStateOf("")
    }
    OutlinedTextField(value = oldpass, onValueChange = { oldpass = it }, label = { Text("Enter your old Password", color = textcolor) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next), visualTransformation = PasswordVisualTransformation(), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
    Spacer(modifier = Modifier.padding(8.dp))
    var newpass1 by remember {
        mutableStateOf("")
    }
    OutlinedTextField(value = newpass1, onValueChange = { newpass1 = it }, label = { Text("Enter your new Password", color = textcolor) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next), visualTransformation = PasswordVisualTransformation(), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
    Spacer(modifier = Modifier.padding(8.dp))
    var newpass2 by remember {
        mutableStateOf("")
    }
    fun changing(old: String, new: String) {
        if(old == value?.get(0)?.pass){
            val temp = value?.get(0)!!
            value?.removeAt(0)
            temp.pass = new
            value?.add(temp)
            myRef.setValue(value)
            text = "Password has changed"
        } else
            text = "Password is incorrect"
    }
    fun go() {
        text = ""
        if(oldpass.length>=8&&newpass1.length>=8&&newpass2.length>=8){
            if(newpass1==newpass2) {
                changing(oldpass, newpass1)
            } else
                text = "Both new passwords aren't matching"
        } else
            text = "Password must contain at least 8 characters"
        oldpass = ""
        newpass1 = ""
        newpass2 = ""
    }
    OutlinedTextField(value = newpass2, onValueChange = { newpass2 = it }, label = { Text("Re-enter your new Password", color = textcolor) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go), keyboardActions = KeyboardActions( onGo = { go() } ), visualTransformation = PasswordVisualTransformation(), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
    Spacer(modifier = Modifier.padding(16.dp))
    Button(onClick = { go() }, colors = ButtonDefaults.buttonColors(secondary)) {
        Text(text = "Change", color = textcolor)
    }
}
}