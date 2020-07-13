package com.example.inteligentnalodowka_mobileapp.ShoppingList

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.MainActivity
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.android.synthetic.main.custom_alertdialog_add_shopping_product.*
import kotlinx.android.synthetic.main.custom_alertdialog_add_shopping_product.view.*
import java.util.*
import kotlin.collections.ArrayList

class ShoppingListActivity : AppCompatActivity() {

    var list_of_types= arrayOf("Wybierz typ produktu", "Sztuki", "Opakowania", "Butelki", "Inne", "g", "dag", "kg","ml","l")
    var list_of_kind= arrayOf("Wybierz rodzaj", "Warzywa", "Owoce", "Nabiał", "Słodycze", "Przekąski", "Mięso", "Ryby", "Produkty zbożowe", "Napoje", "Inne")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)




        AddShoppingProduct.setOnClickListener {
            AlertDialogAddShoppingProduct()
        }

    }

    override fun onBackPressed() {
        val intentOnBackPress = Intent(applicationContext, MainActivity::class.java)
        startActivity(intentOnBackPress)
    }


    override fun onResume() {
        super.onResume()

        val dbHelper = DataBaseHandler(this)
        dbHelper.writableDatabase
        val  productList = dbHelper.getShoppingList()

        recyclerViewShoppingList.layoutManager = LinearLayoutManager(this)
        recyclerViewShoppingList.adapter =  ShoppingListAdapter(this, productList)
    }


    fun AlertDialogAddShoppingProduct() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_alertdialog_add_shopping_product, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
        val  mAlertDialog = mBuilder.show()

        var list = prepareListOfProducts()
        var adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, list)
        mDialogView.EditText_NameShoppingProduct.setAdapter(adapter)
        mDialogView.EditText_NameShoppingProduct.threshold = 1

        var adapterTypes = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_types)
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDialogView.spinner_kindAmount!!.setAdapter(adapterTypes)

        var adapterKinds = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_kind)
        adapterKinds.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mDialogView.editTextTyp!!.setAdapter(adapterKinds)



        mDialogView.Save.setOnClickListener {
            mAlertDialog.dismiss()

            val dbHelper = DataBaseHandler(this)
            val idProduct = UUID.randomUUID().toString()
            var nameShoppingProduct = mDialogView.EditText_NameShoppingProduct.text.toString()

            var kind = mDialogView.spinner_kindAmount.getSelectedItem().toString()
            var howMuchInt = mDialogView.editText_Amount.text.toString().toInt()
            var howMuch_result = customizeTheText(kind, howMuchInt)

            var howMuch = howMuch_result
            var type = mDialogView.editTextTyp.getSelectedItem().toString()

            var product = ShoppingProduct(
                idProduct,
                nameShoppingProduct,
                howMuch,
                type,
                0
            )

            val success = dbHelper.addShoppingProduct(product)
            onResume()


        }
        mDialogView.Back.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


    fun customizeTheText(type : String, howMuch : Int) : String{

        var result = ""
        //"Sztuki", "Opakowania", "Butelki", "Inne", "g", "dag", "kg","ml","l")
        if(type.equals("Sztuki")) result = "sztuk: " + howMuch.toString()
        else if(type.equals("Opakowania")) result = "opakowań: " + howMuch.toString()
        else if(type.equals("Butelki")) result = "butelek: " + howMuch.toString()
        else if(type.equals("Inne")) result = "ilość: " + howMuch.toString()
        else result =  howMuch.toString() + " " + type

        return result
    }

    fun prepareListOfProducts() : ArrayList<String>{

        val dbHelper = DataBaseHandler(this)
        var lisOfProducts = dbHelper.getAllDatabaseProducts()
        var resultsList = ArrayList<String>()

        for (product in lisOfProducts) {
            resultsList.add(product.nameProduct)
        }

        return resultsList
    }

}
