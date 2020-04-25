package com.example.inteligentnalodowka_mobileapp

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


const  val DATABASE_NAME = "Lodowka.db"

//TABELA PRODUKTÓW UŻYTKOWNIKA W LODÓWCE

const val PRODUKTY_TABLE_NAME = "Produkty"
const val ID_PRODUKTU = "ID"
const val NAZWA_PRODUKTU = "Nazwa"
const val DATA_PRZYDATNOSCI = "Data_przydatnosci"
const val ILOSC = "Ilosc"
const val TYP = "Typ"

//TABELA PRZEPISÓW

const val PRZEPISY_TABLE_NAME = "Przepisy"
const val ID_PRZEPISU = "ID"
const val NAZWA_PRZEPISU = "Nazwa"
const val CZAS_WYKONANIA = "Czas_wykonania"
const val OPIS = "Opis"

//TABELA PRODUTKY DO PRZEPISOW
const val PRODUKTY_DO_PRZEPISOW_TABLE_NAME = "Produkty_do_przepisow"
const val ID_PRZEPISU2 = "Id_przepisu"
const val ID_PRODUKTU2 = "Id_produktu"
const val ILOSC_PRODUKTU = "Ilosc"


//TABELA PRODUKTY
const val SQL_CREATE_TABLE_PRODUKTY = ("CREATE TABLE IF NOT EXISTS "  + PRODUKTY_TABLE_NAME +" (" +
        ID_PRODUKTU + " TEXT PRIMARY KEY," +
        NAZWA_PRODUKTU + " TEXT NOT NULL," +
        DATA_PRZYDATNOSCI + " DATETIME," +
        ILOSC + " TEXT" +
        TYP + " TEXT)")

//TABELA PRZEPISY
const val SQL_CREATE_TABLE_PRZEPISY= ("CREATE TABLE IF NOT EXISTS "  + PRZEPISY_TABLE_NAME +" (" +
        ID_PRZEPISU + " TEXT PRIMARY KEY," +
        NAZWA_PRZEPISU + " TEXT NOT NULL," +
        CZAS_WYKONANIA + " TEXT," +
        OPIS + " TEXT)")

//TABELA PRODUKTY DO PRZEPISOW
const val SQL_CREATE_TABLE_PRODUKTY_DO_PRZEPISOW= ("CREATE TABLE IF NOT EXISTS "  + PRODUKTY_DO_PRZEPISOW_TABLE_NAME +" (" +
        ID_PRZEPISU2 + " TEXT NOT NULL," +
        "FOREIGN KEY(Id_przepisu) REFERENCES Przepisy(ID," +
        ID_PRODUKTU2 + " TEXT NOT NULL," +
        "FOREIGN KEY(Id_produktu) REFERENCES Produkty(ID)," +
        ILOSC_PRODUKTU + " TEXT)")

//TABALA PRODUKTY
const val SQL_DELETE_TABLE_PRODUKTY = "DROP TABLE IF EXISTS $PRODUKTY_TABLE_NAME"


//TABELA PRZEPISY
const val SQL_DELETE_TABLE_PRZEPISY = "DROP TABLE IF EXISTS $PRZEPISY_TABLE_NAME"

//TABELA PRODUKTY DO PRZEPISOW
const val SQL_DELETE_TABLE_PRODUKTY_DO_PRZEPISOW = "DROP TABLE IF EXISTS $PRODUKTY_DO_PRZEPISOW_TABLE_NAME"

class SQLConector(context: Context): SQLiteOpenHelper(context,
    DATABASE_NAME, null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        // tutaj tworzysz tabele:
        db.execSQL(SQL_CREATE_TABLE_PRODUKTY)
        db.execSQL(SQL_CREATE_TABLE_PRZEPISY)
        db.execSQL(SQL_CREATE_TABLE_PRODUKTY_DO_PRZEPISOW)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // tutaj usuwasz tabele
        db.execSQL(SQL_DELETE_TABLE_PRODUKTY)
        db.execSQL(SQL_DELETE_TABLE_PRZEPISY)
        db.execSQL(SQL_DELETE_TABLE_PRODUKTY_DO_PRZEPISOW)
        onCreate(db)
    }
}