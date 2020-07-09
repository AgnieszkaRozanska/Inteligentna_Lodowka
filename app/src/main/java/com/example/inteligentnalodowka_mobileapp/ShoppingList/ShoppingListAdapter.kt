package com.example.inteligentnalodowka_mobileapp.ShoppingList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
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

        if(ifBuy == 1) holder.shoppingProduct.setChecked(true)
        if(ifBuy == 0)holder.shoppingProduct.setChecked(false)

    }


}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var shoppingProduct : CheckBox = view.findViewById(R.id.checkBoxProductShoppingList)
    var imageShoppingProduct : ImageView = view.findViewById(R.id.imageView_ShoppingProduct)
    var cardView : CardView = view.findViewById(R.id.cardView_product)
}