package com.example.letschat

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch
import java.io.File

var sname = ""
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Contacts(navController: NavHostController, context: Context) {
    if(uname=="")
        navController.navigate("login")
    var contacts by remember {
        mutableStateOf(mutableListOf<String>())
    }
    var temp = mutableListOf<String>()
    for (x in value!!) {
        if(x.to!=""&&x.from!="") {
            if(x.from==uname) {
                if (temp.contains(x.to))
                    temp.remove(x.to)
                temp.add(0, x.to)
            } else {
                if (temp.contains(x.from))
                    temp.remove(x.from)
                temp.add(0, x.from)
            }
        }
    }
    contacts = temp
    val drawerstate = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(
        navigationItem(
            title = "Change Password",
            icon = painterResource(id = R.drawable.changepassword),
            event = {
                scope.launch {
                    drawerstate.close()
                    navController.navigate("chpass")
                }
            }
        ),
        navigationItem(
            title = "Theme",
            icon = painterResource(id = R.drawable.theme),
            event = {
                if(Theme == "light")
                    dark()
                else
                    light()
                scope.launch {
                    drawerstate.close()
                    navController.navigate("contacts")
                }
            },
            badge = Theme
        ),
        navigationItem(
            title = "Share",
            icon = painterResource(id = R.drawable.share),
            event = {
                scope.launch {
                    drawerstate.close()
                    val file = File(context.applicationInfo.sourceDir)
                    val intent = Intent(Intent.ACTION_SEND)
                    intent.type = "application/vnd.android.package-archive"
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file))
                    context.startActivity(Intent.createChooser(intent, "Share via").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
                }
            }
        ),
        navigationItem(
            title = "About",
            icon = painterResource(id = R.drawable.info),
            event = {
                scope.launch {
                    drawerstate.close()
                    navController.navigate("about")
                }
            }
        )
    )
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp), horizontalArrangement = Arrangement.Center) {
                    Text(
                        text = uname,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
                items.forEach { item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = item.event,
                        icon = { Image(painter = item.icon, contentDescription = item.title, modifier = Modifier
                            .width(24.dp)
                            .height(24.dp)) },
                        badge = { Text(text = item.badge) }
                    )
                }
            }
        },
        drawerState = drawerstate
    ) {
        var add by remember {
            mutableStateOf(false)
        }
        Column (modifier = Modifier
            .fillMaxSize()
            .background(color = primary), horizontalAlignment = Alignment.CenterHorizontally) {
            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color = secondary)
                .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Row {
                    IconButton(onClick = {
                        scope.launch {
                            drawerstate.open()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Navigation Drawer Icon",
                            tint = textcolor
                        )
                    }
                    Spacer(modifier = Modifier.padding(4.dp, 0.dp, 4.dp, 0.dp))
                    Text(
                        text = "Letzchat",
                        color = secondary2,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row {
                    /*Image(
                        painter = painterResource(id = if(Theme == "light") R.drawable.reload_light else R.drawable.reload_dark),
                        contentDescription = "add people",
                        modifier = Modifier
                            .clickable {
                                navController.navigate("contacts")
                            }
                            .height(24.dp)
                            .width(24.dp))
                    Spacer(modifier = Modifier.padding(4.dp, 0.dp, 4.dp, 0.dp))*/
                    Image(
                        painter = painterResource(id = if(Theme == "light") R.drawable.addperson_black else R.drawable.addperson_white),
                        contentDescription = "add people",
                        modifier = Modifier
                            .clickable {
                                add = !add
                            }
                            .height(24.dp)
                            .width(24.dp))
                    Spacer(modifier = Modifier.padding(4.dp, 0.dp, 4.dp, 0.dp))
                    Image(
                        painter = painterResource(id = if(Theme == "light") R.drawable.logout_black else R.drawable.logout_white),
                        contentDescription = "Logout",
                        modifier = Modifier
                            .clickable {
                                uname = ""
                                navController.navigate("login")
                            }
                            .height(24.dp)
                            .width(24.dp)
                    )
                }
            }
            if(add) {
                var name by remember {
                    mutableStateOf("")
                }
                var text by remember {
                    mutableStateOf("")
                }
                var i: Boolean
                Text(text = text, color = Color.Red)
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("receiver's username", color = textcolor) },
                    textStyle = LocalTextStyle.current.copy(textcolor),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = textcolor,
                        unfocusedBorderColor = textcolor),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Go",
                            tint = textcolor,
                            modifier = Modifier.clickable {
                                if(name.length>=3) {
                                    i = true
                                    othersRef = database.getReference(name)
                                    othersRef.addValueEventListener(object : ValueEventListener {

                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            bgvalue = snapshot.getValue<MutableList<messagedetails>>()
                                            if(i) {
                                                if (bgvalue != null) {
                                                    sname = name
                                                    navController.navigate("chat")
                                                } else
                                                    text = "No person with that username"
                                                i = false
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            println("Not yet implemented")
                                        }
                                    })
                                } else
                                    text = "Username must contain atleast 3 characters"
                            }
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.padding(8.dp))
            var j: Boolean
            contacts.forEach { s ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        j = true
                        sname = s
                        othersRef = database.getReference(sname)
                        othersRef.addValueEventListener(object : ValueEventListener {

                            override fun onDataChange(snapshot: DataSnapshot) {
                                bgvalue = snapshot.getValue<MutableList<messagedetails>>()
                                if (j) {
                                    navController.navigate("chat")
                                    j = false
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                println("Not yet implemented")
                            }
                        })
                    },
                    colors = CardDefaults.cardColors(primary2)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), horizontalArrangement = Arrangement.Center) {
                        Text(text = s, color = textcolor, fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.padding(4.dp))
            }
        }
    }
    fun loop() {
        val handler = Handler()
        handler.postDelayed({
            temp = mutableListOf()
            for (x in value!!) {
                if (x.to != "" && x.from != "") {
                    if (x.from == uname) {
                        if (temp.contains(x.to))
                            temp.remove(x.to)
                        temp.add(0, x.to)
                    } else {
                        if (temp.contains(x.from))
                            temp.remove(x.from)
                        temp.add(0, x.from)
                    }
                }
            }
            contacts = temp
            loop()
        }, 500)
    }
    loop()
}