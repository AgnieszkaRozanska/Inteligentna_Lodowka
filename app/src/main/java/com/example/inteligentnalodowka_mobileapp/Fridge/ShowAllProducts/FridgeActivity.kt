package com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.MainActivity
import com.example.inteligentnalodowka_mobileapp.Product
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_fridge.*
import java.util.Locale.filter


class FridgeActivity : AppCompatActivity() {


    var editTextSearch: EditText? = null
    var adapter: ShowAllProductsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        setTextIfListIsEmpty()


        editTextSearch = findViewById(R.id.editTextSearchProducts);


        editTextSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                adapter?.filter?.filter(charSequence.toString())

            }

            override fun afterTextChanged(editable: Editable) {
                adapter?.filter?.filter(editable.toString())

            }
        })


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

        adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter


    }

    private fun  setTextIfListIsEmpty() {
        val sqlConector = DataBaseHandler(this)
        val productsList = sqlConector.getAllProducts()

        if (productsList.isNullOrEmpty()) {
            textViewInformationAboutLackOfProducts.text = "Brak produktów"
            textViewInformationAboutLackOfProducts.visibility = TextView.VISIBLE
        } else textViewInformationAboutLackOfProducts.visibility = TextView.INVISIBLE


    }
}





