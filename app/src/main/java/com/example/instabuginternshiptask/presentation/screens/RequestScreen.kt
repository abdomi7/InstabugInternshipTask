package com.example.instabuginternshiptask.presentation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.instabuginternshiptask.presentation.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@ExperimentalMaterial3Api
@Composable
fun RequestScreen() {
    val viewModel: RequestScreenViewModel = viewModel()
    val context = LocalContext.current

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
                        viewModel.testGivenURL(context)
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

            repeat(viewModel.textFieldCount.value) { num ->


                Row(modifier = Modifier) {
                    OutlinedTextField(
                        modifier = Modifier
                            .weight(0.5F)
                            .padding(12.dp),
                        value = viewModel.headersKeys[num].value,
                        onValueChange = {
                            viewModel.headersKeys[num].value = it
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
                        value = viewModel.headersValues[num].value,
                        onValueChange = {
                            viewModel.headersValues[num].value = it
                        },
                        label = {
                            Text(text = "Header Value")
                        },
                        shape = RoundedCornerShape(8.dp),
                    )
                }
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
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = viewModel.radioState.value,
                    onClick = { viewModel.radioState.value = viewModel.radioState.value.not() })
                Text(text = "Post Request")
            }
            Row(modifier = Modifier) {
                Button(onClick = {
                    viewModel.headersKeys.add(mutableStateOf(TextFieldValue()))
                    viewModel.headersValues.add(mutableStateOf(TextFieldValue()))
                    viewModel.textFieldCount.value++

                }, modifier = Modifier.padding(8.dp)) {
                    Text("Add Header")

                }
                Button(onClick = {
                    if (viewModel.textFieldCount.value > 0) {
                        viewModel.textFieldCount.value--
                    }
                }, modifier = Modifier.padding(8.dp)) {
                    Text("Delete Header")
                }
            }
            Column(modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()) {
                if (viewModel.isLoading.value) {

                    Box(modifier = Modifier.align(CenterHorizontally)) {
                        CircularProgressIndicator()
                    }
                }
                Text(text = "Response code: ${viewModel.requestData["responseCode"].orEmpty()}", modifier = Modifier.padding(vertical = 12.dp))
                Text(text = "Error: ${viewModel.requestData["error"].orEmpty()}", modifier = Modifier.padding(vertical = 12.dp))
                Text(text = "Headers: ${viewModel.requestData["headerFields"].orEmpty()}", modifier = Modifier.padding(vertical = 12.dp))
                Text(text = "Request body or query parameters: ${viewModel.requestData["body/query"].orEmpty()}", modifier = Modifier.padding(vertical = 12.dp))
            }
        }
    }
}

