package com.example.inteligentnalodowka_mobileapp.Scan

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.inteligentnalodowka_mobileapp.R
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_scan_prroducts.*

class ScanPrroductsActivity : AppCompatActivity() {

    var eanCode = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan_prroducts)

        buttonScanProduct.setOnClickListener {
            run {
                IntentIntegrator(this@ScanPrroductsActivity).initiateScan();
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if(result != null){

            if(result.contents != null){
                eanCode = result.contents
                textViewValue.text = eanCode
            } else {
                Toast.makeText(this, getString(R.string.scanfailed), Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
