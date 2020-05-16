package com.example.inteligentnalodowka_mobileapp.Fridge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_product_details.*

class ProductDetailsActivity : AppCompatActivity() {

    private var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setVaues()
    }


    private fun setVaues(){
        if (intent.hasExtra("name")) textViewNameOfProduct.text = intent.getStringExtra("name")
        if (intent.hasExtra("expirationDate")) textViewExpirationDate.text = intent.getStringExtra("expirationDate")
        if (intent.hasExtra("quantity")) textViewAmount.text = intent.getStringExtra("quantity")

        if (intent.hasExtra("id"))  id= intent.getStringExtra("id")

    }
}
