package com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.inteligentnalodowka_mobileapp.Fridge.ProductDetailsActivity
import com.example.inteligentnalodowka_mobileapp.Product
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_card_view_all_products.view.*
import kotlinx.android.synthetic.main.activity_fridge.*
import kotlinx.android.synthetic.main.activity_fridge.view.*
import org.w3c.dom.Text

class ShowAllProductsAdapter(context: FridgeActivity, var productsList: ArrayList<Product>): RecyclerView.Adapter<MyViewHolder>(),
Filterable {

    internal var productsFilterList = ArrayList<Product>()

    init{
        this.productsFilterList = productsList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val cardViewProduct = layoutInflater.inflate(R.layout.activity_card_view_all_products, viewGroup, false)
        return MyViewHolder(cardViewProduct)
    }

    override fun getItemCount(): Int {
        return productsFilterList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charString: CharSequence?): FilterResults {
                val charSearch = charString.toString()
                if(charSearch.isEmpty()){
                    productsFilterList = productsList
                }
                else {
                    val resultList = ArrayList<Product>()
                    for(row in productsList){
                        if(row.nameProduct!!.toLowerCase().contains(charSearch.toLowerCase())){
                            resultList.add(row)
                        }
                    }

                    productsFilterList = resultList

                }

                val filterResults = Filter.FilterResults()

                filterResults.values = productsFilterList

                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, results: FilterResults?) {
                productsFilterList = results!!.values as ArrayList<Product>
                notifyDataSetChanged()
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val productName_cardView=productsFilterList[holder.adapterPosition].nameProduct
        val typeProduct_cardView = productsFilterList[holder.adapterPosition].type
        val countProduct_cardView = productsFilterList[holder.adapterPosition].quantity

        setImage(typeProduct_cardView, holder)

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

    fun setImage(typeProduct_cardView : String, holder: MyViewHolder){

        if(typeProduct_cardView == "Owoce"){
            holder.imageProduct.setBackgroundResource(R.drawable.fruits)
        }else if(typeProduct_cardView == "Warzywa"){
            holder.imageProduct.setBackgroundResource(R.drawable.vegetable)
        }else if(typeProduct_cardView == "Nabiał"){
            holder.imageProduct.setBackgroundResource(R.drawable.milk_products)
        }else if(typeProduct_cardView == "Produkty zbożowe"){
            holder.imageProduct.setBackgroundResource(R.drawable.flour)
        }else if(typeProduct_cardView == "Słodycze") {
            holder.imageProduct.setBackgroundResource(R.drawable.candy)
        }else if(typeProduct_cardView == "Przekąski") {
            holder.imageProduct.setBackgroundResource(R.drawable.snack)
        }else if(typeProduct_cardView == "Mięso") {
            holder.imageProduct.setBackgroundResource(R.drawable.meat)
        }else if(typeProduct_cardView == "Ryby") {
            holder.imageProduct.setBackgroundResource(R.drawable.fish)
        }else if(typeProduct_cardView == "Napoje") {
            holder.imageProduct.setBackgroundResource(R.drawable.drinks)
        }else{
            holder.imageProduct.setBackgroundResource(R.drawable.shopping_bag_red)
        }
    }


}

class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    var productName : TextView = view.findViewById(R.id.ProductName_cardView)
    var productType : TextView = view.findViewById(R.id.Type_cardView)
    var productCount : TextView = view.findViewById(R.id.Count_cardView)
    var cardView : CardView = view.findViewById(R.id.cardView_product)
    var imageProduct : ImageView = view.findViewById(R.id.imageViewProduct)

}