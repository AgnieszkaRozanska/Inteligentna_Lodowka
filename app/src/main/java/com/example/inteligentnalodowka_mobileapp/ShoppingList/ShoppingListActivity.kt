package com.example.inteligentnalodowka_mobileapp.ShoppingList

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.MainActivity
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.android.synthetic.main.custom_alertdialog_add_shopping_product.*
import kotlinx.android.synthetic.main.custom_alertdialog_add_shopping_product.view.*
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ShoppingListActivity : AppCompatActivity() {

    var list_of_types= arrayOf("Wybierz typ produktu", "Sztuki", "Opakowania", "Butelki", "Inne", "g", "dag", "kg","ml","l")
    var list_of_kind= arrayOf("Wybierz rodzaj", "Warzywa", "Owoce", "Nabiał", "Słodycze", "Przekąski", "Mięso", "Ryby", "Produkty zbożowe", "Napoje", "Inne")
    var flagIfExists = false
    var nameShoppingItem = ""
    var idShoppingItem = ""
    var howMuchShoppingItem = ""
    var typeShoppingItem = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        setTextIfListIsEmpty()

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

        setTextIfListIsEmpty()
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
            try {
                var nameShoppingProduct = mDialogView.EditText_NameShoppingProduct.text.toString()

                var kind = mDialogView.spinner_kindAmount.getSelectedItem().toString()
                var howMuchInt = mDialogView.editText_Amount.text.toString().toInt()
                var howMuch_result = customizeTheText(kind, howMuchInt)

                var howMuch = howMuch_result
                var type = mDialogView.editTextTyp.getSelectedItem().toString()
                val idProduct = UUID.randomUUID().toString()

                nameShoppingItem = nameShoppingProduct
                idShoppingItem = idProduct
                howMuchShoppingItem = howMuch
                typeShoppingItem = type

                var ifCorrect = checkCorrectOfData(nameShoppingProduct, type, howMuch, kind)
                var ifNotExist = ifNotExists(nameShoppingProduct, type,  howMuch_result)

                if(ifCorrect && flagIfExists) {

                    val dbHelper = DataBaseHandler(this)


                    var product = ShoppingProduct(
                        idProduct,
                        nameShoppingProduct,
                        howMuch,
                        type,
                        0
                    )

                    val success = dbHelper.addShoppingProduct(product)
                    mAlertDialog.dismiss()
                    onResume()

                }
            }
            catch (e: Exception) {
                Toast.makeText(this, "Uzupełnij wszytskie dane", Toast.LENGTH_LONG).show()
            }
        }

        mDialogView.Back.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }


    fun customizeTheText(type : String, howMuch : Int) : String{

        var result = ""
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

    fun checkCorrectOfData(nameShoppingItem : String, type : String, howMuch: String, kind : String) : Boolean{
        var result = true;

        if(nameShoppingItem.isEmpty() || type.isEmpty() || type == "Wybierz rodzaj" || howMuch.isEmpty() || kind == "Wybierz typ produktu")
        {
            result = false
            alertDialogLackOfData()
        }
        flagIfExists = result
        return result
    }

    private fun alertDialogLackOfData() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.AlertDialogTitleLackOfData))
        builder.setMessage(getString(R.string.AlertDialogMessageShoppingProductLackOfData))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }

    fun ifNotExists(nameShoppingItem : String, type : String,  kind : String){
        val dbHelper = DataBaseHandler(this)
        val  productList = dbHelper.getShoppingList()

       var kind_from_app = kind.split(' ')
        for (i in productList) {
            var kind_from_database = i.howMuch.split(' ')
            if(i.nameShoppingProduct.equals(nameShoppingItem) && i.type.equals(type) && !kind_from_app[0].equals(kind_from_database[0])){
                alertDialogShoppingItemMaybeExists()
                //alertDialogShoppingItemExists()
                flagIfExists = false
            }
            if(i.nameShoppingProduct.equals(nameShoppingItem) && i.type.equals(type) && kind_from_app[0].equals(kind_from_database[0])){
                //
                flagIfExists = false
                alertDialogShoppingItemExists()
            }
        }
    }


    private fun alertDialogShoppingItemExists() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alertDialogAttention))
        builder.setMessage(getString(R.string.alertDialogShoppingItemExistsMessage))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }

    private fun alertDialogShoppingItemMaybeExists(){

        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alertDialogAttention))
        builder.setMessage(getString(R.string.alertDialogShoppingItemMaybeExistsMessage))
        builder.setPositiveButton(getString(R.string.AlertDialogYes)) { dialog: DialogInterface, which: Int ->
            flagIfExists = true
            addShoppingProduct_EvenThoughItMaybeExist(idShoppingItem, nameShoppingItem, howMuchShoppingItem, typeShoppingItem)

        }
       builder.setNegativeButton(getString(R.string.AlertDialogNo)){ dialog: DialogInterface, which: Int ->
           flagIfExists = false

       }
        builder.show()
    }


    fun addShoppingProduct_EvenThoughItMaybeExist(idProduct : String, nameShoppingProduct : String, howMuch : String, type : String){
        val dbHelper = DataBaseHandler(this)


        var product = ShoppingProduct(
            idProduct,
            nameShoppingProduct,
            howMuch,
            type,
            0
        )

        val success = dbHelper.addShoppingProduct(product)

        val intentOnBackPress = Intent(applicationContext, ShoppingListActivity::class.java)
        startActivity(intentOnBackPress)

        onResume()
    }


    private fun  setTextIfListIsEmpty(){
        val sqlConector = DataBaseHandler(this)
        val  productsList=sqlConector.getShoppingList()

        if(productsList.isNullOrEmpty()) {
            textView_InformAboutLackOfShoppingList.visibility = TextView.VISIBLE
        }
        else textView_InformAboutLackOfShoppingList.visibility = TextView.INVISIBLE
    }
}
