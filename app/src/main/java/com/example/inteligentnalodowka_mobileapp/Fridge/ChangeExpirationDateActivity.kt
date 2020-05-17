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
import java.text.SimpleDateFormat
import java.util.*


class ChangeExpirationDateActivity : AppCompatActivity() {

    private var id = ""
    private var name = ""
    private var quantity = ""
    private var expirationDate = ""

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

    private fun setVaues() {
        if (intent.hasExtra("name")) textViewNameOfProduct.text = intent.getStringExtra("name")
        if (intent.hasExtra("expirationDate")) textPreviousDate.text =
            intent.getStringExtra("expirationDate")
        if (intent.hasExtra("id")) id = intent.getStringExtra("id")
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
            startActivity(intentUpdate)
            Toast.makeText(applicationContext,getString(R.string.succesOfUpdateProduct), Toast.LENGTH_SHORT).show()
        }


    }



}
