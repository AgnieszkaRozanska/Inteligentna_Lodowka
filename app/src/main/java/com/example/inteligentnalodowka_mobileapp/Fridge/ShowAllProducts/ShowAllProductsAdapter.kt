package com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inteligentnalodowka_mobileapp.Fridge.ProductDetailsActivity
import com.example.inteligentnalodowka_mobileapp.Product
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_card_view_all_products.view.*

class ShowAllProductsAdapter(context: Context, var productsList: ArrayList<Product>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val cardViewProduct = layoutInflater.inflate(R.layout.activity_card_view_all_products, viewGroup, false)
        return MyViewHolder(cardViewProduct)
    }

    override fun getItemCount(): Int {
        return productsList.count()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val productName_cardView=productsList[holder.adapterPosition].nameProduct
        val typeProduct_cardView = productsList[holder.adapterPosition].type
        val countProduct_cardView = productsList[holder.adapterPosition].quantity

        holder.productName.text = productName_cardView
        holder.productType.text = typeProduct_cardView
        holder.productCount.text = "Liczba opakowań: "+countProduct_cardView

        if (position % 2 == 1) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#CCEBF6"))
        }

        val cardViewProduct = holder.view.cardView_product
        val context:Context = holder.view.context

        cardViewProduct.setOnClickListener {
            val intentEdit = Intent(context, ProductDetailsActivity::class.java)
            val priductID = productsList[holder.adapterPosition].id
            val priductName =productsList[holder.adapterPosition].nameProduct
            val productExpirationDate=productsList[holder.adapterPosition].expirationDate
            val productPurchaseDate=productsList[holder.adapterPosition].purchaseDate
            val productQuantity = productsList[holder.adapterPosition].quantity

            intentEdit.putExtra("id", priductID)
            intentEdit.putExtra("name", priductName)
            intentEdit.putExtra("expirationDate", productExpirationDate)
            intentEdit.putExtra("purchaseDate", productPurchaseDate)
            intentEdit.putExtra("quantity", productQuantity)

            context.startActivity(intentEdit)
        }

    }
}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var productName : TextView = view.findViewById(R.id.ProductName_cardView)
    var productType : TextView = view.findViewById(R.id.Type_cardView)
    var productCount : TextView = view.findViewById(R.id.Count_cardView)
    var cardView : CardView = view.findViewById(R.id.cardView_product)
}