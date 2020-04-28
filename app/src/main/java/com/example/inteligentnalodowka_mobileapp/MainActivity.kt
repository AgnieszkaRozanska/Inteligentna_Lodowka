package com.example.inteligentnalodowka_mobileapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inteligentnalodowka_mobileapp.Fridge.FridgeActivity
import com.example.inteligentnalodowka_mobileapp.Recipies.ShowAllRecipesActivity
import com.example.inteligentnalodowka_mobileapp.Scan.ScanPrroductsActivity
import com.example.inteligentnalodowka_mobileapp.ShoppingList.ShoppingListActivity
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context = this
        setContentView(R.layout.activity_main)
        setStetho()
        var db = DataBaseHandler(context)
        db.insertData("Data")

        buttonFridge.setOnClickListener{
            goToWindowFridgeActivity()
        }
        buttonScan.setOnClickListener{
            goToWindowScanActivity()
        }
        buttonRecipes.setOnClickListener{
            goToWindowShowAllRecipesActivity()
        }
        buttonShoppingList.setOnClickListener{
            goToShopListActivity()
        }


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
}
