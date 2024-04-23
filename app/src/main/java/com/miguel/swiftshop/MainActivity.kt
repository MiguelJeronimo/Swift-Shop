package com.miguel.swiftshop

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.miguel.swiftshop.ui.theme.SwiftShopTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FirebaseApp.initializeApp(applicationContext)
//        val db = Firebase.firestore
//        db.collection("users")
//            .get()
//            .addOnSuccessListener { result ->
//               for (document in result){
//                   println("DATA: ${document.data}")
//               }
//            }
//            .addOnFailureListener { exception ->
//                Log.w(TAG, "Error getting documents.", exception)
//            }
        setContent {
            SwiftShopTheme {
                val stateForms = remember{ mutableStateOf(0) }
                Scaffold(topBar = { DropDownMenu(stateForms) }){
                    Column(
                        Modifier.verticalScroll(rememberScrollState())
                    ) {
                        when(stateForms.value){
                            0-> Login()
                            1-> UserRegisterForm()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Login(){
    Box(
        Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ){
        Column (
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()){
            val textStateEmail = remember { mutableStateOf("") } // Initialize the state variable
            val textStateConfirmEmail = remember { mutableStateOf("") }
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Icon App",
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(5.dp)
            )
            Text(
                text = "Bienvenido",
                Modifier.align(alignment = Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displaySmall
            )
            TextField("email",textStateEmail,0)
            TextField("Password",textStateConfirmEmail,1)
            Button(onClick = { /*TODO*/ },
                Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Iniciar sesión")
            }
        }
    }
}

@Composable
fun UserRegisterForm(){
    Box(
        Modifier
            .fillMaxSize()
            .fillMaxWidth()
    ){
        Column (
            Modifier
                .align(Alignment.Center)
                .fillMaxWidth()){
            val textStateName = remember { mutableStateOf("") } // Initialize the state variable
            val textStateSecondName = remember { mutableStateOf("") } // Initialize the state variable
            val textStateEmail = remember { mutableStateOf("") } // Initialize the state variable
            val textStateConfirmEmail = remember { mutableStateOf("") }
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Icon App",
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(5.dp)
            )
            Text(
                text = "Registro",
                Modifier.align(alignment = Alignment.CenterHorizontally),
                style = MaterialTheme.typography.displaySmall
            )
            TextField("Nombre",textStateName,0)
            TextField("Apellidos",textStateSecondName,0)
            TextField("email",textStateEmail,0)
            TextField("Password",textStateConfirmEmail,1)
            TextField("confirmar password",textStateConfirmEmail,1)
            Button(onClick = { /*TODO*/ },
                Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = "Crear cuenta")
            }
        }
    }
}

@Composable
fun TextField(label: String, text: MutableState<String>, inputType: Int){
    when(inputType){
        0->{
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(1F)
                    .padding(10.dp, 0.dp, 10.dp, 0.dp),
                label = { Text(text = label) },
                //placeholder = { Text(text = label) },
            )
        }
        1->{
            OutlinedTextField(
                value = text.value,
                onValueChange = { text.value = it },
                singleLine = true,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth(1F)
                    .padding(10.dp, 0.dp, 10.dp, 0.dp),
                label = { Text(text = label) },
                //placeholder = { Text(text = label) },
            )
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun DropDownMenu(stateForms: MutableState<Int>?) {
    var expanded by remember { mutableStateOf(false) }
    val contextForToast = LocalContext.current.applicationContext
    Row (Modifier.fillMaxWidth(1f)){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            horizontalArrangement = Arrangement.End
        ){
            Box (){
                IconButton(onClick = { expanded = true}) {
                    Icon(Icons.Default.MoreVert , contentDescription = "Open Menu")
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = {
                            Text("Iniciar sesión")
                        },
                            onClick = {
                                stateForms?.value  = 0
                                expanded = false
                            })
                        DropdownMenuItem(text = {
                            Text("Registrarse")
                        },
                            onClick = {
                                expanded = false
                                stateForms?.value  = 1
                            }
                        )
                    }
                }
            }
        }
    }

}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SwiftShopTheme {
        Scaffold(topBar = { DropDownMenu(null) }){
            Column {
                Login()
                //UserRegisterForm()
            }
        }
    }
}

