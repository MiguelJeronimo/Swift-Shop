package com.miguel.swiftshop.Views.Components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Timestamp
import com.miguel.swiftshop.Views.ViewModels.ViewModelHome
import com.miguel.swiftshop.models.UserStateUpdate
import java.util.Date

class AlertDialogCustom(private val viewModelUserList: ViewModelHome) {
    @Composable
    fun AlertDialogAdd(
        alertDialogState: MutableState<Boolean>?,
        idCollection: String?,
        userStateUpdate: UserStateUpdate? = null
    ) {
        val textState = remember { mutableStateOf("") }
        val openDialog = remember { mutableStateOf(true) }
        //Data to update firebase
        if (userStateUpdate != null) {
            textState.value = userStateUpdate.name.toString()
        }else{
            textState.value = ""
        }
        val isError = remember {mutableStateOf(false)}
        if (openDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openDialog.value = true
                },
                title = {
                    if (userStateUpdate != null) {
                        Text(text = "Actualizar lista")
                    }else{
                        Text(text = "Insertar lista")
                    }
                },
                text = {
                    TextField("Nombre",textState,null,0,isError)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if(textState.value.isNotEmpty()){
                                if (userStateUpdate?.name == null){
                                    val date = Date()
                                    val timeStamp = Timestamp(date)
                                    viewModelUserList.insert(textState.value,timeStamp, idCollection)
                                    openDialog.value = false
                                    alertDialogState?.value = false
                                } else {
                                    val date = Date()
                                    val timeStamp = Timestamp(date)
                                    viewModelUserList.update(textState.value,timeStamp, idCollection, userStateUpdate.uuiDocument)
                                    openDialog.value = false
                                    alertDialogState?.value = false
                                }
                            }
                        }
                    ) {
                        Text("Aceptar")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDialog.value = false
                            alertDialogState?.value = false
                            textState.value = ""
                        }
                    ) {
                        Text("Salir")
                    }
                }
            )
        }
    }

    @Composable
    fun TextField(label: String, text: MutableState<String>, text2: MutableState<String>?, inputType: Int, isError: MutableState<Boolean>){
        when(inputType){
            0->{
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {
                        text.value = it
                        //textUpdateState.value = it
                        println("Valor TExto Editar: ${it}")
                        println("TextValue ${text.value}")
                                    },
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
                    value = text.value,
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

    //preview
    @Preview(showBackground = true)
    @Composable
    fun PreviewModals(){
        AlertDialogAdd(null, null)
    }
}