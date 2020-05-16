package com.example.inteligentnalodowka_mobileapp.Fridge

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts.FridgeActivity
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_change_number_of_product.*

class ChangeNumberOfProductActivity : AppCompatActivity() {

    private var id = ""
    private var name = ""
    private var quantity = ""
    private var expirationDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_number_of_product)

        setVaues()

        buttonIncrease.setOnClickListener {
            increaseProduct()
        }

        buttonDecrease.setOnClickListener {
            decreaseProduct()
        }

        buttonSave.setOnClickListener {
            updateQuantity(textViewAmount.text.toString())
        }
    }

    override fun onBackPressed() {
        var oldValue = intent.getStringExtra("quantity").toInt()
        var newValue = textViewAmount.text.toString().toInt()
        if(oldValue != newValue){
            alertDialogOnBackPress()
        }else{
            val intentOnBackPress = Intent(applicationContext, ProductDetailsActivity::class.java)
            if (intent.hasExtra("id")) id = intent.getStringExtra("id")
            if (intent.hasExtra("name")) name = intent.getStringExtra("name")
            if (intent.hasExtra("quantity")) quantity = intent.getStringExtra("quantity")
            if (intent.hasExtra("expirationDate"))   expirationDate = intent.getStringExtra("expirationDate")

            intentOnBackPress.putExtra("id", id)
            intentOnBackPress.putExtra("name", name)
            intentOnBackPress.putExtra("quantity", quantity)
            intentOnBackPress.putExtra("expirationDate", expirationDate)
            startActivity(intentOnBackPress)
        }
    }

    private fun setVaues() {
        if (intent.hasExtra("name")) textViewNameOfProduct.text = intent.getStringExtra("name")
        if (intent.hasExtra("quantity")) textViewAmount.text = intent.getStringExtra("quantity")

        if (intent.hasExtra("id")) id = intent.getStringExtra("id")
    }

    private fun increaseProduct(){
        var amount = textViewAmount.text.toString().toInt()
        amount += 1
        setNewQuantity(amount)
    }

    private fun decreaseProduct(){
        var amount = textViewAmount.text.toString().toInt()
        if(amount>1){
            amount -= 1
            setNewQuantity(amount)
        }
        if (amount == 1){
            alertDialogRemoveProduct()
        }
    }

    private fun setNewQuantity(quantity : Int){
        textViewAmount.text = quantity.toString()
    }

    private fun alertDialogRemoveProduct() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.attention))
        builder.setMessage(getString(R.string.alertDialogMessageChangeQuantity))
        builder.setPositiveButton(getString(R.string.AlertDialogYes)) { dialog: DialogInterface, which: Int ->
            removeProduct()
        }
        builder.setNegativeButton(getString(R.string.AlertDialogNo)) { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    private fun removeProduct() {
        val intentRemove = Intent(applicationContext, FridgeActivity::class.java)
        val dbHelper = DataBaseHandler(this)
        if (intent.hasExtra("id")) id = intent.getStringExtra("id")

        val success = dbHelper.removeProduct(id)

        if (success) {
            startActivity(intentRemove)
            Toast.makeText(applicationContext,getString(R.string.successOfRemoveProduct), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateQuantity(quantity : String){
        val intentUpdate = Intent(applicationContext, ProductDetailsActivity::class.java)
        val dbHelper = DataBaseHandler(this)
        intentUpdate.putExtra("quantity", textViewAmount.text.toString())
        if (intent.hasExtra("id")) id = intent.getStringExtra("id")

        val success = dbHelper.updateQuantityOfProducts(id, quantity)

        if (success) {
            if (intent.hasExtra("expirationDate"))   expirationDate = intent.getStringExtra("expirationDate")
            intentUpdate.putExtra("expirationDate", expirationDate)
            startActivity(intentUpdate)
            Toast.makeText(applicationContext,getString(R.string.succesOfUpdateProduct), Toast.LENGTH_SHORT).show()
        }
    }


    private fun alertDialogOnBackPress() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.attention))
        builder.setMessage("Cofnięcie się spowoduje, że zmiany nie zostaną zapisane. Czy na pewno chcesz wrócić?")
        builder.setPositiveButton(getString(R.string.AlertDialogYes)) { dialog: DialogInterface, which: Int ->
            val intentOnBackPress = Intent(applicationContext, ProductDetailsActivity::class.java)
            if (intent.hasExtra("id")) id = intent.getStringExtra("id")
            if (intent.hasExtra("name")) name = intent.getStringExtra("name")
            if (intent.hasExtra("quantity")) quantity = intent.getStringExtra("quantity")
            if (intent.hasExtra("expirationDate"))   expirationDate = intent.getStringExtra("expirationDate")

            intentOnBackPress.putExtra("id", id)
            intentOnBackPress.putExtra("name", name)
            intentOnBackPress.putExtra("quantity", quantity)
            intentOnBackPress.putExtra("expirationDate", expirationDate)
            startActivity(intentOnBackPress)

            startActivity(intentOnBackPress)
        }
        builder.setNegativeButton(getString(R.string.AlertDialogNo)) { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

}
