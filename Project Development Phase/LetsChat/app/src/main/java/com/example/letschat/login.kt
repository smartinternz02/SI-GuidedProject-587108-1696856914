package com.example.letschat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Login(navController: NavHostController) {
    if(uname!="")
        navController.navigate("contacts")
    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = primary), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Login Page", fontFamily = FontFamily.Cursive, fontSize = 48.sp, color = textcolor)
        Spacer(modifier = Modifier.padding(32.dp))
        var text by remember {
            mutableStateOf("")
        }
        Text(text = text, color = Color.Red)
        Spacer(modifier = Modifier.padding(4.dp))
        var name by remember {
            mutableStateOf("")
        }
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(text = "Enter Username", color = textcolor)}, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
        Spacer(modifier = Modifier.padding(8.dp))
        var pass by remember {
            mutableStateOf("")
        }
        var checking by remember {
            mutableStateOf(false)
        }
        var i = false
        fun checking(name: String, pass: String) {
            myRef = database.getReference(name)
            myRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    value = snapshot.getValue<MutableList<messagedetails>>()
                    if (i) {
                        if (value != null) {
                            if (value?.get(0)?.pass == pass) {
                                uname = name
                                navController.navigate("contacts")
                            } else
                                text = "Username or Password is doesn't match"
                        } else
                            text = "Username or Password is doesn't match"
                        i = false
                    }
                    checking = false
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Not yet implemented")
                }
            })
            checking = true
        }
        fun go() {
            text = ""
            if(!checking) {
                if (name.length >= 3) {
                    if (pass.length >= 8) {
                        i = true
                        checking(name, pass)
                    } else
                        text = "Password must contain at least 8 characters"
                } else
                    text = "Username must contain at least 3 characters"
                name = ""
                pass = ""
            }
        }
        OutlinedTextField(value = pass, onValueChange = { pass = it }, label = {Text("Enter Password", color = textcolor)}, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go), keyboardActions = KeyboardActions( onGo = { go() } ), visualTransformation = PasswordVisualTransformation(), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = {
            go()
        }, colors = ButtonDefaults.buttonColors(secondary)) {
            Text(text = if(checking) "Validating.." else "Sign In", color = textcolor)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row {
            Text(text = "Doesn't have account? ", color = textcolor)
            ClickableText(text = AnnotatedString("Sign Up here", spanStyle = SpanStyle(color = Color.Blue)), onClick = { navController.navigate("signup")})
        }
    }
}