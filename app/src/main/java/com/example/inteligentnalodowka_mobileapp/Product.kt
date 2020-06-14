package com.example.inteligentnalodowka_mobileapp

import java.util.*

class Product {

    var id : String
    var nameProduct : String
    var purchaseDate : String
    var expirationDate : String
    var quantity : String
    var type : String

    constructor(id : String, nameProduct : String, purchaseDate : String, expirationDate: String, quantity : String, type : String){
        this.id = id
        this.nameProduct = nameProduct
        this.purchaseDate = purchaseDate
        this.expirationDate = expirationDate
        this.quantity = quantity
        this.type = type
    }

}