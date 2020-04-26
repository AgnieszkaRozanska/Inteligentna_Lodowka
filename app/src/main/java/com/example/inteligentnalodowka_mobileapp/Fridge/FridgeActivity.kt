package com.example.inteligentnalodowka_mobileapp.Fridge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_fridge.*

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)
        button.setOnClickListener {
            val activityGoToFridge = Intent(applicationContext, ChangeNumberOfProductActivity::class.java)
            startActivity(activityGoToFridge)
        }
    }
}
