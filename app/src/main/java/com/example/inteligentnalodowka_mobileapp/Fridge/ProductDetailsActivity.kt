package com.example.inteligentnalodowka_mobileapp.Fridge

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts.FridgeActivity
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_product_details.textViewExpirationDate

class ProductDetailsActivity : AppCompatActivity() {

    private var id = ""
    private var name = ""
    private var quantity = ""
    private var expirationDate = ""
    private var purchaseDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setVaues()
        if (intent.hasExtra("quantity")) textViewAmount.text = intent.getStringExtra("quantity")

        buttonChangeDate.setOnClickListener {
            goToChangeDateActivity()
        }

        buttonAddProducts.setOnClickListener {
            goToChangeQuantityActivity()
        }

        buttonDeleteProduct.setOnClickListener {
            alertDialogRemoveProduct()
        }

        //declare the animation
        val animation_button_ExpirationDate = AnimationUtils.loadAnimation(this, R.anim.animation_button_expiration_date)
        val animation_button_AddProducts = AnimationUtils.loadAnimation(this, R.anim.animation_button_add_product)
        val animation_button_DeleteProducts = AnimationUtils.loadAnimation(this, R.anim.animation_button_delete_product)

        val button_change_expirationDate = findViewById(R.id.buttonChangeDate) as Button
        val button_add_products = findViewById(R.id.buttonAddProducts) as Button
        val button_delete_product = findViewById(R.id.buttonDeleteProduct) as Button

        // set the animation
        button_change_expirationDate.startAnimation(animation_button_ExpirationDate)
        button_add_products.startAnimation(animation_button_AddProducts)
        button_delete_product.startAnimation(animation_button_DeleteProducts)


    }

    override fun onBackPressed() {
        val intentOnBackPress = Intent(applicationContext, FridgeActivity::class.java)
        startActivity(intentOnBackPress)
    }


    private fun setVaues() {
        if (intent.hasExtra("name")) textViewNameOfProduct.text = intent.getStringExtra("name")
        if (intent.hasExtra("expirationDate")) textViewExpirationDate.text =
            intent.getStringExtra("expirationDate")
        if (intent.hasExtra("purchaseDate")) textViewFinalPurDate.text =
            intent.getStringExtra("purchaseDate")
        if (intent.hasExtra("quantity")) textViewAmount.text = intent.getStringExtra("quantity")

        if (intent.hasExtra("id")) id = intent.getStringExtra("id")
    }

    private fun alertDialogRemoveProduct() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alertDialogRemoveProductTitle))
        builder.setMessage(getString(R.string.alertDialogRemoveProductMessage))
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
        }else  alertDialog()

    }


    private fun alertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alertDialogMistakeTitle))
        builder.setMessage(getString(R.string.alertDialogMistakeMessage))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }


    fun goToChangeQuantityActivity(){
        val goToChangeNumberOfProductActivity = Intent(applicationContext, ChangeNumberOfProductActivity::class.java)
        if (intent.hasExtra("id")) id = intent.getStringExtra("id")
        if (intent.hasExtra("name")) name = intent.getStringExtra("name")
        if (intent.hasExtra("quantity")) quantity = intent.getStringExtra("quantity")
        if (intent.hasExtra("expirationDate")) expirationDate=
            intent.getStringExtra("expirationDate")
        if (intent.hasExtra("purchaseDate")) purchaseDate=
            intent.getStringExtra("purchaseDate")


        goToChangeNumberOfProductActivity.putExtra("id", id)
        goToChangeNumberOfProductActivity.putExtra("name", name)
        goToChangeNumberOfProductActivity.putExtra("quantity", quantity)
        goToChangeNumberOfProductActivity.putExtra("expirationDate", expirationDate)
        goToChangeNumberOfProductActivity.putExtra("purchaseDate", purchaseDate)
        startActivity(goToChangeNumberOfProductActivity)
    }

    fun goToChangeDateActivity(){
        val goToChangeExpirationDateActivity = Intent(applicationContext, ChangeExpirationDateActivity::class.java)
        if (intent.hasExtra("id")) id = intent.getStringExtra("id")
        if (intent.hasExtra("name")) name = intent.getStringExtra("name")
        if (intent.hasExtra("quantity")) quantity = intent.getStringExtra("quantity")
        if (intent.hasExtra("expirationDate")) expirationDate=
            intent.getStringExtra("expirationDate")
        if (intent.hasExtra("purchaseDate")) purchaseDate=
            intent.getStringExtra("purchaseDate")


        goToChangeExpirationDateActivity.putExtra("id", id)
        goToChangeExpirationDateActivity.putExtra("name", name)
        goToChangeExpirationDateActivity.putExtra("quantity", quantity)
        goToChangeExpirationDateActivity.putExtra("expirationDate", expirationDate)
        goToChangeExpirationDateActivity.putExtra("purchaseDate", purchaseDate)
        startActivity(goToChangeExpirationDateActivity)
    }
}



