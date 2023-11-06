package com.example.letschat

import android.os.Handler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(navController: NavHostController) {
    if(uname=="")
        navController.navigate("login")
    var chats by remember {
        mutableStateOf(mutableListOf<messagedetails>())
    }
    var temp = mutableListOf<messagedetails>()
    for(x in value!!) {
        if(uname==x.from&&sname==x.to||uname==x.to&&sname==x.from)
            temp.add(x)
    }
    chats = temp
    Column (modifier = Modifier
        .fillMaxSize()
        .background(color = primary)) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .background(color = secondary)) {
            IconButton(onClick = { navController.navigate("contacts") }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "go back", tint = textcolor)
            }
            Text(text = sname, modifier = Modifier.padding(4.dp), fontSize = 24.sp, color = textcolor)
        }
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(chats?.toList()!!) {
                if(it.to==sname) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.Bottom) {
                        Text(text = it.date.day.toString()+"/"+it.date.month.toString()+"/"+it.date.year.toString()+" "+it.date.hour.toString()+":"+it.date.minute.toString()+" "+it.date.meridiem, color = Color.Gray, fontSize = 12.sp)
                        if(it.message.length>30) {
                            Text(
                                text = it.message, color = textcolor, modifier = Modifier
                                    .background(
                                        color = if (Theme == "light") Color.White else Color.Black,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(16.dp, 4.dp)
                                    .weight(1f)
                            )
                        } else {
                            Text(
                                text = it.message, color = textcolor, modifier = Modifier
                                    .background(
                                        color = if (Theme == "light") Color.White else Color.Black,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .padding(16.dp, 4.dp)
                            )
                        }
                    }
                } else {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp), horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.Bottom) {
                        if(it.message.length>30) {
                            Text(
                                text = it.message, color = textcolor, modifier = Modifier
                                    .background(color = primary2, shape = RoundedCornerShape(8.dp))
                                    .padding(16.dp, 4.dp)
                                    .weight(1f)
                            )
                        } else {
                            Text(
                                text = it.message, color = textcolor, modifier = Modifier
                                    .background(color = primary2, shape = RoundedCornerShape(8.dp))
                                    .padding(16.dp, 4.dp)
                            )
                        }
                        Text(text = it.date.day.toString()+"/"+it.date.month.toString()+"/"+it.date.year.toString()+" "+it.date.hour.toString()+":"+it.date.minute.toString()+" "+it.date.meridiem, color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically) {
            var msg by remember {
                mutableStateOf("")
            }
            TextField(
                value = msg,
                onValueChange = { msg = it },
                label = { Text("Enter your message here", color = textcolor) },
                textStyle = LocalTextStyle.current.copy(textcolor),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = textcolor,
                    unfocusedBorderColor = textcolor
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions( onSend = { send(msg); msg = "" } ),
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                send(msg)
                msg = ""
            }, modifier = Modifier.background(color = secondary, shape = RoundedCornerShape(16.dp))) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = textcolor)
            }
        }
    }
    fun loop() {
        val handler = Handler()
        handler.postDelayed({
            temp = mutableListOf()
            for (x in value!!) {
                if (uname == x.from && sname == x.to || uname == x.to && sname == x.from)
                    temp.add(x)
            }
            chats = temp
            loop()
        }, 100)
    }
    loop()
}

fun send(msg: String) {
    val sdf = SimpleDateFormat("yyyy M dd hh mm ss a")
    val currentDate = sdf.format(Date()).split(" ")
    if(msg!="") {
        val temp = messagedetails(
            uname,
            sname,
            date(
                currentDate[0].toInt(),
                currentDate[1].toInt(),
                currentDate[2].toInt(),
                currentDate[3].toInt(),
                currentDate[4].toInt(),
                currentDate[5].toInt(),
                currentDate[6]
            ),
            msg
        )
        value?.add(temp)
        bgvalue?.add(temp)
        myRef.setValue(value)
        othersRef.setValue(bgvalue)
    }
}