package com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.MainActivity
import com.example.inteligentnalodowka_mobileapp.R
import com.example.inteligentnalodowka_mobileapp.Recipies.ConcreteRecipeActivity
import kotlinx.android.synthetic.main.activity_fridge.*

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

    }

    override fun onBackPressed() {
        val intentOnBackPress = Intent(applicationContext, MainActivity::class.java)
        startActivity(intentOnBackPress)
    }

    override fun onResume() {
        super.onResume()

        val dbHelper = DataBaseHandler(this)
        dbHelper.writableDatabase
        val  productList = dbHelper.getAllProducts()

        recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)
    }
}
