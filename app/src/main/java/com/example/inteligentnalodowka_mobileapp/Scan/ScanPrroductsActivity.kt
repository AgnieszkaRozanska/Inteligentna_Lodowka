package com.example.inteligentnalodowka_mobileapp.Scan

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.inteligentnalodowka_mobileapp.*
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
    var list_of_types= arrayOf("Wybierz typ", "Warzywa", "Owoce", "Nabiał", "Słodycze", "Przekąski", "Mięso", "Ryby", "Produkty zbożowe", "Napoje", "Inne")
    var typeProduct = ""
    var idProduct = ""
    var eanCodeDatabase = "Brak"
    var flagIfNotExists = false

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

                if(checkifExistsProdukt(textViewNameProduct.text.toString(), spinnerTypeOfProduct.getSelectedItem().toString())){
                    updateProduct(idProduct)
                }else{
                    addProduct(this)
                }


            }
        }

        buttonAddProductWithoutScan.setOnClickListener {
            setVisibilityItems()
            textViewInfo2.setVisibility(View.VISIBLE)
        }


        //declare the animation
        val animation_for_buttonScan = AnimationUtils.loadAnimation(this, R.anim.animationbuttonscanproducts)
        val animation_for_buttonAdd = AnimationUtils.loadAnimation(this, R.anim.animationbuttonaddproduct)

        val buttonScan = findViewById(R.id.buttonScanProduct) as Button
        val buttonAddWithoutScan = findViewById(R.id.buttonAddProductWithoutScan) as Button


        // set the animation
        buttonScan.startAnimation(animation_for_buttonScan)
        buttonAddWithoutScan.startAnimation(animation_for_buttonAdd)


    }

    override fun onBackPressed() {
        val intentOnBackPress = Intent(applicationContext, MainActivity::class.java)
        startActivity(intentOnBackPress)
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
        var db = DataBaseHandler(this)
        var product = db.getDatabaseProduct(eanCode)
        if(product==null)
        {
            alertDialogNoProductInDatabase()
            eanCodeDatabase = eanCode
        }else
        {
            setVisibilityItems()
            textViewNameProduct.setText(product.nameProduct)
            eanCodeDatabase = product.eanCode
        }
    }


    private fun alertDialogNoProductInDatabase() {
        setVisibilityItems()
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.AlertDialogTitle))
        builder.setMessage(getString(R.string.AlertDialogMessageNoProductInDatabase))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int ->
            flagIfNotExists = true
        }
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
        textViewFinalPurDate.setVisibility(View.VISIBLE)
        textViewPurDate.setVisibility(View.VISIBLE)
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
        var purdate = textViewPurDate.text.toString()
        var after = "false"

        var product = Product(
            idProduct,
            name,
            date,
            purdate,
            quantity,
            type,
            eanCodeDatabase,
            after
        )

        val success = dbHelper.addProduct(product)
        if(success){
            Toast.makeText(this, getString(R.string.addProduct), Toast.LENGTH_LONG).show()
            if(flagIfNotExists){
                addScanedProductToDatabaseOfPRoducts()
            }
            alertDialogAddProduct()
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

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = current.format(formatter)
        setDate(formatted)
        setPurchaseDate(formatted)
        return  formatted
    }

    private fun setDate(currentDate : String){

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        var dateEndFormatDate = LocalDate.parse(currentDate, formatter)


        textViewDate.setText("Brak")

        textViewDate.setOnClickListener {
            var cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd.MM.yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    textViewDate.text = sdf.format(cal.time)

                }
            DatePickerDialog(
                this@ScanPrroductsActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
            val date = "$day.$month.$year"
            textViewDate.setText(date.toString())
        }

    }

    private fun setPurchaseDate(currentDate : String){


        textViewPurDate.setText(currentDate)

        textViewPurDate.setOnClickListener {
            var cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val myFormat = "dd.MM.yyyy" // mention the format you need
                    val sdf = SimpleDateFormat(myFormat, Locale.US)
                    textViewPurDate.text = sdf.format(cal.time)

                }
            DatePickerDialog(
                this@ScanPrroductsActivity,
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
            val date = "$day.$month.$year"
            textViewPurDate.setText(date.toString())
        }

    }

    private fun alertDialogAddProduct() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Dodano produkt")
        builder.setMessage("Czy chcesz dodać kolejny produkt?")
        builder.setPositiveButton(getString(R.string.AlertDialogYes)) { dialog: DialogInterface, which: Int ->
            editTextNumberOfPackages.text.clear()
            textViewNameProduct.text.clear()
            buttonAddProduct.setVisibility(View.VISIBLE)
            eanCodeDatabase = "Brak"
            flagIfNotExists = false

            var adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, list_of_types)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerTypeOfProduct!!.setAdapter(adapter)
            
            setInvisibilityItems()
        }
        builder.setNegativeButton(getString(R.string.AlertDialogNo)) { dialogInterface: DialogInterface, i: Int ->
            goToMainWindow()
        }
        builder.show()
    }

    private fun goToMainWindow(){
        val activityGoToMainWindowy = Intent(applicationContext, MainActivity::class.java)
        startActivity(activityGoToMainWindowy)
    }


    private fun checkifExistsProdukt(name : String, type : String) : Boolean{
        val dbHelper = DataBaseHandler(this)
        var result = false
        var productsList = dbHelper.getAllProducts()

        for (i: Product in productsList) {
            if ((i.nameProduct == name && i.type == type) || (i.eanCode != "Brak" && eanCodeDatabase != "Brak" && i.eanCode == eanCodeDatabase)) {
                idProduct = i.id
                result = true
            }
        }

        return result
    }

    private fun updateProduct(id :String){
        val dbHelper = DataBaseHandler(this)
        var oldQuantity = dbHelper.findProduct(id)?.quantity.toString()
        var newQuantity = oldQuantity.toInt() + editTextNumberOfPackages.text.toString().toInt()


        val success = dbHelper.inscreaseQuantityOfProducts(id, newQuantity.toString())
        if(success){
            Toast.makeText(this, getString(R.string.addProduct), Toast.LENGTH_LONG).show()
            alertDialogAddProduct()
        }
    }


    private fun setInvisibilityItems(){
        textViewNameProduct.setVisibility(View.INVISIBLE)
        textViewNumberOfPackagesInfo.setVisibility(View.INVISIBLE)
        textViewTypeOfProductInfo.setVisibility(View.INVISIBLE)
        textViewExpirationDate.setVisibility(View.INVISIBLE)
        textViewDate.setVisibility(View.INVISIBLE)
        spinnerTypeOfProduct.setVisibility(View.INVISIBLE)
        editTextNumberOfPackages.setVisibility(View.INVISIBLE)
        textViewInformed.setVisibility(View.VISIBLE)
        textViewFinalPurDate.setVisibility(View.INVISIBLE)
        textViewPurDate.setVisibility(View.INVISIBLE)
        buttonAddProduct.setVisibility(View.INVISIBLE)

        takeCurrentDate()
    }

    private fun addScanedProductToDatabaseOfPRoducts(){
        val dbHelper = DataBaseHandler(this)
        var databaseProduct = DatabaseProduct("", textViewNameProduct.text.toString(),eanCodeDatabase)

        dbHelper.addDatabaseProduct(databaseProduct)

    }

    private fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }

}
