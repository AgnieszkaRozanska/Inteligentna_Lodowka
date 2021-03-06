package com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.Fridge.MultiDeleteAdapter
import com.example.inteligentnalodowka_mobileapp.MainActivity
import com.example.inteligentnalodowka_mobileapp.Product
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_fridge.*
import kotlinx.android.synthetic.main.activity_multi_delete.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class FridgeActivity : AppCompatActivity() {

    var editTextSearch: EditText? = null
    var editTextSearchDelete: EditText? = null
    var adapter: ShowAllProductsAdapter? = null
    var adapterDelete: MultiDeleteAdapter? =null
    var textViewBrakProduktu: TextView? = null
    var textViewBrakProduktuDelete: TextView? = null
    var productsAfterExpirationDate = ArrayList<Product>()
    var productList = ArrayList<Product>()
    var isMultiDelete: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkExpirationDate()
        setContentView(R.layout.activity_fridge)

        val dbHelper = DataBaseHandler(this)
        dbHelper.writableDatabase
        productList = dbHelper.getAllProducts()
        isMultiDelete = "false"


        toolbar.inflateMenu(R.menu.menu_fridge)
        toolbar.setOnMenuItemClickListener {


            if (it.itemId==R.id.filtruj){
                Filters(productList)
            }
            if (it.itemId==R.id.sortuj){
                Sort(productList)
            }
            if (it.itemId==R.id.wyczysc){
                productList.clear()
                productList = dbHelper.getAllProducts()
                textViewBrakProduktu!!.visibility = TextView.INVISIBLE
                onResume()
            }
            if(it.itemId==R.id.usun){
                val dbHelper = DataBaseHandler(this)
                dbHelper.writableDatabase
                val  productListDelete = dbHelper.getAllProducts()
                Usun(productListDelete)

            }
            true
        }

        setTextIfListIsEmpty()




        editTextSearch = findViewById(R.id.editTextSearchProducts)
        textViewBrakProduktu = findViewById(R.id.textViewBrakProduktu)

        editTextSearch!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter?.filter?.filter(s.toString())

                if (adapter?.productsFilterList.isNullOrEmpty()) {
                    textViewBrakProduktu!!.visibility = TextView.VISIBLE

                }
                else textViewBrakProduktu!!.visibility = TextView.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                adapter?.filter?.filter(s.toString())

                if (adapter?.productsFilterList.isNullOrEmpty()) {
                    textViewBrakProduktu!!.visibility = TextView.VISIBLE

                }
                else textViewBrakProduktu!!.visibility = TextView.INVISIBLE

            }
        })


        //declare the animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.animationapperance)
        val imageView = findViewById(R.id.imageView) as ImageView

        // set the animation
        imageView.startAnimation(animation)



    }



    override fun onBackPressed() {

        if (isMultiDelete=="true"){

            val activityGoToFridge = Intent(applicationContext, FridgeActivity::class.java)
            startActivity(activityGoToFridge)

        }
        else{
            val intentOnBackPress = Intent(applicationContext, MainActivity::class.java)
            startActivity(intentOnBackPress)
        }

    }

    override fun onResume() {
        super.onResume()
        setTextIfListIsEmpty()
        val dbHelper = DataBaseHandler(this)
        dbHelper.writableDatabase
        val  productList = dbHelper.getAllProducts()

        recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
        recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

        adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter
    }

    private fun  setTextIfListIsEmpty(){
        val sqlConector = DataBaseHandler(this)
        val  productsList=sqlConector.getAllProducts()

        if(productsList.isNullOrEmpty()) {
            textViewInformationAboutLackOfProducts.text = "Brak produktów"
            textViewInformationAboutLackOfProducts.visibility = TextView.VISIBLE
        }
        else textViewInformationAboutLackOfProducts.visibility = TextView.INVISIBLE
    }

    private fun  checkExpirationDate(){
        val sqlConector = DataBaseHandler(this)
        val  productsList=sqlConector.getAllProducts()
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = current.format(formatter)

        for(item in productsList)
        {
            if (item.expirationDate!="Brak") {
                if(item.afterExpirationDate=="false") {
                    var dateTime =
                        LocalDate.parse(
                            item.expirationDate,
                            DateTimeFormatter.ofPattern("dd.MM.yyyy")
                        )

                    var expirationDate =
                        LocalDate.parse(formatted, DateTimeFormatter.ofPattern("dd.MM.yyyy"))

                    if (dateTime.compareTo(expirationDate) < 0) {
                        productsAfterExpirationDate.add(item)
                        val success = sqlConector.updateAfterExpirationDate(item.id, "true")
                    }
                }
                else if(item.afterExpirationDate=="true"){
                    productsAfterExpirationDate.add(item)
                }

            }

        }

        if(productsAfterExpirationDate.isNotEmpty())
            alertDialogExpirationDate()
    }


    private fun alertDialogExpirationDate() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Przeterminowane produkty")
        builder.setMessage("W lodówce znajdują się przeterminowane produkty. Czy chcesz je usunąć?")
        builder.setPositiveButton(getString(R.string.AlertDialogYes)) { dialog: DialogInterface, which: Int ->

            val dbHelper = DataBaseHandler(this)
            for (item in productsAfterExpirationDate){
                val success = dbHelper.removeProduct(item.id)

                if (success) {

                    Toast.makeText(applicationContext,getString(R.string.successOfRemoveProduct), Toast.LENGTH_SHORT).show()
                }else  alertDialogRemove()
            }
            onResume()


        }
        builder.setNegativeButton(getString(R.string.AlertDialogNo)) { dialogInterface: DialogInterface, i: Int ->
            val dbHelper = DataBaseHandler(this)
            val allsuccess = ArrayList<Boolean>()
            for (item in productsAfterExpirationDate) {
                val success = dbHelper.updateAfterExpirationDate(item.id,"neutral")
                if (success) {
                    allsuccess.add(success)

                }else  Toast.makeText(applicationContext,"Aktualizacja produktu się nie powiodła", Toast.LENGTH_SHORT).show()
                onResume()
            }
            if (!allsuccess.isEmpty()){
                Toast.makeText(applicationContext,"Produkty zostały zaktualizowane", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNeutralButton("Usuń wybrane") { dialogInterface: DialogInterface, i: Int ->
            removeChoice()
            onResume()
        }
        builder.show()
    }

    fun removeChoice() {


        val listitems = ArrayList<String>()

        for (item in productsAfterExpirationDate){

            listitems.add(item.nameProduct + " (" + item.expirationDate + ")")
        }
        val items = listitems.toTypedArray()

        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(this)



        builder.setTitle("Usuń wybrane produkty")
        builder.setMultiChoiceItems(items, null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("Usuń") { dialogInterface, i ->
            val selectedStrings = ArrayList<Product>()

            for (j in selectedList.indices) {
                selectedStrings.add(productsAfterExpirationDate[selectedList[j]])
            }



            for (item in selectedStrings){

                    val dbHelper = DataBaseHandler(this)
                    val success = dbHelper.removeProduct(item.id)

                    if (success) {

                        Toast.makeText(applicationContext,getString(R.string.successOfRemoveProduct), Toast.LENGTH_SHORT).show()
                    }else  alertDialogRemove()

            }

            val dbHelper = DataBaseHandler(this)
            for (item in productsAfterExpirationDate) {
                val success = dbHelper.updateAfterExpirationDate(item.id,"neutral")


                onResume()
            }
        }

        builder.setNegativeButton("Anuluj") { dialogInterface: DialogInterface, i: Int ->
            onResume()
        }
        builder.show()

    }

    private fun alertDialogRemove() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alertDialogMistakeTitle))
        builder.setMessage(getString(R.string.alertDialogMistakeMessage))
        builder.setPositiveButton(getString(R.string.Back)) { dialog: DialogInterface, which: Int -> }
        builder.show()
    }

    fun Filters(productList: ArrayList<Product>) {

        setTextIfListIsEmpty()

        var items= arrayOf("Wszystko","Przeterminowane","Warzywa", "Owoce", "Nabiał", "Słodycze", "Przekąski", "Mięso", "Ryby", "Produkty zbożowe", "Napoje", "Inne")


        val selectedList = ArrayList<Int>()
        val builder = AlertDialog.Builder(this)



        builder.setTitle("Filtruj według typu")
        builder.setMultiChoiceItems(items, null
        ) { dialog, which, isChecked ->
            if (isChecked) {
                selectedList.add(which)
            } else if (selectedList.contains(which)) {
                selectedList.remove(Integer.valueOf(which))
            }
        }

        builder.setPositiveButton("Zatwierdź") { dialogInterface, i ->
            val selectedStrings = ArrayList<String>()

            for (j in selectedList.indices) {
                selectedStrings.add(items[selectedList[j]])
            }




            val productFilteredList = ArrayList<Product>()

            for (item in productList){

                if (selectedStrings.contains("Warzywa") && item.type == "Warzywa"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Owoce") && item.type == "Owoce"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Nabiał") && item.type == "Nabiał"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Słodycze") && item.type == "Słodycze"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Przekąski") && item.type == "Przekąski"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Mięso") && item.type == "Mięso"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Ryby") && item.type == "Ryby"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Produkty zbożowe") && item.type == "Produkty zbożowe"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Napoje") && item.type == "Napoje"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if (selectedStrings.contains("Inne") && item.type == "Inne"){
                    if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false"){
                        productFilteredList.add(item)
                    }
                    else {
                        productFilteredList.add(item)
                    }
                }
                if(selectedStrings.contains("Wszystko")){
                    productFilteredList.add(item)
                }
                if (selectedStrings.contains("Przeterminowane") && item.afterExpirationDate!="false" && selectedStrings.size==1){
                    productFilteredList.add(item)
                }


            }



            recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
            recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productFilteredList)

            adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter

            this.productList = productFilteredList

            if (productFilteredList.isEmpty()){
                textViewBrakProduktu!!.visibility = TextView.VISIBLE
            }
            else
                textViewBrakProduktu!!.visibility = TextView.INVISIBLE


        }

        builder.setNegativeButton("Anuluj") { dialogInterface: DialogInterface, i: Int ->
            textViewBrakProduktu!!.visibility = TextView.INVISIBLE
            onResume()
        }
        builder.show()

    }


    fun Sort(productList: ArrayList<Product>){


        setTextIfListIsEmpty()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sortuj")
        builder.setMessage("Sortuj według daty:")

        builder.setPositiveButton("ważności") { dialog: DialogInterface, which: Int ->
            val builder2 = AlertDialog.Builder(this)
            builder2.setTitle("Sortuj")
            builder2.setMessage("Wybierz rodzaj sortowania:")

            builder2.setPositiveButton("Rosnąco") { dialog: DialogInterface, which: Int ->
                productList.sortBy{it.expirationDate}
                recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
                recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

                adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter

            }
            builder2.setNegativeButton("Malejąco"){dialog: DialogInterface, which: Int ->
                productList.sortByDescending { it.expirationDate }
                recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
                recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

                adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter
            }
            builder2.setNeutralButton("Anuluj"){dialog: DialogInterface, which: Int ->
                recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
                recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

                adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter

            }
            builder2.show()

        }
        builder.setNegativeButton("zakupu"){dialog: DialogInterface, which: Int ->
            val builder2 = AlertDialog.Builder(this)
            builder2.setTitle("Sortuj")
            builder2.setMessage("Wybierz rodzaj sortowania:")

            builder2.setPositiveButton("Rosnąco") { dialog: DialogInterface, which: Int ->
                productList.sortBy{it.purchaseDate}
                recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
                recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

                adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter

            }
            builder2.setNegativeButton("Malejąco"){dialog: DialogInterface, which: Int ->
                productList.sortByDescending { it.purchaseDate }
                recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
                recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

                adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter
            }
            builder2.setNeutralButton("Anuluj"){dialog: DialogInterface, which: Int ->
                recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
                recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

                adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter

            }
            builder2.show()
        }
        builder.setNeutralButton("Anuluj"){dialog: DialogInterface, which: Int ->
            recyclerViewAllProducts.layoutManager = LinearLayoutManager(this)
            recyclerViewAllProducts.adapter =  ShowAllProductsAdapter(this, productList)

            adapter = recyclerViewAllProducts.adapter as ShowAllProductsAdapter


        }
        builder.show()







    }
    fun Usun(productList: ArrayList<Product>) {

        setContentView(R.layout.activity_multi_delete)

        val animationFridgeDelete = AnimationUtils.loadAnimation(this, R.anim.animationmovefridge)
        val imageViewDelete = findViewById(R.id.imageView2) as ImageView

        // set the animation
        imageViewDelete.startAnimation(animationFridgeDelete)

        val animDelete = AnimationUtils.loadAnimation(this, R.anim.animationapperance)
        val buttonDelete = findViewById(R.id.buttonUsun) as Button

        // set the animation
        buttonDelete.startAnimation(animDelete)

        val animAnuluj = AnimationUtils.loadAnimation(this, R.anim.animationapperance)
        val buttonAnuluj = findViewById(R.id.buttonAnuluj) as Button

        // set the animation
        buttonAnuluj.startAnimation(animAnuluj)

        isMultiDelete = "true"

        recyclerViewAllProductsDelete.layoutManager = LinearLayoutManager(this)
        recyclerViewAllProductsDelete.adapter =  MultiDeleteAdapter(this, productList)

        adapterDelete = recyclerViewAllProductsDelete.adapter as MultiDeleteAdapter

        editTextSearchDelete = findViewById(R.id.editTextSearchProducts2)
        textViewBrakProduktuDelete = findViewById(R.id.textViewBrakProduktuDelete)

        editTextSearchDelete!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapterDelete?.filter?.filter(s.toString())

                if (adapterDelete?.productsFilterList.isNullOrEmpty()) {
                    textViewBrakProduktuDelete!!.visibility = TextView.VISIBLE

                }
                else textViewBrakProduktuDelete!!.visibility = TextView.INVISIBLE
            }

            override fun afterTextChanged(s: Editable?) {
                adapterDelete?.filter?.filter(s.toString())

                if (adapterDelete?.productsFilterList.isNullOrEmpty()) {
                    textViewBrakProduktuDelete!!.visibility = TextView.VISIBLE

                }
                else textViewBrakProduktuDelete!!.visibility = TextView.INVISIBLE

            }
        })







        buttonUsun.setOnClickListener {
            val dbHelper = DataBaseHandler(this)
            dbHelper.writableDatabase
            val  productList = dbHelper.getAllProducts()
            val listSuccess = ArrayList<Boolean>()

            for (item in productList){

                if(item.isSelected=="true"){
                    val success = dbHelper.removeProduct(item.id)
                    if (success){
                        listSuccess.add(success)
                        isMultiDelete = "false"
                    }
                    else
                        Toast.makeText(applicationContext,"Usunięcie produktu nie powiodło się", Toast.LENGTH_SHORT).show()

                }
            }
            if (!listSuccess.isEmpty() && listSuccess.size >1){
                Toast.makeText(applicationContext,"Produkty zostały usunięte", Toast.LENGTH_SHORT).show()
                val activityGoToFridge = Intent(applicationContext, FridgeActivity::class.java)
                startActivity(activityGoToFridge)
            }
            else if(!listSuccess.isEmpty() && listSuccess.size == 1){
                Toast.makeText(applicationContext,"Produkt został usunięty", Toast.LENGTH_SHORT).show()
                val activityGoToFridge = Intent(applicationContext, FridgeActivity::class.java)
                startActivity(activityGoToFridge)
            }
            else if(listSuccess.isEmpty()){
                alertNonChoosenProductRemove()

            }




        }
        buttonAnuluj.setOnClickListener {
            val dbHelper = DataBaseHandler(this)
            dbHelper.writableDatabase
            val  productList = dbHelper.getAllProducts()
            for (item in productList){
                val success = dbHelper.updateIsSelected(item.id,"false")
            }
            isMultiDelete = "false"

            val activityGoToFridge = Intent(applicationContext, FridgeActivity::class.java)
            startActivity(activityGoToFridge)
        }


    }



    private fun alertNonChoosenProductRemove() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Uwaga")
        builder.setMessage("Nie wybrano żadnego produktu")
        builder.setPositiveButton("Ok") { dialog: DialogInterface, which: Int ->

        }
        builder.show()
    }


}


