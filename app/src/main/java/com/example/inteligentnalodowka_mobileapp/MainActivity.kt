package com.example.inteligentnalodowka_mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonFridge.setOnClickListener{
            goToWindowFridge()
        }

    }

    private fun goToWindowFridge(){
        val activityGoToFridge = Intent(applicationContext, Fridge::class.java)
        startActivity(activityGoToFridge)
    }
}
