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
import com.miguel.swiftshop.models.UserData
import com.miguel.swiftshop.utils.CodeEncode
import kotlinx.coroutines.launch
import java.util.UUID
import android.util.Base64
import com.miguel.swiftshop.models.UserDataInsertModel

class MainActivity : ComponentActivity() {
    lateinit var stateForms: MutableState<Int>
    lateinit var isError: MutableState<Boolean>
    lateinit var typeError: MutableState<Int>
    lateinit var viewModelLogin: ViewModelLogin
    lateinit var settingsDataStore: SettingsDataStore
    lateinit var uuii: UUID
    val codeDecode = CodeEncode()
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsDataStore = SettingsDataStore(context = this)
        viewModelLogin = ViewModelProvider(this)[ViewModelLogin::class.java]
        uuii = UUID.randomUUID()
        val public_key = resources.openRawResource(R.raw.publickey)
        val publicKey = codeDecode.readKey(public_key)
        val private_key = resources.openRawResource(R.raw.privatekey)
        val privateKey = codeDecode.readKey(private_key)
        //FirebaseApp.initializeApp(applicationContext)
        setContent {
            SwiftShopTheme {
                stateForms = remember { mutableStateOf(2) }
                isError= remember { mutableStateOf(false) }
                typeError = remember{ mutableStateOf(0) }
                val userDataState = remember { mutableStateOf(UserData(null, null, null,null,null)) }
                viewModelLogin.login.observe(this, Observer {
                    stateForms.value = it
                })
                settingsDataStore.preferencesFlow.asLiveData().observe(this, Observer {
                    if (it){
                        Intent(applicationContext, ShoppingList::class.java).also {
                            startActivity(it)
                        }
                    }
                })

                viewModelLogin.user.observe(this, Observer {
                    if(it != null){
                        println("DATA USER $it")
                        val userData = UserData(
                            it.name,
                            it.apellidos,
                            it.email,
                            null,
                            it.idCollection,
                        )
                        userDataState.value = userData
                        println("USERSTATE $userDataState")
                        viewModelLogin.stateLogin(3)
                    }
                })
                Scaffold(topBar = { DropDownMenu(viewModelLogin) }){
                    Column(
                        Modifier.verticalScroll(rememberScrollState())
                    ) {
                        when(stateForms.value){
                            0-> Login(viewModelLogin, isError,typeError,privateKey)
                            1-> {UserRegisterForm(viewModelLogin, isError,typeError, publicKey)}
                            3->{
                                //go to darshboard
                                lifecycleScope.launch {
                                    settingsDataStore.saveUserPreferences(userDataState.value.idCollection.toString(),applicationContext)
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
fun Login(
    stateForms: ViewModelLogin?,
    isError: MutableState<Boolean>?,
    typeError: MutableState<Int>?,
    privateKey: String?
) {
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
            TextField("email",textStateEmail,null,0, isError!!)
            TextField("Password",textStatePassword,null,1,isError)
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
                    stateForms?.user(textStateEmail.value, textStatePassword.value, privateKey)
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
fun UserRegisterForm(
    viewModelLogin: ViewModelLogin?,
    isError: MutableState<Boolean>?,
    typeError: MutableState<Int>?,
    publicKey: String?
){
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
            val textStateConfirmPassword = remember { mutableStateOf("") }
            val textStateConfirmPassword2 = remember { mutableStateOf("") }
            val isError= remember { mutableStateOf(false) }
            val isEmailInvalid = remember{ mutableStateOf(false) }
            val isErrorEqualPassword = remember { mutableStateOf(false) }
            val isErrorEqualPassword2 = remember { mutableStateOf(false) }
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
            TextField("Nombre",textStateName,null,0,isError)
            TextField("Apellidos",textStateSecondName,null,0,isError)
            TextField("email",textStateEmail,null,0,isEmailInvalid)
            TextField("Password",textStateConfirmPassword,null,1,isErrorEqualPassword)
            TextField("confirmar password",textStateConfirmPassword2,textStateConfirmPassword,1,isErrorEqualPassword2)
            when(typeError.value){
                1->{TextError(message = "Ingrese los campos solicitados")}
                2->{TextError(message = "Email invalido, ejemplo: email@gmail.com")}
                3->{TextError(message = "Las contraseñas no son iguales")}
            }
            Button(onClick = {
                val emailRegex = Regex("^[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\$")
                if (
                    textStateName.value.isEmpty()&&
                    textStateSecondName.value.isEmpty()&&
                    textStateEmail.value.isEmpty()&&
                    textStateConfirmPassword.value.isEmpty()&&
                    textStateConfirmPassword2.value.isEmpty()
                ){
                    isError.value = true
                    isEmailInvalid.value = true
                    isErrorEqualPassword.value = true
                    isErrorEqualPassword2.value = true
                    typeError.value = 1
                } else if (!emailRegex.matches(textStateEmail.value)){
                        isError.value = false
                        isEmailInvalid.value = true
                        isErrorEqualPassword.value = false
                        isErrorEqualPassword2.value = false
                        typeError.value = 2
                }else if(textStateConfirmPassword.value != textStateConfirmPassword2.value){
                    isError.value = false
                    isEmailInvalid.value = false
                    isErrorEqualPassword.value = true
                    isErrorEqualPassword2.value = true
                    typeError.value = 3
                }
                else{
                    isError.value = false
                    isEmailInvalid.value = false
                    isErrorEqualPassword.value = false
                    isErrorEqualPassword2.value = false
                    typeError.value = 0
                    //encriptdata
                    val encripted = CodeEncode().encryptData(publicKey, textStateConfirmPassword.value)
                    val pass = Base64.encodeToString(encripted, Base64.DEFAULT)
                    val userData = UserDataInsertModel(
                        UUID.randomUUID().toString(),
                        textStateName.value,
                        textStateSecondName.value,
                        textStateEmail.value,
                        pass,
                    )
                    viewModelLogin?.userRegistry(userData)
                    if (viewModelLogin?.userData?.value == true){

                    }
                }
            },
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
fun TextField(label: String, text: MutableState<String>,text2: MutableState<String>?, inputType: Int, isError: MutableState<Boolean>){
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
                modifier = Modifier
                    .fillMaxWidth(1F)
                    .padding(10.dp, 0.dp, 10.dp, 0.dp),
                value = text.value.trim(),
                onValueChange = {
                    text.value = it
                },
                singleLine = true,
                isError = if (isError.value) true else false,
                trailingIcon = {
                    Icon(imageVector = Icons.Default.CheckCircle, contentDescription = "")
                },
                visualTransformation = PasswordVisualTransformation(),
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
                UserRegisterForm(null, null, null, null)
            }
        }
    }
}

