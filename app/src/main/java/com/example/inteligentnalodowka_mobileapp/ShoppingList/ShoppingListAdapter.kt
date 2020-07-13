package com.example.inteligentnalodowka_mobileapp.ShoppingList

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.R

class ShoppingListAdapter(context: Context, var shoppingList: ArrayList<ShoppingProduct>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val cardViewProduct = layoutInflater.inflate(R.layout.activity_card_view_shopping_item, viewGroup, false)
        return MyViewHolder(cardViewProduct)
    }

    override fun getItemCount(): Int {
        return shoppingList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val productId_cardView=shoppingList[holder.adapterPosition].id
        val productName_cardView=shoppingList[holder.adapterPosition].nameShoppingProduct
        val typeProduct_cardView = shoppingList[holder.adapterPosition].type
        val countProduct_cardView = shoppingList[holder.adapterPosition].howMuch
        val ifBuy = shoppingList[holder.adapterPosition].ifBuy

        holder.shoppingProduct.text = productName_cardView + ",  "+ countProduct_cardView

        val cardViewCheckBox = holder.shoppingProduct
        val context:Context = holder.view.context

        cardViewCheckBox.setOnClickListener {
            val ID = shoppingList[holder.adapterPosition].id
            if(cardViewCheckBox.isChecked())
            {
                val dbHelper = DataBaseHandler(context)
                dbHelper.updateIsChecked(ID)
            }
            else
            {
                val dbHelper = DataBaseHandler(context)
                dbHelper.updateIsNotChecked(ID)
            }

        }
        setImage(typeProduct_cardView, holder)

        if(ifBuy == 1) holder.shoppingProduct.setChecked(true)
        if(ifBuy == 0)holder.shoppingProduct.setChecked(false)

        holder.buttonDelete.setOnClickListener {
            alertDialogRemoveProduct(context, productId_cardView)
        }



    }

    fun setImage(typeProduct_cardView : String, holder: MyViewHolder){

        if(typeProduct_cardView == "Owoce"){
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.fruits)
        }else if(typeProduct_cardView == "Warzywa"){
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.vegetable)
        }else if(typeProduct_cardView == "Nabiał"){
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.milk_products)
        }else if(typeProduct_cardView == "Produkty zbożowe"){
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.flour)
        }else if(typeProduct_cardView == "Słodycze") {
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.candy)
        }else if(typeProduct_cardView == "Przekąski") {
        holder.imageShoppingProduct.setBackgroundResource(R.drawable.snack)
        }else if(typeProduct_cardView == "Mięso") {
        holder.imageShoppingProduct.setBackgroundResource(R.drawable.meat)
        }else if(typeProduct_cardView == "Ryby") {
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.fish)
        }else if(typeProduct_cardView == "Napoje") {
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.drinks)
        }else{
            holder.imageShoppingProduct.setBackgroundResource(R.drawable.shopping_bag_red)
        }
    }


    private fun alertDialogRemoveProduct(context : Context, id : String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Czy jesteś pewien")
        builder.setMessage("Czy chcesz usunąć produkt?")
        builder.setPositiveButton("Tak") { dialog: DialogInterface, which: Int ->
            removeShoppingProduct(context, id)
        }
        builder.setNegativeButton("Nie") { dialogInterface: DialogInterface, i: Int -> }
        builder.show()
    }

    private fun removeShoppingProduct(context : Context, id : String) {
        val dbHelper = DataBaseHandler(context)
        val success = dbHelper.removeShoppingProduct(id)

        val intentEdit = Intent(context, ShoppingListActivity::class.java)
        context.startActivity(intentEdit)

    }




}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var shoppingProduct : CheckBox = view.findViewById(R.id.checkBoxProductShoppingList)
    var imageShoppingProduct : ImageView = view.findViewById(R.id.imageView_ShoppingProduct)
    var cardView : CardView = view.findViewById(R.id.cardView_product)
    var buttonDelete : Button = view.findViewById(R.id.button_delete)
}