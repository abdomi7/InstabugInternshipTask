package com.example.instabuginternshiptask.data.repository

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.input.TextFieldValue
import com.example.instabuginternshiptask.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class HTTPRequest {
    private val LOG_TAG = HTTPRequest::class.java.simpleName
    fun fetchNewsData(
        requestUrl: String,
        headersKeys: MutableList<MutableState<TextFieldValue>>,
        headersValues: MutableList<MutableState<TextFieldValue>>,
        textFieldCount: MutableState<Int>,
        requestBodyState: MutableState<TextFieldValue>,
        requestType: String
    ): MutableMap<String, String> {
        val url = createUrl(requestUrl)
        var jsonResponse: MutableMap<String, String> = mutableMapOf()
        try {
            jsonResponse = makeHttpRequest(
                url,
                headersKeys,
                headersValues,
                textFieldCount,
                requestBodyState,
                requestType
            )
        } catch (e: IOException) {
            Log.e(LOG_TAG, R.string.HTTP_request.toString(), e)
        }
        return jsonResponse
    }

    private fun createUrl(stringUrl: String): URL? {
        var url: URL? = null
        try {
            url = URL(stringUrl)
        } catch (e: MalformedURLException) {
            Log.e(LOG_TAG, R.string.url_problem.toString(), e)
        }
        return url
    }

    @Throws(IOException::class)
    private fun makeHttpRequest(
        url: URL?,
        headersKeys: MutableList<MutableState<TextFieldValue>>,
        headersValues: MutableList<MutableState<TextFieldValue>>,
        textFieldCount: MutableState<Int>,
        requestBodyState: MutableState<TextFieldValue>,
        requestType: String
    ): MutableMap<String, String> {

        val responseData: MutableMap<String, String> = mutableMapOf()
        if (url == null) {
            responseData["error"] = "Invalid URL"
            return responseData
        }
        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.useCaches = true
            urlConnection.defaultUseCaches = true
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            for (i in 0 until textFieldCount.value) {
                if (headersKeys[i].value.text.isNotEmpty() && headersValues[i].value.text.isNotEmpty()) {
                    urlConnection.setRequestProperty(
                        headersKeys[i].value.text,
                        headersValues[i].value.text
                    )
                }
            }
            if (requestType == "POST") {
                urlConnection.requestMethod = requestType
                urlConnection.doInput = true
                urlConnection.doOutput = true
                urlConnection.connect()
                urlConnection.outputStream.use { os ->
                    val input: ByteArray = requestBodyState.value.text.byteInputStream().readBytes()
                    os.write(input, 0, input.size)
                }
                BufferedReader(
                    InputStreamReader(urlConnection.inputStream, "utf-8")
                ).use { br ->
                    val response = java.lang.StringBuilder()
                    var responseLine: String?
                    while (br.readLine().also { responseLine = it } != null) {
                        response.append(responseLine!!.trim { it <= ' ' })
                    }
                    responseData["body/query"] = responseData["body/query"].orEmpty() + response.toString().orEmpty()
                }
            } else {
                urlConnection.requestMethod = requestType
                urlConnection.connect()
            }

            responseData["responseCode"] = urlConnection.responseCode.toString()
            responseData["body/query"] = responseData["body/query"].orEmpty() + url.query.orEmpty()
            if (urlConnection.responseCode == 200) {
                inputStream = urlConnection.inputStream
                responseData["jsonResponse"] = readFromStream(inputStream).orEmpty()
                responseData["headerFields"] = urlConnection.headerFields.orEmpty().toString()
                responseData["error"] = "No Error"
            } else {
                responseData["error"] = readFromStream(urlConnection.errorStream).orEmpty()
                Log.e(LOG_TAG, R.string.Error_response_code.toString() + urlConnection.responseCode)
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, R.string.Problem_JSON_results.toString(), e)
            responseData["error"] = e.localizedMessage.toString()
        } finally {
            urlConnection?.disconnect()
            inputStream?.close()
        }
        return responseData
    }

    @Throws(IOException::class)
    private fun readFromStream(inputStream: InputStream?): String {
        val output = StringBuilder()
        if (inputStream != null) {
            val inputStreamReader = InputStreamReader(inputStream, Charset.forName("UTF-8"))
            val reader = BufferedReader(inputStreamReader)
            var line = reader.readLine()
            while (line != null) {
                output.append(line)
                line = reader.readLine()
            }
        }
        return output.toString()
    }

}