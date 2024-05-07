package com.miguel.swiftshop.Views.Components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.miguel.swiftshop.Views.theme.md_theme_dark_error
import com.miguel.swiftshop.models.UserList
import java.text.SimpleDateFormat
import java.util.Locale

class ShoppingListComponet(private val stateDeleteButton: MutableState<Boolean>?) {
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemList(list: UserList) {
        val date = list.date?.toDate()
        val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        //obtein to state and save cofiguration state in item to the list
        var stateDeleteItem by rememberSaveable { mutableStateOf(false)}
        //validate state to delete list
        if (!stateDeleteButton!!.value){
            stateDeleteItem = false
        }

        Card(
            //onClick = { onClick(list) },
            Modifier
                .fillMaxWidth()
                .size(width = 180.dp, height = 80.dp)
                .padding(5.dp)
                .combinedClickable(
                    onClick = { onClick(list) },
                    onLongClick = {
                        stateDeleteItem = true
                        onLongClick(list)
                    }
                )
        )
        {
            when(stateDeleteItem){
                true->{
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(md_theme_dark_error)
                    ) {
                        Text(
                            list.name.toString(),
                            Modifier
                                .align(Alignment.CenterStart)
                                .fillMaxWidth()
                                .padding(5.dp)
                                .wrapContentHeight(),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            formattedDate,
                            Modifier
                                .align(Alignment.CenterEnd)
                                .padding(5.dp),
                            fontStyle = FontStyle.Italic
                        )
                    }
                }
                false->{
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
        }
    }

    private fun onClick(item: UserList){
        println("DATA CLICLEADA: ${item.idDocument}")
    }

    private fun onLongClick(list: UserList) {
        stateDeleteButton!!.value = true
        println(list.idDocument)
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