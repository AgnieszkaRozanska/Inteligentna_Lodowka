package com.example.inteligentnalodowka_mobileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.inteligentnalodowka_mobileapp.ShoppingList.ShoppingProduct


const  val DATABASE_NAME = "Fridge.db"

//TABELA PRODUKTÓW UŻYTKOWNIKA W LODÓWCE

const val PRODUCTS_TABLE_NAME = "Products"
const val ID_PRODUCT = "ID_Product"
const val NAME_PRODUCT = "Name_Product"
const val EXPIRATION_DATE = "Expiration_date"
const val QUANTITY = "Quantity_Product"
const val TYPE = "Type_Product"
const val PURCHASE_DATE = "Purchase_date"
const val EAN_PRODUCT_FROM_DATABASE = "ID_Product_from_Database"
const val AFTER_EXPIRATION_DATE = "After_Expiration_Date"
const val IS_SELECTED = "Is_Selected"



//BAZA PRODUKTOW - TABELA WSTEPNA
const val PRODUCTS_DATABASE_TABLE_NAME = "Products_Database"
const val ID_PRODUCT_DATABASE = "ID_Product_Database"
const val EAN_QR_CODE = "Code"
const val NAME_PRODUCT_DATABASE = "Name_Product_Database"

// TABELA DOTYCZACA lISTY ZAKUPOW
const val SHOPPING_LIST_TABLE_NAME = "Shopping_List"
const val ID_SHOPPING_ITEM = "ID_Shopping_Item"
const val NAME_SHOPPING_ITEM = "Name"
const val HOW_MUCH = "How_Much"
const val TYPE_SHOPPING_ITEM = "Type_Shopping_Item"
const val IF_BUY = "If_Buy"



//TABELA PRODUKTY
const val SQL_CREATE_TABLE_PRODUCTS = ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_TABLE_NAME +" (" +
        ID_PRODUCT + " TEXT PRIMARY KEY," +
        NAME_PRODUCT + " TEXT NOT NULL," +
        EXPIRATION_DATE + " TEXT," +
        QUANTITY + " TEXT," +
        EAN_PRODUCT_FROM_DATABASE + " TEXT," +
        TYPE + " TEXT," +
        AFTER_EXPIRATION_DATE + " TEXT," +
        PURCHASE_DATE + " TEXT," +
        IS_SELECTED + " TEXT," +
        "FOREIGN KEY(" + EAN_PRODUCT_FROM_DATABASE + ") REFERENCES " + PRODUCTS_DATABASE_TABLE_NAME + "(" + ID_PRODUCT_DATABASE +"))" )



//BAZA PRODUKTOW
const val SQL_CREATE_TABLE_PRODUCTS_DATABASE = ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_DATABASE_TABLE_NAME +" (" +
        ID_PRODUCT_DATABASE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        EAN_QR_CODE + " TEXT NOT NULL," +
        NAME_PRODUCT_DATABASE + " TEXT NOT NULL)")


// TABELA DOTYCZACA LISTY ZAKUPOW
const val SQL_CREATE_TABLE_SHOPPING_LIST = ("CREATE TABLE IF NOT EXISTS "  + SHOPPING_LIST_TABLE_NAME +" (" +
        ID_SHOPPING_ITEM + " TEXT PRIMARY KEY," +
        NAME_SHOPPING_ITEM + " TEXT NOT NULL," +
        TYPE_SHOPPING_ITEM + " TEXT," +
        HOW_MUCH + " TEXT NOT NULL," +
        IF_BUY + " INT)")




//TABALA PRODUKTY
const val SQL_DELETE_TABLE_PRODUCTS= "DROP TABLE IF EXISTS $PRODUCTS_TABLE_NAME"


//BAZA PRODUKTOW
const val SQL_DELETE_TABLE_PRODUCTS_DATABASE = "DROP TABLE IF EXISTS $PRODUCTS_DATABASE_TABLE_NAME"

//TABELA DOTYCZACA LISTY ZAKUPOW
const val SQL_DELETE_TABLE_SHOPPING_LIST = "DROP TABLE IF EXISTS $SHOPPING_LIST_TABLE_NAME"


class DataBaseHandler(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_PRODUCTS)
        db.execSQL(SQL_CREATE_TABLE_PRODUCTS_DATABASE)
        db.execSQL(SQL_CREATE_TABLE_SHOPPING_LIST)


    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE_PRODUCTS)
        db.execSQL(SQL_DELETE_TABLE_PRODUCTS_DATABASE)
        db.execSQL(SQL_DELETE_TABLE_SHOPPING_LIST)
        onCreate(db)
    }

    fun insertData(data: String)
    {
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(ID_PRODUCT, data)
        cv.put(NAME_PRODUCT, data)
    }

    fun addProduct(product : Product):Boolean
    {
        val db=this.writableDatabase
        val cv = ContentValues()
        cv.put(ID_PRODUCT,  product.id)
        cv.put(NAME_PRODUCT, product.nameProduct)
        cv.put(EXPIRATION_DATE, product.expirationDate)
        cv.put(PURCHASE_DATE, product.purchaseDate)
        cv.put(QUANTITY, product.quantity)
        cv.put(TYPE, product.type)
        cv.put(EAN_PRODUCT_FROM_DATABASE, product.eanCode)
        cv.put(AFTER_EXPIRATION_DATE,product.afterExpirationDate)
        cv.put(IS_SELECTED,product.isSelected)

        val result= db.insert(PRODUCTS_TABLE_NAME, null, cv)

        db.close()
        return !result.equals(-1)
    }

    fun addDatabaseProduct(product : DatabaseProduct)
    {
        val db=this.writableDatabase
        val cv = ContentValues()
        cv.put(NAME_PRODUCT_DATABASE, product.nameProduct)
        //cv.put(ID_PRODUCT_DATABASE,  product.id)
        cv.put(EAN_QR_CODE, product.eanCode)

        val result= db.insert(PRODUCTS_DATABASE_TABLE_NAME, null, cv)
        db.close()
    }



    fun getAllDatabaseProducts(): ArrayList<DatabaseProduct>
    {
        val allProductsList= ArrayList<DatabaseProduct>()
        val db= readableDatabase

        val cursor=db.rawQuery("SELECT * FROM $PRODUCTS_DATABASE_TABLE_NAME", null)
        if(cursor!= null)
        {
            if(cursor.moveToNext())
            {
                do{
                    val id= cursor.getString(cursor.getColumnIndex(ID_PRODUCT_DATABASE))
                    val name=cursor.getString(cursor.getColumnIndex(NAME_PRODUCT_DATABASE))
                    val ean = cursor.getString(cursor.getColumnIndex(EAN_QR_CODE))

                    val product = DatabaseProduct(id, name, ean)
                    allProductsList.add(product)
                }while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return allProductsList
    }
    fun getDatabaseProduct(eanCode : String): DatabaseProduct?
    {
        val db= readableDatabase
        val cursor=db.rawQuery("SELECT * FROM $PRODUCTS_DATABASE_TABLE_NAME WHERE $EAN_QR_CODE='$eanCode'", null)
        if(cursor!= null)
        {
            if(cursor.moveToNext())
            {
                val id= cursor.getString(cursor.getColumnIndex(ID_PRODUCT_DATABASE))
                val name=cursor.getString(cursor.getColumnIndex(NAME_PRODUCT_DATABASE))
                val ean = cursor.getString(cursor.getColumnIndex(EAN_QR_CODE))

                cursor.close()
                db.close()
                return DatabaseProduct(id, name, ean)
            }
        }
        cursor.close()
        db.close()
        return null
    }

    fun getAllProducts(): ArrayList<Product>
    {
        val allProductsList= ArrayList<Product>()
        val db= readableDatabase

        val cursor=db.rawQuery("SELECT * FROM $PRODUCTS_TABLE_NAME", null)
        if(cursor!= null)
        {
            if(cursor.moveToNext())
            {
                do{
                    val id= cursor.getString(cursor.getColumnIndex(ID_PRODUCT))
                    val name=cursor.getString(cursor.getColumnIndex(NAME_PRODUCT))
                    val date = cursor.getString(cursor.getColumnIndex(EXPIRATION_DATE))
                    val purchaseDate = cursor.getString(cursor.getColumnIndex(PURCHASE_DATE))
                    val productType=cursor.getString(cursor.getColumnIndex(TYPE))
                    val quantity =cursor.getString(cursor.getColumnIndex(QUANTITY))
                    val eanCodeDatabase = cursor.getString(cursor.getColumnIndex(EAN_PRODUCT_FROM_DATABASE))
                    val afterExpirationDate = cursor.getString(cursor.getColumnIndex(
                        AFTER_EXPIRATION_DATE))
                    val isSelected = cursor.getString(cursor.getColumnIndex(IS_SELECTED))

                    val product = Product(id, name, date, purchaseDate, quantity, productType, eanCodeDatabase,afterExpirationDate,isSelected)
                    allProductsList.add(product)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return allProductsList
    }


    fun removeProduct(id: String): Boolean
    {
        try {
            val db=this.writableDatabase
            db.delete(PRODUCTS_TABLE_NAME, "$ID_PRODUCT=?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun updateQuantityOfProducts(id:String, quantity: String):Boolean{
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(QUANTITY, quantity)
            db.update(PRODUCTS_TABLE_NAME, cv, "ID_PRODUCT =?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun updateAfterExpirationDate(id:String,bool:String):Boolean{
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(AFTER_EXPIRATION_DATE, bool)
            db.update(PRODUCTS_TABLE_NAME, cv, "ID_PRODUCT =?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun updateExpirationDate(id:String, date: String):Boolean{
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(EXPIRATION_DATE, date)
            db.update(PRODUCTS_TABLE_NAME, cv, "ID_PRODUCT =?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun updateIsSelected(id:String, selected: String):Boolean{
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(IS_SELECTED, selected)
            db.update(PRODUCTS_TABLE_NAME, cv, "ID_PRODUCT =?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }


    fun findProduct(id:String) : Product?{
        val db= readableDatabase
        val cursor=db.rawQuery("SELECT * FROM $PRODUCTS_TABLE_NAME WHERE $ID_PRODUCT='$id'", null)
        if(cursor!= null)
        {
            if(cursor.moveToNext())
            {
                val id= cursor.getString(cursor.getColumnIndex(ID_PRODUCT))
                val name=cursor.getString(cursor.getColumnIndex(NAME_PRODUCT))
                val dateExpiration = cursor.getString(cursor.getColumnIndex(EXPIRATION_DATE))
                val datePurchase = cursor.getString(cursor.getColumnIndex(PURCHASE_DATE))
                val quantity = cursor.getString(cursor.getColumnIndex(QUANTITY))
                val productType=cursor.getString(cursor.getColumnIndex(TYPE))
                val eanCodeDatabase = cursor.getString(cursor.getColumnIndex(EAN_PRODUCT_FROM_DATABASE))
                val afterExpirationDate = cursor.getString(cursor.getColumnIndex(
                    AFTER_EXPIRATION_DATE))
                val isSelected = cursor.getString(cursor.getColumnIndex(IS_SELECTED))

                cursor.close()
                db.close()
                return Product(id, name, dateExpiration, datePurchase, quantity, productType, eanCodeDatabase, afterExpirationDate, isSelected)
            }
        }
        cursor.close()
        db.close()
        return null
    }


    fun inscreaseQuantityOfProducts(id:String, count: String):Boolean{
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(QUANTITY, count)
            db.update(PRODUCTS_TABLE_NAME, cv, "ID_PRODUCT =?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }

        return true
    }

    fun addShoppingProduct(shoppingProduct : ShoppingProduct)
    {
        val db=this.writableDatabase
        val cv = ContentValues()
        cv.put(ID_SHOPPING_ITEM,  shoppingProduct.id)
        cv.put(NAME_SHOPPING_ITEM, shoppingProduct.nameShoppingProduct)
        cv.put(HOW_MUCH, shoppingProduct.howMuch)
        cv.put(TYPE_SHOPPING_ITEM, shoppingProduct.type)
        cv.put(IF_BUY, shoppingProduct.ifBuy)

        val result= db.insert(SHOPPING_LIST_TABLE_NAME, null, cv)
        db.close()
    }

    fun getShoppingList() :ArrayList<ShoppingProduct>{

        val shoppingList= ArrayList<ShoppingProduct>()
        val db= readableDatabase

        val cursor=db.rawQuery("SELECT * FROM $SHOPPING_LIST_TABLE_NAME", null)
        if(cursor!= null)
        {
            if(cursor.moveToNext())
            {
                do{
                    val id= cursor.getString(cursor.getColumnIndex(ID_SHOPPING_ITEM))
                    val name=cursor.getString(cursor.getColumnIndex(NAME_SHOPPING_ITEM))
                    val howMuch = cursor.getString(cursor.getColumnIndex(HOW_MUCH))
                    val type= cursor.getString(cursor.getColumnIndex(TYPE_SHOPPING_ITEM))
                    val ifBuy =  cursor.getString(cursor.getColumnIndex(IF_BUY)).toInt()

                    val shoppingProduct = ShoppingProduct(id, name, howMuch, type,  ifBuy)
                    shoppingList.add(shoppingProduct)
                }while (cursor.moveToNext())
            }
        }
        cursor.close()
        db.close()
        return shoppingList
    }

    fun updateIsChecked(id:String):Boolean{
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(IF_BUY, 1)
            db.update(SHOPPING_LIST_TABLE_NAME, cv, "ID_SHOPPING_ITEM =?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun updateIsNotChecked(id:String):Boolean{
        try {
            val db = this.writableDatabase
            val cv = ContentValues()
            cv.put(IF_BUY, 0)
            db.update(SHOPPING_LIST_TABLE_NAME, cv, "ID_SHOPPING_ITEM =?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }

    fun getAllDatabaseProductsNames(): ArrayList<String>
    {
        val resultsList= ArrayList<String>()
        val db= readableDatabase

        val cursor=db.rawQuery("SELECT * FROM $PRODUCTS_DATABASE_TABLE_NAME", null)
        if(cursor!= null)
        {
            if(cursor.moveToNext())
            {
                do{
                    val name=cursor.getString(cursor.getColumnIndex(NAME_PRODUCT_DATABASE))
                    resultsList.add(name)
                }while (cursor.moveToNext())
            }
        }

        cursor.close()
        db.close()
        return resultsList
    }

    fun removeShoppingProduct(id: String): Boolean
    {
        try {
            val db=this.writableDatabase
            db.delete(SHOPPING_LIST_TABLE_NAME, "$ID_SHOPPING_ITEM=?", arrayOf(id))
            db.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
            return false
        }
        return true
    }


}

