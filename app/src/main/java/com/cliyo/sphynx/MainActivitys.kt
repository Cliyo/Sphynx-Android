package com.cliyo.sphynx

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import org.json.JSONObject
import java.util.Calendar

class MainActivitys : ComponentActivity() {
    companion object {
        const val LOGIN_CRED = "login_cred"
        const val EMAIL_KEY = "email_key"
        const val PASSWORD_KEY = "password_key"
        const val KEY_EXPIRATION_TIME = "expiration_time"
    }

    private lateinit var sharedpreferences: SharedPreferences
    private var email: String? = null
    private var password: String? = null
    private var expirationTime: Long = -1L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val passwordInput: EditText = findViewById<EditText>(R.id.password_input)
        val usernameInput: EditText = findViewById<EditText>(R.id.username_input)
        val loginBtn = findViewById<Button>(R.id.login_button)

        sharedpreferences = getSharedPreferences(LOGIN_CRED, Context.MODE_PRIVATE)

        this.email = sharedpreferences.getString(EMAIL_KEY, null)
        this.password = sharedpreferences.getString(PASSWORD_KEY, null)
        this.expirationTime = sharedpreferences.getLong(KEY_EXPIRATION_TIME, -1L)

        loginBtn.setOnClickListener {
            this.email = usernameInput.text.toString()
            this.password = passwordInput.text.toString()

            if (checkField(this.email, this.password)) {
                login(this.email, this.password)
            }
        }
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

                expirationTime = Calendar.getInstance().apply { add(Calendar.MINUTE, 3) }.timeInMillis

                val editor = sharedpreferences.edit()

                editor.putString(EMAIL_KEY, username)
                editor.putString(PASSWORD_KEY, password)
                editor.putLong(KEY_EXPIRATION_TIME, expirationTime)

                editor.apply()

                val home = Intent(this@MainActivitys, MainActivity::class.java)
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
        val currentTime = Calendar.getInstance().timeInMillis
        if (email != null && password != null && expirationTime != -1L && currentTime < expirationTime) {
            val home = Intent(this@MainActivitys, MainActivity::class.java)
            startActivity(home)
        }

    }
}