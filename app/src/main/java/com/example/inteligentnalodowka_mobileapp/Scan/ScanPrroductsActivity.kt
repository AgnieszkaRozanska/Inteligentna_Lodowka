package com.example.inteligentnalodowka_mobileapp.Scan

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.Product
import com.example.inteligentnalodowka_mobileapp.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_scan_prroducts.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class ScanPrroductsActivity : AppCompatActivity() {

    var eanCode = ""
    var flagScanned = false
    var list_of_types= arrayOf("Wybierz typ", "Warzywa", "Owoce", "Nabiał", "Słodycze", "Przekąski", "Mięso", "Ryby", "Produkty zbożowe", "Inne")
    var typeProduct = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_prroducts)

        buttonScanProduct.setOnClickListener {
            run {
                IntentIntegrator(this@ScanPrroductsActivity).initiateScan();
            }
        }

        var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_types)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeOfProduct!!.setAdapter(adapter)

        buttonAddProduct.setOnClickListener{
            var result : Boolean = checkCorrectOfData()
            if(result){
                addProduct(this)
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                eanCode = result.contents
                //textViewValue.text = eanCode
                checkEanCode(eanCode)
            } else {
                Toast.makeText(this, getString(R.string.scanfailed), Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun checkEanCode(eanCode : String){

        // tu bedzie kod sprawdzający, czy w api mamy produkt czy też nie
        // jeżeli go nie będzie mamy alert dialog

        alertDialogNoProductInDatabase()

    }


    private fun alertDialogNoProductInDatabase() {
        setVisibilityItems()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.AlertDialogTitle))
        builder.setMessage(getString(R.string.AlertDialogMessageNoProductInDatabase))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }

    private fun setVisibilityItems(){
        textViewNameProduct.setVisibility(View.VISIBLE)
        textViewNumberOfPackagesInfo.setVisibility(View.VISIBLE)
        textViewTypeOfProductInfo.setVisibility(View.VISIBLE)
        textViewExpirationDate.setVisibility(View.VISIBLE)
        textViewDate.setVisibility(View.VISIBLE)
        spinnerTypeOfProduct.setVisibility(View.VISIBLE)
        editTextNumberOfPackages.setVisibility(View.VISIBLE)
        textViewInformed.setVisibility(View.INVISIBLE)
        buttonAddProduct.setVisibility(View.VISIBLE)
        takeCurrentDate()
    }


    private fun addProduct(view : ScanPrroductsActivity){
        val dbHelper = DataBaseHandler(this)
        val idProduct = UUID.randomUUID().toString()
        var name = textViewNameProduct.text.toString()
        var quantity = editTextNumberOfPackages.text.toString()
        var type = spinnerTypeOfProduct.getSelectedItem().toString();
        var date = textViewDate.text.toString()

        var product = Product(
            idProduct,
            name,
            date,
            quantity,
            type
        )

        val success = dbHelper.addProduct(product)
        if(success){
            Toast.makeText(this, getString(R.string.addProduct), Toast.LENGTH_LONG).show()
        }
    }

    fun checkCorrectOfData() : Boolean{
        var name = textViewNameProduct.text.toString()
        var quantity = editTextNumberOfPackages.text.toString()
        var type = spinnerTypeOfProduct.getSelectedItem().toString();
        var result = true;

        if(name.isEmpty() || quantity.isEmpty() || type == "Wybierz typ")
        {
            result = false
            alertDialogLackOfData()
        }
        return result
    }

    private fun alertDialogLackOfData() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.AlertDialogTitleLackOfData))
        builder.setMessage(getString(R.string.AlertDialogMessageLackOfData))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }

    private fun takeCurrentDate() : String{
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val formatted = current.format(formatter)
        setDate(formatted)
        return  formatted
    }

    private fun setDate(currentDate : String){

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        var dateEndFormatDate = LocalDate.parse(currentDate, formatter)
        var period = Period.of(0, 0, 7)
        var modifiedDate = dateEndFormatDate.plus(period)

        textViewDate.setText(modifiedDate.toString())
    }

    private fun checkifExistsProdukt(){
        // w przyszłości trzeba będzie sprawdzać czy produkt istnieje i updatować ilość
    }

    private fun ifExistsProdukt(){
        // update ilości produktów już w bazie
    }

    private fun LoadApi():
            Array<Array<String>>{
        val fileName = "Api.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        val lines: List<String> = inputString.split("\n")
        var APIData = arrayOf<Array<String>>()
        for (i in 0..lines.size-1) {
            var array = arrayOf<String>()
            for (j in 0..2) {
                array += ""
            }
            APIData += array
        }
        for (i in 0..lines.size-2)
        {
            val temp = lines[i].split(",")
            APIData[i][0] = temp[0]
            APIData[i][1] = temp[1]
            if (temp.size > 3) {
                for (j in 2..temp.size-1) {
                    APIData[i][2] += temp[j] + "/"
                }
            } else {
                APIData[i][2] = temp[2]
            }
        }
        return APIData
    }

}
