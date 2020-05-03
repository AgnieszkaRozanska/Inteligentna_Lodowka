package com.example.inteligentnalodowka_mobileapp

import java.util.*

class Product {

    var id : String
    var nameProduct : String
    var expirationDate : Date
    var quantity : String
    var type : String

    constructor(id : String, nameProduct : String, expirationDate: Date, quantity : String, type : String){
        this.id = id
        this.nameProduct = nameProduct
        this.expirationDate = expirationDate
        this.quantity = quantity
        this.type = type
    }

}