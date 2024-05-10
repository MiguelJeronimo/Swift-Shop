package com.miguel.swiftshop.Views.Components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import com.miguel.swiftshop.Views.DeleteButton
import com.miguel.swiftshop.Views.ViewModels.ViewModelHome
import com.miguel.swiftshop.Views.theme.errorLight
import com.miguel.swiftshop.models.UserList
import java.text.SimpleDateFormat
import java.util.Locale

class ShoppingListComponet(
    private val stateDeleteButton: MutableState<Boolean>?,
    private val stateDataUser: ViewModelHome?
) {
    @SuppressLint("NotConstructor")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ShoppingList(list: MutableState<List<UserList>>?) {
        val array = ArrayList<String>()
        LazyColumn(Modifier.fillMaxHeight()) {
            items(list!!.value) {
                ItemList(it,array)
            }
        }
    }
    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun ItemList(list: UserList, array: ArrayList<String>) {
        val date = list.date?.toDate()
        val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date)
        //obtein to state and save cofiguration state in item to the list
        var stateDeleteItem by rememberSaveable { mutableStateOf(false)}
        //validate state to delete list
        if (!stateDeleteButton!!.value){
            stateDeleteItem = false
        }
        val isArrayNull = stateDataUser!!.dataUserState.value!!.idDocuments
        if (isArrayNull == null){
            array.clear()
        }
        OutlinedCard(
            Modifier
                .padding(5.dp)
                .combinedClickable(
                    onClick = { onClick(list) },
                    onLongClick = {
                        stateDeleteItem = true
                        val count = stateDataUser.dataUserState.value?.count?.inc()
                        onLongClick(list, array, count)
                    }
                )
        )
        {
            when(stateDeleteItem){
                true->{
                    Row(Modifier.background(errorLight)){
                        Box (Modifier.fillMaxWidth(0.9f)){
                            Column {
                                Text(
                                    list.name.toString(),
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp,5.dp,10.dp,0.dp)
                                        .wrapContentWidth(Alignment.Start),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                //Spacer(modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                                Text(
                                    formattedDate,
                                    Modifier
                                        .padding(20.dp,5.dp,10.dp,10.dp),
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
                false->{
                    Row{
                        Box (Modifier.fillMaxWidth(0.9f)){
                            Column {
                                Text(
                                    list.name.toString(),
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp,5.dp,10.dp,0.dp)
                                        .wrapContentWidth(Alignment.Start),
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                //Spacer(modifier = Modifier.padding(0.dp,0.dp,0.dp,10.dp))
                                Text(
                                    formattedDate,
                                    Modifier
                                        .padding(20.dp,5.dp,10.dp,10.dp),
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
        }
    }

    private fun onClick(item: UserList){
        println("DATA: ${item}")
    }

    private fun onLongClick(list: UserList, array: ArrayList<String>, count: Int?) {
        stateDeleteButton!!.value = true
        array.add(list.idDocument.toString())
        stateDataUser?.stateDataUser(count!!, array)
    }


/*
Preview Components
* **/
    @RequiresApi(Build.VERSION_CODES.O)
    @Preview(showBackground = true)
    @Composable
    fun PreviewList(){
        val list = listOf(
            UserList("i1289askjdnk","Aurrera", Timestamp(1714416433,  533000000)),
            UserList("i1289askjdnk","Aurrera", Timestamp(1714416433,  533000000))
        )

        val listDataState = remember { mutableStateOf( emptyList<UserList>()) }
        listDataState.value = list
        ShoppingListTest(listDataState)
    }


    @SuppressLint("NotConstructor")
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ShoppingListTest(list: MutableState<List<UserList>>?) {
        LazyColumn(Modifier.fillMaxHeight()) {
            items(list!!.value) {
                ItemListTest(it)
            }
        }
    }

    @Composable
    fun ItemListTest(list: UserList) {
        val date = list.date?.toDate()
        val dateFormat = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(date!!)
        Card(
            //onClick = { onClick(list) },
            Modifier
                .fillMaxWidth()
                .size(width = 180.dp, height = 80.dp)
                .padding(5.dp)
        )
        {
            Box(
                Modifier
                    .fillMaxSize()
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
    }
}