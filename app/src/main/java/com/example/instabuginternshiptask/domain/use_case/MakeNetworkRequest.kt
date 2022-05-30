package com.example.instabuginternshiptask.domain.use_case

import com.example.instabuginternshiptask.data.repository.HTTPRequest

class MakeNetworkRequest(
    private var request: HTTPRequest
) {
    operator fun invoke(url: String): MutableMap<String, String> = request.fetchNewsData(url)

}