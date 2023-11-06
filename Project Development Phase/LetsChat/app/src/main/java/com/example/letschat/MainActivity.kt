package com.example.letschat

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import com.example.letschat.ui.theme.LetsChatTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue

val database = Firebase.database
var uname: String = ""
var myRef: DatabaseReference = database.getReference(uname)
var othersRef: DatabaseReference = database.getReference("")
var value: MutableList<messagedetails>? = mutableListOf(messagedetails())
var bgvalue: MutableList<messagedetails>? = mutableListOf(messagedetails())

var Theme = "light"
var primary = Color(0xFFFFF0FF)
var primary2 = Color(0xFFFFE0FF)
var secondary = Color(0xFFFFC0FF)
var secondary2 = Color(0xFFFFA0FF)
var textcolor = Color.Black

fun light() {
    Theme = "light"
    primary = Color(0xFFFFF0FF)
    primary2 = Color(0xFFFFE0FF)
    secondary = Color(0xFFFFC0FF)
    secondary2 = Color(0xFFFFA0FF)
    textcolor = Color.Black
}

fun dark() {
    Theme = "dark"
    primary = Color(0xFFCC80CC)
    primary2 = Color(0xFFDDA0DD)
    secondary = Color(0xFF883088)
    secondary2 = Color(0xFFAA40AA)
    textcolor = Color.White
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = applicationContext
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        builder.detectFileUriExposure()
        setContent {
            LetsChatTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = if(uname!="") "contacts" else "login"
                ) {
                    composable("login") {
                        Login(navController)
                    }
                    composable("contacts") {
                        Contacts(navController, context)
                    }
                    composable("chat") {
                        Chat(navController)
                    }
                    composable("signup") {
                        Signup(navController)
                    }
                    composable("chpass") {
                        ChPass(navController)
                    }
                    composable("about") {
                        About(navController)
                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        val sharedPreferences = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        uname = sharedPreferences.getString("uname","").toString()
        Theme = sharedPreferences.getString("theme","light").toString()
        if(Theme == "light")
            light()
        else
            dark()
        if(uname!="") {
            myRef = database.getReference(uname)
            myRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    value = snapshot.getValue<MutableList<messagedetails>>()
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Not yet implemented")
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        val sharedPreferences = getSharedPreferences("my_app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("uname", uname)
        editor.putString("theme", Theme)
        editor.apply()
    }
}

data class date(val year: Int = 0, val month: Int = 0, val day: Int = 0, val hour: Int = 0, val minute: Int = 0, val second: Int = 0, val meridiem: String = "")
data class messagedetails(val from: String = "", val to: String = "", val date: date = date(), val message: String = "", var pass: String = "")
data class navigationItem(val title:String, val icon: Painter, val event: ()->Unit, val badge: String = "")