package com.example.inteligentnalodowka_mobileapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts.FridgeActivity
import com.example.inteligentnalodowka_mobileapp.Recipies.ShowAllRecipesActivity
import com.example.inteligentnalodowka_mobileapp.Scan.ScanPrroductsActivity
import com.example.inteligentnalodowka_mobileapp.ShoppingList.ShoppingListActivity
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_scan_prroducts.*
import okhttp3.OkHttpClient
import java.util.*


class MainActivity : AppCompatActivity() {



    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        setContentView(R.layout.activity_main)
        setStetho()
        var db = DataBaseHandler(context)

        buttonFridge.setOnClickListener{
            goToWindowFridgeActivity()
        }
        buttonScan.setOnClickListener{
            goToWindowScanActivity()
        }
        buttonShoppingList.setOnClickListener{
            goToShopListActivity()
        }



        //declare the animation
        val header = AnimationUtils.loadAnimation(this, R.anim.header)
        val appearanceAndScale = AnimationUtils.loadAnimation(this, R.anim.appearancechangeofscale)
        val animation_for_buttonFridge = AnimationUtils.loadAnimation(this, R.anim.buttonfridge)
        val animation_for_buttonShoppingList = AnimationUtils.loadAnimation(this, R.anim.buttonshoppinglist)
        val animation_for_buttonScan = AnimationUtils.loadAnimation(this, R.anim.buttonscan)
        val headerOfApp = findViewById(R.id.imageView9) as ImageView
        val imageOfApp = findViewById(R.id.imageView7) as ImageView
        val button_go_to_Fridge = findViewById(R.id.buttonFridge) as Button
        val button_shoppingList = findViewById(R.id.buttonShoppingList) as Button
        val button_ScanProducts = findViewById(R.id.buttonScan) as Button


        // set the animation
        headerOfApp.startAnimation(header)
        imageOfApp.startAnimation(appearanceAndScale)
        button_go_to_Fridge.startAnimation(animation_for_buttonFridge)
        button_shoppingList.startAnimation(animation_for_buttonShoppingList)
        button_ScanProducts.startAnimation(animation_for_buttonScan)


        val isFirstRun =
            getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            LoadDatabaseAsync(this,db).execute()
        }

        getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE).edit()
            .putBoolean("isFirstRun", false).commit()
    }

    private fun goToWindowFridgeActivity(){
        val activityGoToFridge = Intent(applicationContext, FridgeActivity::class.java)
        startActivity(activityGoToFridge)
    }

    private fun goToWindowScanActivity(){
        val activityGoToScanActivity = Intent(applicationContext, ScanPrroductsActivity::class.java)
        startActivity(activityGoToScanActivity)
    }

    private fun goToWindowShowAllRecipesActivity(){
        val activityGoToShowAllRecipesActivity = Intent(applicationContext, ShowAllRecipesActivity::class.java)
        startActivity(activityGoToShowAllRecipesActivity)
    }

    private fun goToShopListActivity(){
        val activityGoToShopListAcyivity = Intent(applicationContext, ShoppingListActivity::class.java)
        startActivity(activityGoToShopListAcyivity)
    }

    private fun setStetho(){

        Stetho.initializeWithDefaults(this)
        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
    }

    private fun loadApiToDatabase(db: DataBaseHandler)
    {
        var APIData = loadApiAsTxt()
        var APIData2 = apiToObjects(APIData)
        addAPIToDatabase(db,APIData2)
    }

    override fun onBackPressed() {
        finishAffinity()
    }

    private fun loadApiAsTxt():
            Array<Array<String>> {
        //ładuje Api z pliku txt i zwraca w tablicy
        val fileName = "Api.txt"
        val inputString = application.assets.open(fileName).bufferedReader().use { it.readText() }
        val lines: List<String> = inputString.split("\n")
        var APIData = arrayOf<Array<String>>()
        for (i in 0..lines.size - 1) {
            var array = arrayOf<String>()
            for (j in 0..2) {
                array += ""
            }
            APIData += array
        }
        for (i in 0..lines.size - 2) {
            val temp = lines[i].split(",")
            APIData[i][0] = temp[0]
            APIData[i][1] = temp[1]
            for (j in 2..temp.size - 1) {
                APIData[i][1] +=  "," + temp[j]
            }
        }
        return APIData
    }

    private fun apiToObjects(APIData : Array<Array<String>>):
            Array<DatabaseProduct> {
        var data = arrayOf<DatabaseProduct>()
        for (i in 0..APIData.size - 1) {
            val id = UUID.randomUUID().toString()
            data += DatabaseProduct(id, APIData[i][1], APIData[i][0])
        }
        return  data
    }

    private fun addAPIToDatabase(db: DataBaseHandler, APIData : Array<DatabaseProduct>)
    {
        for (i in 0..APIData.size - 1) {
            db.addDatabaseProduct(APIData[i])
        }
    }

    //@SuppressLint("StaticFieldLeak")
    class LoadDatabaseAsync(private var activity: MainActivity?,private var db: DataBaseHandler) : AsyncTask<String, String, String>() {

        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: String?): String {

            activity?.loadApiToDatabase(db)
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            }
        }





}
