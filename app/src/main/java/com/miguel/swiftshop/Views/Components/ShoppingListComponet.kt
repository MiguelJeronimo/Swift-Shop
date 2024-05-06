package com.miguel.swiftshop.Views.Components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.miguel.swiftshop.models.UserList
import java.text.SimpleDateFormat
import java.util.Locale

class ShoppingListComponet {
    @SuppressLint("NotConstructor")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ShoppingList(list: MutableState<List<UserList>>?) {
        LazyColumn(Modifier.fillMaxHeight()) {
            items(list!!.value) {
                ItemList(it)
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ItemList(list: UserList) {
        val date = list.date?.toDate()
        val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        Card(
            onClick = { onClick(list) },
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
                    list.name.toString(),
                    Modifier
                        .align(Alignment.CenterStart)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    formattedDate,
                    Modifier.align(Alignment.CenterEnd),
                    fontStyle = FontStyle.Italic
                )
            }
        }
    }

    private fun onClick(item: UserList){
        println("DATA CLICLEADA: $item")
    }


/*
Preview Components
* **/
    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun previewList(){
        val list = listOf(
            UserList("i1289askjdnk","Aurrera", Timestamp(1714416433,  533000000)),
            UserList("i1289askjdnk","Aurrera", Timestamp(1714416433,  533000000))
        )

        val listDataState = remember { mutableStateOf( emptyList<UserList>()) }
        listDataState.value = list
        ShoppingList(listDataState)
    }
}