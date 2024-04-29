package com.miguel.swiftshop

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.miguel.swiftshop.Views.ShoppingList
import com.miguel.swiftshop.Views.ViewModels.ViewModelLogin
import com.miguel.swiftshop.Views.theme.SwiftShopTheme
import com.miguel.swiftshop.data.SettingsDataStore
import com.miguel.swiftshop.models.User
import com.miguel.swiftshop.models.UserData
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {
    lateinit var stateForms: MutableState<Int>
    lateinit var isError: MutableState<Boolean>
    lateinit var typeError: MutableState<Int>
    lateinit var viewModelLogin: ViewModelLogin
    lateinit var settingsDataStore: SettingsDataStore
    lateinit var uuii: UUID
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsDataStore = SettingsDataStore(context = this)
        viewModelLogin = ViewModelProvider(this)[ViewModelLogin::class.java]
        uuii = UUID.randomUUID()
        println(uuii)
        //FirebaseApp.initializeApp(applicationContext)
        setContent {
            SwiftShopTheme {
                stateForms = remember { mutableStateOf(2) }
                isError= remember { mutableStateOf(false) }
                typeError = remember{ mutableStateOf(0) }
                val userDataState = remember { mutableStateOf(UserData(null, null, null,null)) }
                viewModelLogin.login.observe(this, Observer {
                    stateForms.value = it
                })
                settingsDataStore.preferencesFlow.asLiveData().observe(this, Observer {
                    if (it){
                        Intent(applicationContext, ShoppingList::class.java).also {
                            it.putExtra("nameUser",viewModelLogin.user.value?.name)
                            it.putExtra("secondNameUser",viewModelLogin.user.value?.apellidos)
                            it.putExtra("emailUser",viewModelLogin.user.value?.email)
                            it.putExtra("idCollection",viewModelLogin.user.value?.idCollection)
                            startActivity(it)
                        }
                    }
                })
                viewModelLogin.user.observe(this, Observer {
                    if(it != null){
                        val userData = UserData(
                            it.name,
                            it.apellidos,
                            it.email,
                            it.idCollection
                        )
                        userDataState.value = userData
                        viewModelLogin.stateLogin(3)
                    }
                })
                Scaffold(topBar = { DropDownMenu(viewModelLogin) }){
                    Column(
                        Modifier.verticalScroll(rememberScrollState())
                    ) {
                        when(stateForms.value){
                            0-> Login(viewModelLogin, isError,typeError)
                            1-> {UserRegisterForm()}
                            3->{
                                //go to darshboard
                                lifecycleScope.launch {
                                    settingsDataStore.saveUserPreferences(userDataState.value.idCollection,applicationContext)
                                    settingsDataStore.saveStatusloginPreferences(true,applicationContext)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        println("ONRESUME")
        viewModelLogin.stateLogin(0)

    }
}


@Composable
fun Login(stateForms: ViewModelLogin?, isError: MutableState<Boolean>?, typeError: MutableState<Int>?) {
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
            val textStatePassword = remember { mutableStateOf("") }
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
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
            TextField("email",textStateEmail,0, isError!!)
            TextField("Password",textStatePassword,1,isError)
            when(typeError?.value){
                1->{TextError(message = "Ingresa tu usuario y contraseña")}
                2->{TextError(message = "Usuario y contraseña no validos")}
            }
            Button(onClick = {
                if (textStateEmail.value.isEmpty() && textStatePassword.value.isEmpty()){
                    isError.value = true
                    typeError?.value = 1
                } else{
                    isError.value = false
                    typeError?.value = 0
                    stateForms?.user(textStateEmail.value, textStatePassword.value)
                }
            },
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
            val isError= remember { mutableStateOf(false) }
            val typeError = remember{ mutableStateOf(0) }
            Image(
                painter = painterResource(id = R.mipmap.ic_launcher_foreground),
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
            TextField("Nombre",textStateName,0,isError)
            TextField("Apellidos",textStateSecondName,0,isError)
            TextField("email",textStateEmail,0,isError)
            TextField("Password",textStateConfirmEmail,1,isError)
            TextField("confirmar password",textStateConfirmEmail,1,isError)
            when(typeError.value){
                1->{TextError(message = "Ingresa tu usuario y contraseña")}
                2->{TextError(message = "Usuario y contraseña no validos")}
            }
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
fun TextError(message:String){
    Text(
        text = message,
        Modifier.padding(10.dp, 0.dp, 0.dp, 0.dp),
        color = MaterialTheme.colorScheme.error
    )
}

@Composable
fun TextField(label: String, text: MutableState<String>, inputType: Int, isError: MutableState<Boolean>){
    when(inputType){
        0->{
            OutlinedTextField(
                value = text.value.trim(),
                onValueChange = { text.value = it },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth(1F)
                    .padding(10.dp, 0.dp, 10.dp, 0.dp),
                label = { Text(text = label) },
                isError = if(isError.value) true else false
                //placeholder = { Text(text = label) },
            )
        }
        1->{
            OutlinedTextField(
                value = text.value.trim(),
                onValueChange = { text.value = it },
                singleLine = true,
                isError = if(isError.value) true else false,
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
fun DropDownMenu(stateForms: ViewModelLogin?) {
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
                                stateForms?.stateLogin(0)
                                expanded = false
                            })
                        DropdownMenuItem(text = {
                            Text("Registrarse")
                        },
                            onClick = {
                                expanded = false
                                stateForms?.stateLogin(1)
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
                //Login(null, null, null)
                UserRegisterForm()
            }
        }
    }
}

