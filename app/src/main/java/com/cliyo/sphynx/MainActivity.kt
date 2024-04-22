package com.cliyo.sphynx

import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.os.AsyncTask
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        adjustFields()

        lateinit var email: EditText
        lateinit var senha: EditText

        val passwordInput = findViewById<EditText>(R.id.password_input)
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val loginBtn = findViewById<Button>(R.id.login_button)

        loginBtn.setOnClickListener{
            var username = usernameInput.text.toString()
            var password = passwordInput.text.toString()

            println(username)
            println(password)

            if(checkField(username, password)){
                login(username, password)
            }

        }
    }

    private fun adjustFields(){
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val editTextWidth = (displayWidth * 0.77).toInt()

        passwordInput.layoutParams.width = editTextWidth
        usernameInput.layoutParams.width = editTextWidth
    }

    private fun checkField(username: String = "", password: String = ""): Boolean{
        var mensagem: Toast
        if (username.trim().equals("")){
            mensagem = Toast.makeText(applicationContext, R.string.empty_login, Toast.LENGTH_SHORT)
            mensagem.show()
            return false
        }
        if (password.trim().equals("")){
            mensagem = Toast.makeText(applicationContext, R.string.empty_password, Toast.LENGTH_SHORT)
            mensagem.show()
            return false
        }

        return true
    }

    private inner class loginRequest: AsyncTask<String, Void, Int>(){
        override fun doInBackground(vararg credenciais: String): Int{
            val username = credenciais[0]
            val password = credenciais[1]

            var resposta = 0
            try {
                val jsonData = JSONObject()
                jsonData.put("user", username)
                jsonData.put("password", password)

                println(jsonData)

                val requestBody = jsonData.toString().toByteArray()
                val mURL = URL("http://192.168.0.108:8080/login")
                //TODO: Criar um finder para achar o ip da API
                // OU criar uma funcao para setar manualmente

                with(mURL.openConnection() as HttpURLConnection) {
                    requestMethod = "POST"
                    setRequestProperty("Content-Type", "application/json")
                    setRequestProperty("Accept", "application/json")
                    doOutput = true

                    outputStream.write(requestBody)
                    outputStream.flush()

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
            val mensagem = Toast.makeText(applicationContext, "$result", Toast.LENGTH_SHORT)
            mensagem.show()
        }

    }
    private fun login(username: String, password: String) {
        loginRequest().execute(username, password)
    }
}
