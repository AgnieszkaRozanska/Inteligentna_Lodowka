package com.example.inteligentnalodowka_mobileapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.inteligentnalodowka_mobileapp.Fridge.FridgeActivity
import com.example.inteligentnalodowka_mobileapp.Recipies.ShowAllRecipesActivity
import com.example.inteligentnalodowka_mobileapp.Scan.ScanPrroductsActivity
import com.example.inteligentnalodowka_mobileapp.ShoppingList.ShoppingListActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
}
