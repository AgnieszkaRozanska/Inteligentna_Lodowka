package com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.MainActivity
import com.example.inteligentnalodowka_mobileapp.R
import com.example.inteligentnalodowka_mobileapp.Recipies.ConcreteRecipeActivity
import kotlinx.android.synthetic.main.activity_fridge.*

class FridgeActivity : AppCompatActivity() {

    var editTextSearch: EditText? = null
    var adapter: ShowAllProductsAdapter? = null
    var textViewBrakProduktu: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        setTextIfListIsEmpty()

        editTextSearch = findViewById(R.id.editTextSearchProducts)
        textViewBrakProduktu = findViewById(R.id.textViewBrakProduktu)

        editTextSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter?.filter?.filter(s.toString())

                if (adapter?.productsFilterList.isNullOrEmpty()) {
                    textViewBrakProduktu!!.visibility = TextView.VISIBLE

                }
                else textViewBrakProduktu!!.visibility = TextView.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                adapter?.filter?.filter(s.toString())

                if (adapter?.productsFilterList.isNullOrEmpty()) {
                    textViewBrakProduktu!!.visibility = TextView.VISIBLE

                }
                else textViewBrakProduktu!!.visibility = TextView.INVISIBLE

            }
        })

        //declare the animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.animationapperance)
        val imageView = findViewById(R.id.imageView) as ImageView

        // set the animation
        imageView.startAnimation(animation)



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

    private fun  setTextIfListIsEmpty(){
        val sqlConector = DataBaseHandler(this)
        val  productsList=sqlConector.getAllProducts()

        if(productsList.isNullOrEmpty()) {
            textViewInformationAboutLackOfProducts.text = "Brak produktów"
            textViewInformationAboutLackOfProducts.visibility = TextView.VISIBLE
        }
        else textViewInformationAboutLackOfProducts.visibility = TextView.INVISIBLE
    }
}
