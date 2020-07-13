package com.example.inteligentnalodowka_mobileapp.ShoppingList

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_shopping_list.*
import kotlinx.android.synthetic.main.custom_alertdialog_add_shopping_product.view.*
import java.util.*

class ShoppingListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_list)

        AddShoppingProduct.setOnClickListener {
            AlertDialogAddShoppingProduct()
        }

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
        mDialogView.Save.setOnClickListener {
            mAlertDialog.dismiss()
         /*   val dbHelper = DataBaseHandler(this)
            val idProduct = UUID.randomUUID().toString()
            var product = ShoppingProduct(
                idProduct,
                "Test",
                "2 opakowania",
                "zboze",
                0
            )

            val success = dbHelper.addShoppingProduct(product)
            onResume()


          */

        }
        mDialogView.Back.setOnClickListener {
            mAlertDialog.dismiss()
        }


    }





}
