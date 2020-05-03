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
import com.example.inteligentnalodowka_mobileapp.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_scan_prroducts.*

class ScanPrroductsActivity : AppCompatActivity() {

    var eanCode = ""
    var flagScanned = false
    var list_of_types= arrayOf("Wybierz typ", "Warzywa", "Owoce", "Nabiał", "Słodycze", "Przekąski", "Mięso", "Ryby", "Produkty zbożowe", "Inne")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_prroducts)

        buttonScanProduct.setOnClickListener {
            run {
                IntentIntegrator(this@ScanPrroductsActivity).initiateScan();
            }
        }

        val aa = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_types)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTypeOfProduct!!.setAdapter(aa)

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
        // jeżeli go nei będzie mamy alert dialog

        alertDialogNoProductInDatabase()

    }


    private fun alertDialogNoProductInDatabase() {
        textViewNameProduct.setVisibility(View.VISIBLE)
        textViewNumberOfPackagesInfo.setVisibility(View.VISIBLE)
        textViewTypeOfProductInfo.setVisibility(View.VISIBLE)
        textViewExpirationDate.setVisibility(View.VISIBLE)
        textViewDate.setVisibility(View.VISIBLE)
        spinnerTypeOfProduct.setVisibility(View.VISIBLE)
        editTextNumberOfPackages.setVisibility(View.VISIBLE)
        textViewInformed.setVisibility(View.INVISIBLE)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.AlertDialogTitle))
        builder.setMessage(getString(R.string.AlertDialogMessageNoProductInDatabase))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }



}
