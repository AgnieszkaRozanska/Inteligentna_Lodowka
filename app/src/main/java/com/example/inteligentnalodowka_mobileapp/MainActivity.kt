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
            goToWindowFridgeActivity()
        }
        buttonScan.setOnClickListener{
            goToWindowScanActivity()
        }

    }

    private fun goToWindowFridgeActivity(){
        val activityGoToFridge = Intent(applicationContext, FridgeActivity::class.java)
        startActivity(activityGoToFridge)
    }

    private fun goToWindowScanActivity(){
        val activityGoToScanActivity = Intent(applicationContext, ScanPrroductsActivity::class.java)
        startActivity(activityGoToScanActivity)
    }

}
