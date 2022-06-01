package com.example.instabuginternshiptask.presentation.screens

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.util.Patterns
import android.webkit.URLUtil
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.example.instabuginternshiptask.data.repository.HTTPRequest
import com.example.instabuginternshiptask.data.services.AppStatus
import com.example.instabuginternshiptask.domain.use_case.CheckDeviceStatus
import com.example.instabuginternshiptask.domain.use_case.MakeNetworkRequest


class RequestScreenViewModel : ViewModel() {

    val textFieldCount = mutableStateOf(0)
    val urlTextField = mutableStateOf(TextFieldValue())
    val radioState = mutableStateOf(false)
    val requestBodyState = mutableStateOf(TextFieldValue())

    var headersKeys = mutableListOf(mutableStateOf(TextFieldValue()))
    var headersValues = mutableListOf(mutableStateOf(TextFieldValue()))
    var requestData: MutableMap<String, String> = mutableMapOf()
    val isLoading = mutableStateOf(false)

    private fun isOnline(context: Context): Boolean = CheckDeviceStatus(AppStatus(context)).invoke()

    private fun makeRequest() =
        MakeNetworkRequest(HTTPRequest()).invoke(
            urlTextField.value.text.toString(),
            headersKeys,
            headersValues, textFieldCount, requestBodyState.value.text, requestType()
        )


    fun testGivenURL(context: Context) {
        requestData = mutableMapOf()
        if (isOnline(context)) {
            Toast.makeText(context, "You are online", Toast.LENGTH_SHORT).show()
            if (urlTextField.value.text.isBlank()) {

                Toast.makeText(context, "URL cannot be empty", Toast.LENGTH_SHORT).show()
            } else if (!URLUtil.isValidUrl(urlTextField.value.text) && !Patterns.WEB_URL.matcher(
                    urlTextField.value.text
                ).matches()
            ) {
                Toast.makeText(context, "Invalid URL format", Toast.LENGTH_SHORT).show()
            } else {
                Thread {
                    isLoading.value = true
                    requestData = makeRequest()
                    Log.d(TAG, "testGivenURL: ${requestData.toMap().toString()}")
                    isLoading.value = false
                }.start()
            }
        } else {
            Toast.makeText(context, "You are offline!!!!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestType(): String {
        return if (radioState.value) {
            "POST"
        } else {
            "GET"
        }
    }

}