package com.example.instabuginternshiptask.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.instabuginternshiptask.presentation.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun RequestScreen(navController: NavHostController) {
    val viewModel: RequestScreenViewModel = viewModel()
    Scaffold(
        modifier = Modifier, topBar = { TopBar() },
        floatingActionButton = {

            Image(
                imageVector = Icons.Rounded.Check,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(55.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    .clickable {

                    },
            )
        },
    ) { it ->
        Column(
            modifier = Modifier
                .padding(it)
                .verticalScroll(rememberScrollState())
        ) {

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                value = viewModel.urlTextField.value,
                onValueChange = {
                    viewModel.urlTextField.value = it
                },
                label = {
                    Text(text = "URL")
                },
                shape = RoundedCornerShape(8.dp),
            )

            repeat(viewModel.textFieldCount.value) {

                var valueState by remember {
                    mutableStateOf(TextFieldValue())
                }

                Row(modifier = Modifier) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(0.5F)
                            .padding(12.dp),
                        value = valueState,
                        onValueChange = {
                            valueState = it
                        },
                        label = {
                            Text(text = "Header Key")
                        },
                        shape = RoundedCornerShape(8.dp),
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(0.5F)
                            .padding(12.dp),
                        value = valueState,
                        onValueChange = {
                            valueState = it
                        },
                        label = {
                            Text(text = "Header Value")
                        },
                        shape = RoundedCornerShape(8.dp),
                    )
                }
            }

            Button(onClick = {
                viewModel.textFieldCount.value++
            }, modifier = Modifier.padding(8.dp)) {
                Text("Add Header")
            }

            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = viewModel.radioState.value,
                    onClick = { viewModel.radioState.value = viewModel.radioState.value.not() })
                Text(text = "Post Request")
            }

            if (viewModel.radioState.value) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    value = viewModel.requestBodyState.value,
                    onValueChange = {
                        viewModel.requestBodyState.value = it
                    },
                    label = {
                        Text(text = "Request Body")
                    },
                    shape = RoundedCornerShape(8.dp),
                )
            }
        }
    }
}

