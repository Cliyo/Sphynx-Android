package com.cliyo.sphynx

import android.os.AsyncTask
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Requests (private val callback: (Int) -> Unit) {
    private inner class RequestsTask : AsyncTask<String, Void, Int>() {
        override fun doInBackground(vararg params: String?): Int {
            var resposta = 0
            try {
                val metodo = params[0]
                val url = params[1]
                val dados = params[2]

                println(dados)

                val requestBody = dados?.toByteArray()
                val mURL = URL("http://sphynx-api.local/$url")

                with(mURL.openConnection() as HttpURLConnection) {
                    requestMethod = metodo
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Accept", "application/json")
                    doOutput = true

                    requestBody?.let {
                        outputStream.write(it)
                        outputStream.flush()
                    }

                    resposta = responseCode

                    BufferedReader(InputStreamReader(inputStream)).use {
                        val response = StringBuffer()

                        var inputLine = it.readLine()
                        while (inputLine != null) {
                            response.append(inputLine)
                            inputLine = it.readLine()
                        }
                        println("Response : $response")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return resposta
        }

        override fun onPostExecute(result: Int) {
            callback(result)
        }
    }

    fun request(metodo: String, url: String, dados: JSONObject) {
        RequestsTask().execute(metodo, url, dados.toString())
    }

//    fun testConexao(apiUrls: List<String>){
//        for (i in apiUrls){
//            RequestsTask().execute("GET", i,)
//        }
//
//    }
}

