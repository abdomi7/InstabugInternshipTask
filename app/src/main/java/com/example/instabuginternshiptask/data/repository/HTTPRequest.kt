package com.example.instabuginternshiptask.data.repository

import android.util.Log
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
    fun fetchNewsData(requestUrl: String): MutableMap<String, String> {
        val url = createUrl(requestUrl)
        var jsonResponse: MutableMap<String, String> = mutableMapOf()
        try {
            jsonResponse = makeHttpRequest(url)
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
    private fun makeHttpRequest(url: URL?): MutableMap<String, String> {

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
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            responseData["responseCode"] = urlConnection.responseCode.toString()
            responseData["body/query"] = url.query.orEmpty()
            if (urlConnection.responseCode == 200) {
                inputStream = urlConnection.inputStream
                responseData["jsonResponse"] = readFromStream(inputStream)
                responseData["headerFields"] = urlConnection.headerFields.toString()
                responseData["error"] = "No Error"
            } else {
                responseData["error"] = readFromStream(urlConnection.errorStream)
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

//    private fun extractFeatureFromJson(newsJSON: String?): List<News>? {
//        if (TextUtils.isEmpty(newsJSON)) {
//            return null
//        }
//        val newsList: MutableList<News> = ArrayList()
//        try {
//            val baseJsonResponse = JSONObject(newsJSON.toString())
//            val newsArray = baseJsonResponse.getJSONObject("response")
//            val results = newsArray.getJSONArray("results")
//            for (i in 0 until results.length()) {
//                val currentNews = results.getJSONObject(i)
//                val webTitle = currentNews.getString("webTitle")
//                val webPublicationDate = currentNews.getString("webPublicationDate")
//                val sectionName = currentNews.getString("sectionName")
//                val webUrl = currentNews.getString("webUrl")
//                val item = News(webTitle, webPublicationDate, sectionName, webUrl)
//                newsList.add(item)
//            }
//        } catch (e: JSONException) {
//            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e)
//        }
//        return newsList
//    }
}