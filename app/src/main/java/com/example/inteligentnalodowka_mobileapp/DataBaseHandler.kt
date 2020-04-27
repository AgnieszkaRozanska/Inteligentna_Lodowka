package com.example.inteligentnalodowka_mobileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


const  val DATABASE_NAME = "Fridge.db"

//TABELA PRODUKTÓW UŻYTKOWNIKA W LODÓWCE

const val PRODUCTS_TABLE_NAME = "Products"
const val ID_PRODUCT = "ID"
const val NAME_PRODUCT = "Name"
const val EXPIRATION_DATE = "Expiration_date"
const val QUANTITY = "Quantity"
const val TYPE = "Type"

//TABELA PRZEPISÓW

const val RECIPES_TABLE_NAME = "Recipes"
const val ID_RECIPE = "ID"
const val NAME_RECIPE = "Name"
const val EXECUTION_TIME = "Execution_time"
const val DESCRIPTION = "Description"

//TABELA PRODUTKY DO PRZEPISOW
const val PRODUCTS_TO_RECIPES_TABLE_NAME = "Products_to_recipes"
const val ID_RECIPE2 = "Id_recipe"
const val ID_PRODUCT2 = "Id_product"
const val PRODUCT_QUANTITY = "Quantity"

//BAZA PRODUKTOW - TABELA WSTEPNA
const val PRODUCTS_DATABASE_TABLE_NAME = "Products_Database"
const val ID_PRODUCT_DATABASE = "ID"
const val EAN_QR_CODE = "Code"
const val NAME_PRODUCT_DATABASE = "Name"
const val TYPE_PRODUCT_DATABASE = "Type"


//TABELA PRODUKTY
const val SQL_CREATE_TABLE_PRODUCTS = ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_TABLE_NAME +" (" +
        ID_PRODUCT + " TEXT PRIMARY KEY," +
        NAME_PRODUCT + " TEXT NOT NULL," +
        EXPIRATION_DATE + " DATETIME," +
        QUANTITY + " TEXT" +
        TYPE + " TEXT)")

//TABELA PRZEPISY
const val SQL_CREATE_TABLE_RECIPES= ("CREATE TABLE IF NOT EXISTS "  + RECIPES_TABLE_NAME +" (" +
        ID_RECIPE + " TEXT PRIMARY KEY," +
        NAME_RECIPE+ " TEXT NOT NULL," +
        EXECUTION_TIME + " TEXT," +
        DESCRIPTION + " TEXT)")

//TABELA PRODUKTY DO PRZEPISOW
const val SQL_CREATE_TABLE_PRODUCTS_TO_RECIPES= ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_TO_RECIPES_TABLE_NAME +" (" +
        ID_RECIPE2 + " TEXT NOT NULL," +
        "FOREIGN KEY(Id_przepisu) REFERENCES Przepisy(ID," +
        ID_PRODUCT2 + " TEXT NOT NULL," +
        "FOREIGN KEY(Id_produktu) REFERENCES Produkty(ID)," +
        PRODUCT_QUANTITY + " TEXT)")

//BAZA PRODUKTOW
const val SQL_CREATE_TABLE_PRODUCTS_DATABASE = ("CREATE TABLE IF NOT EXISTS "  + PRODUCTS_DATABASE_TABLE_NAME +" (" +
        ID_PRODUCT_DATABASE + " TEXT PRIMARY KEY," +
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

class SQLConector(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // tutaj tworzysz tabele:
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
}