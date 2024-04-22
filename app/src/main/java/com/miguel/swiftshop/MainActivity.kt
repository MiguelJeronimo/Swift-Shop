package com.miguel.swiftshop

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.miguel.swiftshop.ui.theme.SwiftShopTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SwiftShopTheme {
                val list = listOf(
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024"),
                    ListProduct("Chedraui", "19/04/2024")
                )
                Scaffold{
                    Column {
                        toobar()
                        shoppingList(list)
                    }
                }
            }
        }
    }
}
data class ListProduct(val nameList: String, val date: String)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun shoppingList(list: List<ListProduct>) {
    LazyColumn(Modifier.fillMaxHeight()) {
        items(list){
            ItemList(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemList(list: ListProduct) {
    Card(
        onClick = { /* Do something */ },
        Modifier
            .fillMaxWidth()
            .size(width = 180.dp, height = 80.dp)
            .padding(5.dp)
    )
    {
        Box(
            Modifier
                .fillMaxSize()
                .padding(5.dp)) {
            Text(
                "Chedrauiiiiiiiiiii",
                Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth()
                    .wrapContentHeight(),
                fontWeight = FontWeight.Bold
            )
            Text(
                "20/02/2024",
                Modifier.align(Alignment.CenterEnd),
                fontStyle = FontStyle.Italic
            )
        }
    }
}

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
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    //painter = painterResource(id = R.drawable.photo),
//                    contentDescription = "Photo"
//                )
//            }
//            IconButton(onClick = { /*TODO*/ }) {
//                Icon(
//                    //painter = painterResource(id = R.drawable.outline_search_24),
//                    contentDescription = "Search"
//                )
//            }
            DropDownMenu()
        }
    }

    Divider(
        modifier= Modifier.padding(5.dp)
    )
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
                    Text("Nuevo grupo")
                },
                    onClick = {
                        Toast.makeText(contextForToast, "¡Suscrito😎!", Toast.LENGTH_SHORT).show()
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.Red
                        )
                    })
                DropdownMenuItem(text = {
                    Text("Nueva difusión")
                },
                    onClick = {
                        Toast.makeText(contextForToast, "¡Suscrito😎!", Toast.LENGTH_SHORT).show()
                        expanded = false
                    },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Favorite,
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.Red
                        )
                    })
            }
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
        val list = listOf(
        ListProduct("Chedraui", "19/04/2024"),
        ListProduct("Chedraui", "19/04/2024")
    )
    SwiftShopTheme {
        Scaffold{
            Column() {
                toobar()
                shoppingList(list)
            }
        }
    }
}

