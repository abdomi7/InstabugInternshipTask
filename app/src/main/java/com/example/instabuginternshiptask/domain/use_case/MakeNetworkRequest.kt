package com.example.instabuginternshiptask.domain.use_case

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import com.example.instabuginternshiptask.data.repository.HTTPRequest

class MakeNetworkRequest(
    private var request: HTTPRequest
) {
    operator fun invoke(
        url: String,
        headersKeys: MutableList<MutableState<TextFieldValue>>,
        headersValues: MutableList<MutableState<TextFieldValue>>,
        textFieldCount: MutableState<Int>,
        requestBodyState: MutableState<TextFieldValue>,
        requestType: String
    ): MutableMap<String, String> =
        request.fetchNewsData(
            url,
            headersKeys,
            headersValues,
            textFieldCount,
            requestBodyState,
            requestType
        )
}