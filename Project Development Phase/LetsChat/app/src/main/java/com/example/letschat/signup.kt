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
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Signup(navController: NavHostController) {
    if(uname!="")
        navController.navigate("contacts")
    Column(modifier = Modifier
        .fillMaxSize()
        .background(primary), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Text(text = "Sign Up Page", fontFamily = FontFamily.Cursive, fontSize = 48.sp, color = textcolor)
        Spacer(modifier = Modifier.padding(32.dp))
        var text by remember {
            mutableStateOf("")
        }
        Text(text = text, color = Color.Red)
        Spacer(modifier = Modifier.padding(4.dp))
        var name by remember {
            mutableStateOf("")
        }
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text(text = "Enter your preferred Username", color = textcolor) }, keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
        Spacer(modifier = Modifier.padding(8.dp))
        var pass1 by remember {
            mutableStateOf("")
        }
        OutlinedTextField(value = pass1, onValueChange = { pass1 = it }, label = { Text("Enter a strong Password", color = textcolor) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Next), visualTransformation = PasswordVisualTransformation(), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
        Spacer(modifier = Modifier.padding(8.dp))
        var pass2 by remember {
            mutableStateOf("")
        }
        var creating by remember {
        mutableStateOf(false)
        }
        var i = false
        fun creating(name: String, pass: String) {
            myRef = database.getReference(name)
            myRef.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    value = snapshot.getValue<MutableList<messagedetails>>()
                    if(i) {
                        if (value == null) {
                            val sdf = SimpleDateFormat("yyyy M dd hh mm ss a")
                            val currentDate = sdf.format(Date()).split(" ")
                            val temp = messagedetails(
                                name,
                                "",
                                date(
                                    currentDate[0].toInt(),
                                    currentDate[1].toInt(),
                                    currentDate[2].toInt(),
                                    currentDate[3].toInt(),
                                    currentDate[4].toInt(),
                                    currentDate[5].toInt(),
                                    currentDate[6]
                                ),
                                "",
                                pass
                            )
                            value = mutableListOf(temp)
                            myRef.setValue(value)
                            uname = name
                            navController.navigate("contacts")
                        } else
                            text = "$name is already taken. Please give another one"
                        i = false
                    }
                    creating = false
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Not yet implemented")
                }
            })
            creating = true
        }
        fun go() {
            text = ""
            if(!creating) {
                if (name.length >= 3) {
                    if (pass1 == pass2) {
                        if (pass1.length >= 8) {
                            i = true
                            creating(name, pass1)
                        } else
                            text = "Passwords must contain at least 8 characters"
                    } else
                        text = "Passwords should match"
                } else
                    text = "Username must contain at least 3 characters"
                name = ""
                pass1 = ""
                pass2 = ""
            }
        }
        OutlinedTextField(value = pass2, onValueChange = { pass2 = it }, label = { Text("Re-enter your Password", color = textcolor) }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Go), keyboardActions = KeyboardActions( onGo= { go() } ), visualTransformation = PasswordVisualTransformation(), textStyle = LocalTextStyle.current.copy(textcolor), colors = TextFieldDefaults.outlinedTextFieldColors(focusedBorderColor = textcolor, unfocusedBorderColor = textcolor))
        Spacer(modifier = Modifier.padding(16.dp))
        Button(onClick = {
            go()
        }, colors = ButtonDefaults.buttonColors(secondary)) {
            Text(text = if(creating) "Creating.." else "Sign Up", color = textcolor)
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Row {
            Text(text = "Have an account? ", color = textcolor)
            ClickableText(text = AnnotatedString("Login here", spanStyle = SpanStyle(color = Color.Blue)), onClick = { navController.navigate("login")})
        }
    }
}