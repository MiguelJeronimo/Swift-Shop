package com.miguel.swiftshop.Views

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.google.firebase.Timestamp
import com.miguel.swiftshop.R
import com.miguel.swiftshop.Views.Components.ShoppingListComponet
import com.miguel.swiftshop.Views.ViewModels.ViewModelHome
import com.miguel.swiftshop.Views.theme.SwiftShopTheme
import com.miguel.swiftshop.data.SettingsDataStore
import com.miguel.swiftshop.models.UserList
import com.miguel.swiftshop.utils.CodeEncode
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


class ShoppingList : ComponentActivity() {
    lateinit var settingsDataStore: SettingsDataStore
    lateinit var viewModelUserList: ViewModelHome
    val shippingList = ShoppingListComponet()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingsDataStore = SettingsDataStore(context = this)
        viewModelUserList = ViewModelProvider(this)[ViewModelHome::class.java]
        setContent {
            SwiftShopTheme {
                val listDataState = remember { mutableStateOf( emptyList<UserList>()) }
                settingsDataStore.preferencesFlow.asLiveData().observe(this, Observer {
                    if (!it){
                        finish()
                    }
                })
                settingsDataStore.preferencesFlowUsers.asLiveData().observe(this, Observer {
                    if (it!=null && it.toString().isNotEmpty()){
                        viewModelUserList.userLists(it.toString())
                    }
                })

                viewModelUserList.list.observe(this, Observer {
                    if (it!=null){
                        listDataState.value=it
                    }
                })

                Scaffold(
                    topBar = { toobar() },
                    floatingActionButton = { FloatButton() },
                    bottomBar = { BottomNavigationBar() }
                ){innerPadding->
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

    override fun onBackPressed() {
        super.onBackPressed()
        println("backPResed")
        finishAffinity(); // Termina la pila de actividades
    }
    data class ListProduct(val nameList: String, val date: String)

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun toobar(){
        val textStle = androidx.compose.ui.text.TextStyle(
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Row (Modifier.fillMaxWidth(1f)){
            Text(
                modifier = Modifier
                    .padding(15.dp,10.dp,0.dp,0.dp),
                text = "Swift Shop",
                color = MaterialTheme.colorScheme.secondary,
                style = textStle
            )
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.End
            ){
                DropDownMenu()
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
                            BadgeBoxCountOrder("10,000",icon = icons[index])
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
    fun FloatButton() {
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
                        Toast.makeText(contextForToast, "¡Suscrito😎!", Toast.LENGTH_SHORT).show()
                        expanded = false
                        rotation = 0.0F
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Favorite,
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
                            Icons.Outlined.Favorite,
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
        listDataState.value = list
        SwiftShopTheme {
            Scaffold(
                topBar = { toobar() },
                floatingActionButton = { FloatButton() },
                bottomBar = { BottomNavigationBar() }
            ) { innerPadding ->
                println(innerPadding)
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

