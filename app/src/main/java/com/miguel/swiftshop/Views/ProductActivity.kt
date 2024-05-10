package com.miguel.swiftshop.Views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.miguel.swiftshop.Views.theme.SwiftShopTheme
import com.miguel.swiftshop.Views.theme.errorDark
import com.miguel.swiftshop.Views.theme.errorLight
import com.miguel.swiftshop.models.UserList
import java.text.SimpleDateFormat
import java.util.Locale

class ProductActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SwiftShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SwiftShopTheme {
        Column {
            Greeting("Android")
            ItemListTest(UserList("i1289askjdnk","Home Depot Av. Canek", Timestamp(1714416433,  533000000)))
        }
    }
}

@Composable
fun ItemListTest(list: UserList) {
    val date = list.date?.toDate()
    val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(date!!)
    Card(
            Modifier
                .padding(5.dp)
    )
    {
            Row(Modifier.background(errorLight)){
                Box (Modifier.fillMaxWidth(0.9f)){
                    Column {
                        Text(
                            list.name.toString(),
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp,5.dp,10.dp,0.dp)
                                .wrapContentWidth(Alignment.Start),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleMedium
                        )
                        //Spacer(modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                        Text(
                            formattedDate,
                            Modifier
                                .padding(10.dp,5.dp,10.dp,10.dp),
                            //fontStyle = FontStyle.Italic,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically),
                    horizontalArrangement = Arrangement.End
                ){
                    DeleteButton()
                }
            }
        }

}
@Composable
fun DeleteButton(
) {
    Box {
        IconButton(
            onClick = {/**/}
        ) {
            Icon(Icons.Default.Edit  , contentDescription = "delete")
        }
    }
}