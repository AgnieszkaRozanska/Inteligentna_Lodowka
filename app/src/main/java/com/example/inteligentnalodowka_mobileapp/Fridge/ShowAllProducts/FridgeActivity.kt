package com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.MainActivity
import com.example.inteligentnalodowka_mobileapp.Product
import com.example.inteligentnalodowka_mobileapp.R
import com.example.inteligentnalodowka_mobileapp.Recipies.ConcreteRecipeActivity
import kotlinx.android.synthetic.main.activity_fridge.*
import kotlinx.android.synthetic.main.activity_scan_prroducts.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class FridgeActivity : AppCompatActivity() {

    var editTextSearch: EditText? = null
    var adapter: ShowAllProductsAdapter? = null
    var textViewBrakProduktu: TextView? = null
    var productsAfterExpirationDate = ArrayList<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fridge)

        setTextIfListIsEmpty()

        checkExpirationDate()



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
        setTextIfListIsEmpty()
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

    private fun  checkExpirationDate(){
        val sqlConector = DataBaseHandler(this)
        val  productsList=sqlConector.getAllProducts()
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = current.format(formatter)

        for(item in productsList)
        {
            if (item.expirationDate!="Brak") {
                if(item.afterExpirationDate=="false") {
                    var dateTime =
                        LocalDate.parse(
                            item.expirationDate,
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        )

                    var expirationDate =
                        LocalDate.parse(formatted, DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                    if (dateTime.compareTo(expirationDate) < 0)
                        productsAfterExpirationDate.add(item)
                }

            }

        }

        if(productsAfterExpirationDate.isNotEmpty())
            alertDialogExpirationDate()
    }

    private fun alertDialogExpirationDate() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Przeterminowane produkty")
        builder.setMessage("W lodówce znajdują się przeterminowane produkty. Czy chcesz je usunąć?")
        builder.setPositiveButton(getString(R.string.AlertDialogYes)) { dialog: DialogInterface, which: Int ->

            val dbHelper = DataBaseHandler(this)
            for (item in productsAfterExpirationDate){
                val success = dbHelper.updateAfterExpirationDate(item.id)

                if (success) {

                    Toast.makeText(applicationContext,getString(R.string.successOfRemoveProduct), Toast.LENGTH_SHORT).show()
                }else  alertDialogRemove()
            }
            onResume()


        }
        builder.setNegativeButton(getString(R.string.AlertDialogNo)) { dialogInterface: DialogInterface, i: Int ->
            val dbHelper = DataBaseHandler(this)
            for (item in productsAfterExpirationDate) {
                val success = dbHelper.updateAfterExpirationDate(item.id)
                if (success) {

                    Toast.makeText(applicationContext,"Produkt został zaktualizowany", Toast.LENGTH_SHORT).show()
                }else  Toast.makeText(applicationContext,"Produkt nie został zaktualizowany", Toast.LENGTH_SHORT).show()
                onResume()
            }
        }

        builder.setNeutralButton("Usuń wybrane") { dialogInterface: DialogInterface, i: Int ->
            removeChoice()
            onResume()
        }
        builder.show()
    }

    fun removeChoice() {


        val listitems = ArrayList<String>()

        for (item in productsAfterExpirationDate){

            listitems.add(item.nameProduct)
        }
        val items = listitems.toTypedArray()

        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(this)



        builder.setTitle("Usuń wybrane produkty")
        builder.setMultiChoiceItems(items, null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("Usuń") { dialogInterface, i ->
            val selectedStrings = ArrayList<Product>()

            for (j in selectedList.indices) {
                selectedStrings.add(productsAfterExpirationDate[selectedList[j]])
            }



            for (item in selectedStrings){

                    val dbHelper = DataBaseHandler(this)
                    val success = dbHelper.removeProduct(item.id)

                    if (success) {

                        Toast.makeText(applicationContext,getString(R.string.successOfRemoveProduct), Toast.LENGTH_SHORT).show()
                    }else  alertDialogRemove()

            }
            onResume()
        }

        builder.setNegativeButton("Anuluj") { dialogInterface: DialogInterface, i: Int ->
            onResume()
        }
        builder.show()

    }

    private fun alertDialogRemove() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alertDialogMistakeTitle))
        builder.setMessage(getString(R.string.alertDialogMistakeMessage))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }
}
