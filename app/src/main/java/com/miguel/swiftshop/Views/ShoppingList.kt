package com.miguel.swiftshop.Views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.miguel.swiftshop.Views.Components.AlertDialogCustom
import com.miguel.swiftshop.Views.Components.ShoppingListComponet
import com.miguel.swiftshop.Views.ViewModels.ViewModelHome
import com.miguel.swiftshop.Views.theme.SwiftShopTheme
import com.miguel.swiftshop.data.SettingsDataStore
import com.miguel.swiftshop.models.UserList
import kotlinx.coroutines.launch


class ShoppingList : ComponentActivity() {
    lateinit var settingsDataStore: SettingsDataStore
    lateinit var viewModelUserList: ViewModelHome
    lateinit var alertDialogCustom: AlertDialogCustom
    lateinit var stateProgressBar:  MutableState<Boolean>
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settingsDataStore = SettingsDataStore(context = this)
        viewModelUserList = ViewModelProvider(this)[ViewModelHome::class.java]
        alertDialogCustom = AlertDialogCustom(viewModelUserList)
        setContent {
            SwiftShopTheme {
                val listDataState = remember { mutableStateOf( emptyList<UserList>()) }
                val alertDialogState = remember { mutableStateOf(false) }
                val stateIDCollection = remember { mutableStateOf("") }
                val stateDeleteButton = remember { mutableStateOf(false) }
                stateProgressBar = remember { mutableStateOf(false) }
                val stateDataUser = remember { mutableStateOf("")}
                val stateNextActivity = remember { mutableStateOf(false) }
                val shippingList = ShoppingListComponet(stateDeleteButton, viewModelUserList)
                viewModelUserList.stateNextActivity.observe(this, Observer {
                    if (it){
                        //Intent(applicationContext, )
                    }
                })
                settingsDataStore.preferencesFlow.asLiveData().observe(this, Observer {
                    if (!it){
                        finish()
                    }
                })
                viewModelUserList.dataUserState.observe(this, Observer {
                    stateDataUser.value = it.count.toString()
                })
                viewModelUserList.delete.observe(this, Observer {
                    val array = viewModelUserList.dataUserState.value?.idDocuments
                    if (array == null){
                        stateProgressBar.value = false
                    }
                })
                settingsDataStore.preferencesFlowUsers.asLiveData().observe(this, Observer {
                    if (it!=null && it.toString().isNotEmpty()) {
                        stateIDCollection.value = it.toString()
                        stateProgressBar.value = true
                        viewModelUserList.userLists(it.toString())
                    }
                })
//titulo, html descripcion, imagenes,
                viewModelUserList.list.observe(this, Observer {
                    if (it!=null){
                        listDataState.value=it
                    }
                    stateProgressBar.value = false
                })

                viewModelUserList.insertList.observe(this, Observer {
                    if (it){
                        Toast.makeText(this, "Se agrego correctamente la lista", Toast.LENGTH_SHORT).show()
                    } else{
                        Toast.makeText(this, "No se pudo ingresar su lista, intente mas tarde", Toast.LENGTH_SHORT).show()
                    }
                })

                Scaffold(
                    topBar = { toobar(stateDeleteButton,stateDataUser,stateIDCollection)},
                    floatingActionButton = { FloatButton(alertDialogState) },
                    bottomBar = { BottomNavigationBar() }
                ){innerPadding->
                    if (stateProgressBar.value){
                        IndeterminateIndicator()
                    }
                    Column(
                        modifier = Modifier
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.spacedBy(5.dp),
                    ) {
                        if (alertDialogState.value){
                            alertDialogCustom.AlertDialogAdd(alertDialogState, stateIDCollection.value)
                        }
                        Divider()
                        shippingList.ShoppingList(listDataState)
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity(); // Termina la pila de actividades
    }
    data class ListProduct(val nameList: String, val date: String)
    @Composable
    fun IndeterminateIndicator() {
        LinearProgressIndicator(
            Modifier.fillMaxWidth()
        )
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun toobar(
        stateDeleteButton: MutableState<Boolean>?,
        stateDataUser: MutableState<String>?,
        stateIDCollection: MutableState<String>?
    ) {
        val textStle = androidx.compose.ui.text.TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Row (Modifier.fillMaxWidth(1f)){
            if (!stateDeleteButton!!.value){
                Text(
                    modifier = Modifier
                        .padding(15.dp,10.dp,0.dp,0.dp),
                    text = "Swift Shop",
                    color = MaterialTheme.colorScheme.secondary,
                    style = textStle
                )
            } else{
                Backbutton(stateDeleteButton)
                Text(
                    text = stateDataUser!!.value,
                    modifier = Modifier
                        .padding(5.dp,10.dp,0.dp,0.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    style = textStle
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.End
            ){
                //DropDownMenu()
                if (!stateDeleteButton.value){
                    DropDownMenu()

                } else {
                    DeleteButton(stateDeleteButton, stateIDCollection)
                }

            }
        }
    }

    @Composable
    fun Backbutton(stateDeleteButton: MutableState<Boolean>) {
        Box {
            IconButton(onClick = {
                stateDeleteButton.value = false
                viewModelUserList.stateDataUser(0, null)
            }) {
                Icon(Icons.Default.ArrowBack , contentDescription = "delete")
            }
        }
    }

    @Composable
    fun DeleteButton(
        stateDeleteButton: MutableState<Boolean>,
        stateIDCollection: MutableState<String>?
    ) {
        Box {
            IconButton(onClick = {
                stateDeleteButton.value = false
                val idsDocumentList = viewModelUserList.dataUserState.value?.idDocuments
                stateProgressBar.value = true
                idsDocumentList?.forEach {
                    viewModelUserList.delete(stateIDCollection?.value,it)
                }
                viewModelUserList.stateDataUser(0, null)
            }) {
                Icon(Icons.Default.Delete , contentDescription = "delete")
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    @Composable
    fun DropDownMenu(){
        var expanded by remember { mutableStateOf(false) }
        val contextForToast = LocalContext.current.applicationContext
        Box {
            IconButton(onClick = { expanded = true}) {
                Icon(Icons.Default.MoreVert , contentDescription = "Open Menu")
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    DropdownMenuItem(text = {
                        Text("Agregar nueva lista")
                    },
                        onClick = {
                            expanded = false
                        })
                    DropdownMenuItem(text = {
                        Text("Acerca de")
                    },
                        onClick = {
                            expanded = false
                        }
                    )
                    DropdownMenuItem(text = {
                        Text("Cerrar sesión")
                    },
                        onClick = {
                            lifecycleScope.launch {
                                settingsDataStore.saveUserPreferences(null, applicationContext)
                                settingsDataStore.saveStatusloginPreferences(false,applicationContext)
                            }
                            expanded = false
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun BottomNavigationBar(){
        var selectedItem by remember { mutableIntStateOf(0) }
        val items = listOf("Listas", "Pedidos")
        val icons = listOf(
            Icons.Filled.Home,
            Icons.Filled.ShoppingCart,
        )
        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(icons[index], contentDescription = item)
                        //BadgeBoxCountOrder()
                        if (index == 1){
                            BadgeBoxCountOrder("0",icon = icons[index])
                        }
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = { selectedItem = index }
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun BadgeBoxCountOrder(text:String, icon: ImageVector){
        BadgedBox(badge = { Badge { Text(text) } }) {
            Icon(
                icon,
                contentDescription = "Favorite"
            )
        }
    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    fun FloatButton(alertDialogState: MutableState<Boolean>?) {
        var rotation by remember { mutableStateOf(0f) }
        var expanded by remember { mutableStateOf(false) }
        val contextForToast = LocalContext.current.applicationContext
        FloatingActionButton(
            onClick = {
                if (rotation == 45.0.toFloat()){
                    expanded = false
                    rotation = 0F
                } else{
                    rotation =  45F
                    expanded = true
                }
            },
            modifier = Modifier.rotate(rotation)
            //icon = { Icon(Icons.Filled.Edit, "Agregar lista") },
            //text = { Text(text = "Agregar lista") },
        ){
            Icon(Icons.Filled.Add, "Agregar lista")
            DropdownMenu(expanded = expanded, onDismissRequest = {
                expanded = false
                rotation = 0.0F
            }) {
                DropdownMenuItem(text = {
                    Text("Agregar Lista")
                },
                    onClick = {
                        alertDialogState?.value = true
                        expanded = false
                        rotation = 0.0F
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Add,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    })
                DropdownMenuItem(text = {
                    Text("Eliminar todas las listas")
                },
                    onClick = {
                        Toast.makeText(contextForToast, "¡Suscrito😎!", Toast.LENGTH_SHORT).show()
                        expanded = false
                        rotation = 0.0F
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Clear,
                            contentDescription = null,
                            tint = Color.Red
                        )
                    }
                )
            }
        }
    }


    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        val list = listOf(
            UserList("i1289askjdnk","Aurrera", Timestamp(1714416433,  533000000))
        )
        val listDataState = remember { mutableStateOf( emptyList<UserList>()) }
        val shippingList = ShoppingListComponet(null, null)
        listDataState.value = list
        SwiftShopTheme {
            Scaffold(
                topBar = { toobar(null, null, null) },
                floatingActionButton = { FloatButton(null) },
                bottomBar = { BottomNavigationBar() }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding),
                    verticalArrangement = Arrangement.spacedBy(5.dp),
                ) {
                    Divider()
                    shippingList.ShoppingList(listDataState)
                }
            }
        }
    }
}

