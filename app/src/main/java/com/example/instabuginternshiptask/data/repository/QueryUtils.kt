package com.example.instabuginternshiptask.data.repository

import android.text.TextUtils
import android.util.Log
import com.example.instabuginternshiptask.R
import com.example.instabuginternshiptask.data.models.News
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.nio.charset.Charset

class QueryUtils {
    private val LOG_TAG = QueryUtils::class.java.simpleName
    fun fetchNewsData(requestUrl: String): List<News>? {
        val url = createUrl(requestUrl)
        var jsonResponse: String? = null
        try {
            jsonResponse = makeHttpRequest(url)
        } catch (e: IOException) {
            Log.e(LOG_TAG, R.string.HTTP_request.toString(), e)
        }
        return extractFeatureFromJson(jsonResponse)
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
    private fun makeHttpRequest(url: URL?): String {
        var jsonResponse = ""
        if (url == null) {
            return jsonResponse
        }
        var urlConnection: HttpURLConnection? = null
        var inputStream: InputStream? = null
        try {
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.readTimeout = 10000
            urlConnection.connectTimeout = 15000
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            if (urlConnection.responseCode == 200) {
                inputStream = urlConnection.inputStream
                jsonResponse = readFromStream(inputStream)
            } else {
                Log.e(LOG_TAG, R.string.Error_response_code.toString() + urlConnection.responseCode)
            }
        } catch (e: IOException) {
            Log.e(LOG_TAG, R.string.Problem_JSON_results.toString(), e)
        } finally {
            urlConnection?.disconnect()
            inputStream?.close()
        }
        return jsonResponse
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

    private fun extractFeatureFromJson(newsJSON: String?): List<News>? {
        if (TextUtils.isEmpty(newsJSON)) {
            return null
        }
        val newsList: MutableList<News> = ArrayList()
        try {
            val baseJsonResponse = JSONObject(newsJSON.toString())
            val newsArray = baseJsonResponse.getJSONObject("response")
            val results = newsArray.getJSONArray("results")
            for (i in 0 until results.length()) {
                val currentNews = results.getJSONObject(i)
                val webTitle = currentNews.getString("webTitle")
                val webPublicationDate = currentNews.getString("webPublicationDate")
                val sectionName = currentNews.getString("sectionName")
                val webUrl = currentNews.getString("webUrl")
                val item = News(webTitle, webPublicationDate, sectionName, webUrl)
                newsList.add(item)
            }
        } catch (e: JSONException) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e)
        }
        return newsList
    }
}