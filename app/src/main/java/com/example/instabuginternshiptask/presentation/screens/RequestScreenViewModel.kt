package com.example.instabuginternshiptask.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.instabuginternshiptask.data.repository.QueryUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RequestScreenViewModel @Inject constructor(private val queryUtils: QueryUtils) : ViewModel() {
    val textFieldCount = mutableStateOf(1)
    val urlTextField = mutableStateOf(TextFieldValue())
    val radioState = mutableStateOf(false)
    val requestBodyState = mutableStateOf(TextFieldValue())

    var headersKeys = mutableListOf(mutableStateOf(TextFieldValue()))
    var headersValues = mutableListOf(mutableStateOf(TextFieldValue()))

}