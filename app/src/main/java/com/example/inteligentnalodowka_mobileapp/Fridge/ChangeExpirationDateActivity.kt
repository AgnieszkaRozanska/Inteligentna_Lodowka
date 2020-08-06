package com.example.inteligentnalodowka_mobileapp.Fridge

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts.FridgeActivity
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_change_expiration_date.*
import kotlinx.android.synthetic.main.activity_change_expiration_date.buttonSave
import kotlinx.android.synthetic.main.activity_change_expiration_date.textViewNameOfProduct
import kotlinx.android.synthetic.main.activity_change_number_of_product.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ChangeExpirationDateActivity : AppCompatActivity() {

    private var id = ""
    private var name = ""
    private var quantity = ""
    private var expirationDate = ""
    private var purchaseDate = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_expiration_date)

        setVaues()

        buttonCalendar.setOnClickListener {
            chooseNewDate()
        }

        buttonSave.setOnClickListener {
            updateDate(textViewNewDate.text.toString())
        }

    }

    override fun onBackPressed() {

        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = current.format(formatter)

        val newValue = textViewNewDate.text.toString()
        if(formatted.toString()!= newValue){
            alertDialogOnBackPress()
        }else{
            val intentOnBackPress = Intent(applicationContext, ProductDetailsActivity::class.java)
            if (intent.hasExtra("id")) id = intent.getStringExtra("id")
            if (intent.hasExtra("name")) name = intent.getStringExtra("name")
            if (intent.hasExtra("quantity")) quantity = intent.getStringExtra("quantity")
            if (intent.hasExtra("expirationDate"))   expirationDate = intent.getStringExtra("expirationDate")
            if (intent.hasExtra("purchaseDate"))   purchaseDate = intent.getStringExtra("purchaseDate")

            intentOnBackPress.putExtra("id", id)
            intentOnBackPress.putExtra("name", name)
            intentOnBackPress.putExtra("quantity", quantity)
            intentOnBackPress.putExtra("expirationDate", expirationDate)
            intentOnBackPress.putExtra("purchaseDate", purchaseDate)
            startActivity(intentOnBackPress)
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
            if (intent.hasExtra("purchaseDate"))   purchaseDate = intent.getStringExtra("purchaseDate")

            intentOnBackPress.putExtra("id", id)
            intentOnBackPress.putExtra("name", name)
            intentOnBackPress.putExtra("quantity", quantity)
            intentOnBackPress.putExtra("expirationDate", expirationDate)
            intentOnBackPress.putExtra("purchaseDate", purchaseDate)
            startActivity(intentOnBackPress)

            startActivity(intentOnBackPress)
        }
        builder.setNegativeButton(getString(R.string.AlertDialogNo)) { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    private fun setVaues() {
        if (intent.hasExtra("name")) textViewNameOfProduct.text = intent.getStringExtra("name")
        if (intent.hasExtra("expirationDate")) textPreviousDate.text =
            intent.getStringExtra("expirationDate")

        if (intent.hasExtra("id")) id = intent.getStringExtra("id")
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = current.format(formatter)
        textViewNewDate.text = formatted.toString()
    }

    private fun chooseNewDate(){

        var cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)
        textViewNewDate.text = SimpleDateFormat("dd.MM.yyyy").format(System.currentTimeMillis())

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd.MM.yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                textViewNewDate.text = sdf.format(cal.time)

            }
        DatePickerDialog(
            this@ChangeExpirationDateActivity, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
        textViewNewDate.text = "$day.$month.$year"
        expirationDate = textViewNewDate.text.toString()

    }

    private fun updateDate(expirationDate : String){
        val intentUpdate = Intent(applicationContext, ProductDetailsActivity::class.java)
        val dbHelper = DataBaseHandler(this)
        intentUpdate.putExtra("expirationDate", textViewNewDate.text.toString())
        if (intent.hasExtra("id")) id = intent.getStringExtra("id")

        val success = dbHelper.updateExpirationDate(id, expirationDate)

        if (success) {
            if (intent.hasExtra("quantity"))   quantity = intent.getStringExtra("quantity")
            intentUpdate.putExtra("quantity", quantity)
            if (intent.hasExtra("name"))   name = intent.getStringExtra("name")
            intentUpdate.putExtra("name", name)
            if (intent.hasExtra("purchaseDate"))   purchaseDate = intent.getStringExtra("purchaseDate")
            intentUpdate.putExtra("purchaseDate", purchaseDate)
            startActivity(intentUpdate)
            Toast.makeText(applicationContext,getString(R.string.succesOfUpdateProduct), Toast.LENGTH_SHORT).show()
        }


    }



}
