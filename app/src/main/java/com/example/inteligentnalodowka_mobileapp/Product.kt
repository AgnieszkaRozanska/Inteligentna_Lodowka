package com.example.inteligentnalodowka_mobileapp

import java.util.*

class Product {

    var id : String
    var nameProduct : String
    var expirationDate : String
    var purchaseDate : String
    var quantity : String
    var type : String

    constructor(id : String, nameProduct : String, expirationDate: String, purchaseDate: String, quantity : String, type : String){
        this.id = id
        this.nameProduct = nameProduct
        this.expirationDate = expirationDate
        this.purchaseDate = purchaseDate
        this.quantity = quantity
        this.type = type
    }

}