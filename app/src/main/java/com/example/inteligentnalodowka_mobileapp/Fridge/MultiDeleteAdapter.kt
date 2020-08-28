package com.example.inteligentnalodowka_mobileapp.Fridge

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.RecyclerView
import com.example.inteligentnalodowka_mobileapp.DataBaseHandler
import com.example.inteligentnalodowka_mobileapp.Fridge.ProductDetailsActivity
import com.example.inteligentnalodowka_mobileapp.Fridge.ShowAllProducts.FridgeActivity
import com.example.inteligentnalodowka_mobileapp.Product
import com.example.inteligentnalodowka_mobileapp.R
import kotlinx.android.synthetic.main.activity_card_view_all_products.view.*
import kotlinx.android.synthetic.main.activity_card_view_all_products_delete.view.*
import kotlinx.android.synthetic.main.activity_fridge.*
import kotlinx.android.synthetic.main.activity_fridge.view.*
import org.w3c.dom.Text
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MultiDeleteAdapter(context: FridgeActivity, var productsList: ArrayList<Product>): RecyclerView.Adapter<MyViewHolder>(),
    Filterable {

    internal var productsFilterList = ArrayList<Product>()

    init{
        this.productsFilterList = productsList
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): MyViewHolder {

        val layoutInflater = LayoutInflater.from(viewGroup.context)
        val cardViewProduct = layoutInflater.inflate(R.layout.activity_card_view_all_products_delete, viewGroup, false)
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
        val ifExpiredProduct = productsFilterList[holder.adapterPosition].afterExpirationDate
        setImage(typeProduct_cardView, holder)

        holder.productName.text = productName_cardView
        holder.productType.text = typeProduct_cardView
        holder.productCount.text = "Liczba opakowań: "+countProduct_cardView

        if(ifExpiredProduct.equals("true") || ifExpiredProduct.equals("neutral")){
            holder.imageProduct.setBackgroundResource(R.drawable.alert)
            holder.cardView.setCardBackgroundColor(Color.parseColor("#EAE1A2"))
        }
        else if (position % 2 == 1 ) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
        } else if (position % 2 != 1 ) {
            holder.cardView.setCardBackgroundColor(Color.parseColor("#CCEBF6"))
        }

        val cardViewProductDelete = holder.view.cardView_productDelete
        val context:Context = holder.view.context
        var selected : String = "false"

        cardViewProductDelete.setOnClickListener {
            val intentEdit = Intent(context, ProductDetailsActivity::class.java)

            val priductID = productsList[holder.adapterPosition].id
            val priductName =productsList[holder.adapterPosition].nameProduct
            val productExpirationDate=productsList[holder.adapterPosition].expirationDate
            val productPurchaseDate=productsList[holder.adapterPosition].purchaseDate
            val productQuantity = productsList[holder.adapterPosition].quantity
            val productType :String = productsList[holder.adapterPosition].type


            val dbHelper = DataBaseHandler(context)

            if (selected=="false"){


                    holder.cardView.setCardBackgroundColor(Color.parseColor("#03DAC5"))
                holder.imageProduct.setBackgroundResource(R.drawable.tick)
                selected = "true"
                val success = dbHelper.updateIsSelected(priductID,selected)
            }
            else if(selected=="true") {

                if (position % 2 == 1 ) {
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
                } else if (position % 2 != 1 ) {
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#CCEBF6"))
                }

                selected = "false"
                val success = dbHelper.updateIsSelected(priductID,selected)
                if(ifExpiredProduct.equals("true") || ifExpiredProduct.equals("neutral")){
                    holder.imageProduct.setBackgroundResource(R.drawable.alert)
                    holder.cardView.setCardBackgroundColor(Color.parseColor("#EAE1A2"))
                }
                else
                    setImage(productType,holder)
            }




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
    var productName : TextView = view.findViewById(R.id.ProductName_cardViewDelete)
    var productType : TextView = view.findViewById(R.id.Type_cardViewDelete)
    var productCount : TextView = view.findViewById(R.id.Count_cardViewDelete)
    var cardView : CardView = view.findViewById(R.id.cardView_productDelete)
    var imageProduct : ImageView = view.findViewById(R.id.imageViewProductDelete)



}
