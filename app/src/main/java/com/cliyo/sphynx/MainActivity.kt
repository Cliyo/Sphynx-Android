package com.cliyo.sphynx

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    companion object {
        const val LOGIN_CRED = "login_cred"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
    }

    private lateinit var sharedpreferences: SharedPreferences
    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        adjustFields()

        val passwordInput = findViewById<EditText>(R.id.password_input)
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val loginBtn = findViewById<Button>(R.id.login_button)

        sharedpreferences = getSharedPreferences(LOGIN_CRED, Context.MODE_PRIVATE)

        email = sharedpreferences.getString(EMAIL_KEY, null)
        password = sharedpreferences.getString(PASSWORD_KEY, null)

        loginBtn.setOnClickListener {
            email = usernameInput.text.toString()
            password = passwordInput.text.toString()

            if (checkField(email, password)) {
                login(email, password)
            }
        }
    }

    private fun adjustFields() {
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val usernameInput = findViewById<EditText>(R.id.username_input)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val editTextWidth = (displayWidth * 0.77).toInt()

        passwordInput.layoutParams.width = editTextWidth
        usernameInput.layoutParams.width = editTextWidth
    }

    private fun checkField(username: String?, password: String?): Boolean {
        var mensagem: Toast
        if (username?.trim().equals("")) {
            mensagem = Toast.makeText(applicationContext, R.string.empty_login, Toast.LENGTH_SHORT)
            mensagem.show()
            return false
        }
        if (password?.trim().equals("")) {
            mensagem =
                Toast.makeText(applicationContext, R.string.empty_password, Toast.LENGTH_SHORT)
            mensagem.show()
            return false
        }

        return true
    }

    private fun login(username: String?, password: String?) {
        val requests = Requests { resposta ->
            val mensagem = Toast.makeText(applicationContext, "$resposta", Toast.LENGTH_SHORT)
            mensagem.show()

            if (resposta == 200) {
                sharedpreferences = getSharedPreferences(LOGIN_CRED, Context.MODE_PRIVATE)
                val editor = sharedpreferences.edit()

                editor.putString(EMAIL_KEY, username)
                editor.putString(PASSWORD_KEY, password)

                editor.apply()

                val home = Intent(this@MainActivity, HomeActivity::class.java)
                startActivity(home)
                finish()
            }
        }
        val dados = JSONObject()
        dados.put("user", username)
        dados.put("password", password)

        requests.request("POST", "login", dados)
    }

    override fun onStart() {
        super.onStart()
        if (email != null && password != null) {
            val home = Intent(this@MainActivity, HomeActivity::class.java)
            startActivity(home)
        }

    }
}