package com.example.instabuginternshiptask.presentation.screens

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel


class RequestScreenViewModel : ViewModel() {
    val textFieldCount = mutableStateOf(1)
    val urlTextField = mutableStateOf(TextFieldValue())
    val radioState = mutableStateOf(false)
    val requestBodyState = mutableStateOf(TextFieldValue())

}