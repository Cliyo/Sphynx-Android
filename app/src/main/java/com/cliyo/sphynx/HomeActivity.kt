package com.cliyo.sphynx

import android.os.Bundle
import androidx.activity.ComponentActivity

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_menu)
    }
}