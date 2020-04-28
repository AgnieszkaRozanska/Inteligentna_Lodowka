package com.example.inteligentnalodowka_mobileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


const  val DATABASE_NAME = "Fridge.db"

//TABELA PRODUKTÓW UŻYTKOWNIKA W LODÓWCE

const val PRODUCTS_TABLE_NAME = "Products"
const val ID_PRODUCT = "ID_Product"
const val NAME_PRODUCT = "Name_Product"
const val EXPIRATION_DATE = "Expiration_date"
const val QUANTITY = "Quantity_Product"
const val TYPE = "Type_Product"

//TABELA PRZEPISÓW

const val RECIPES_TABLE_NAME = "Recipes"
const val ID_RECIPE = "ID_Recipe"
const val NAME_RECIPE = "Name_Recipes"
const val EXECUTION_TIME = "Execution_time"
const val DESCRIPTION = "Description"


//TABELA PRODUTKY DO PRZEPISOW
const val PRODUCTS_TO_RECIPES_TABLE_NAME = "Products_to_recipes"
const val ID_PRODUCT_TO_RECIPE = "Id_Product_to_Recipe"
const val ID_RECIPE2 = "Id_recipe2"
const val ID_PRODUCT2 = "Id_product2"
const val PRODUCT_QUANTITY = "Quantity_Product_to_Recipe"



//BAZA PRODUKTOW - TABELA WSTEPNA
const val PRODUCTS_DATABASE_TABLE_NAME = "Products_Database"
const val ID_PRODUCT_DATABASE = "ID_Product_Database"
const val EAN_QR_CODE = "Code"
const val NAME_PRODUCT_DATABASE = "Name_Product_Database"
const val TYPE_PRODUCT_DATABASE = "Type_Product_Database"




//TABELA PRODUKTY
const val SQL_CREATE_TABLE_PRODUCTS = ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_TABLE_NAME +" (" +
        ID_PRODUCT + " TEXT PRIMARY KEY," +
        NAME_PRODUCT + " TEXT NOT NULL," +
        EXPIRATION_DATE + " DATETIME," +
        QUANTITY + " TEXT," +
        TYPE + " TEXT)")

//TABELA PRZEPISY

const val SQL_CREATE_TABLE_RECIPES= ("CREATE TABLE IF NOT EXISTS "  + RECIPES_TABLE_NAME +" (" +
        ID_RECIPE + " TEXT PRIMARY KEY," +
        NAME_RECIPE+ " TEXT NOT NULL," +
        EXECUTION_TIME + " TEXT," +
        DESCRIPTION + " TEXT)")

//TABELA PRODUKTY DO PRZEPISOW

const val SQL_CREATE_TABLE_PRODUCTS_TO_RECIPES= ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_TO_RECIPES_TABLE_NAME +" (" +
        ID_PRODUCT_TO_RECIPE + " TEXT PRIMARY KEY," +
        ID_PRODUCT2 + " TEXT," +
        PRODUCT_QUANTITY + " TEXT," +
        ID_RECIPE2 + " TEXT," +
        "FOREIGN KEY(" + ID_PRODUCT2 +") REFERENCES " + PRODUCTS_TABLE_NAME + "(" + ID_PRODUCT + ")," +
        "FOREIGN KEY(" + ID_RECIPE2 + ") REFERENCES " + RECIPES_TABLE_NAME + "(" + ID_RECIPE +"))" )







//BAZA PRODUKTOW
const val SQL_CREATE_TABLE_PRODUCTS_DATABASE = ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_DATABASE_TABLE_NAME +" (" +
        ID_PRODUCT_DATABASE + " INTEGER PRIMARY KEY," +
        EAN_QR_CODE + " TEXT NOT NULL," +
        NAME_PRODUCT_DATABASE + " TEXT NOT NULL," +
        TYPE_PRODUCT_DATABASE + " TEXT)")



//TABALA PRODUKTY
const val SQL_DELETE_TABLE_PRODUCTS= "DROP TABLE IF EXISTS $PRODUCTS_TABLE_NAME"


//TABELA PRZEPISY
const val SQL_DELETE_TABLE_RECIPES = "DROP TABLE IF EXISTS $RECIPES_TABLE_NAME"

//TABELA PRODUKTY DO PRZEPISOW
const val SQL_DELETE_TABLE_PRODUCTS_TO_RECIPES = "DROP TABLE IF EXISTS $PRODUCTS_TO_RECIPES_TABLE_NAME"

//BAZA PRODUKTOW
const val SQL_DELETE_TABLE_PRODUCTS_DATABASE = "DROP TABLE IF EXISTS $PRODUCTS_DATABASE_TABLE_NAME"



class DataBaseHandler(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_PRODUCTS)
        db.execSQL(SQL_CREATE_TABLE_RECIPES)
        db.execSQL(SQL_CREATE_TABLE_PRODUCTS_TO_RECIPES)
        db.execSQL(SQL_CREATE_TABLE_PRODUCTS_DATABASE)



    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(SQL_DELETE_TABLE_PRODUCTS)
        db.execSQL(SQL_DELETE_TABLE_RECIPES)
        db.execSQL(SQL_DELETE_TABLE_PRODUCTS_TO_RECIPES)
        db.execSQL(SQL_DELETE_TABLE_PRODUCTS_DATABASE)
        onCreate(db)
    }

    fun insertData(data: String){
        val db = this.writableDatabase
        var cv = ContentValues()
        cv.put(ID_PRODUCT, data)
        cv.put(NAME_PRODUCT, data)

    }


}