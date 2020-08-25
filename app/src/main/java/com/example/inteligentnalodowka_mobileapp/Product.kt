package com.example.inteligentnalodowka_mobileapp

import java.util.*

class Product {

    var id : String
    var nameProduct : String
    var expirationDate : String
    var purchaseDate : String
    var quantity : String
    var type : String
    var eanCode : String
    var afterExpirationDate : String
    var isSelected : String

    constructor(id : String, nameProduct : String, expirationDate: String, purchaseDate: String, quantity : String, type : String, eanCode : String, afterExpirationDate :String, isSelected :String){
        this.id = id
        this.nameProduct = nameProduct
        this.expirationDate = expirationDate
        this.purchaseDate = purchaseDate
        this.quantity = quantity
        this.type = type
        this.eanCode = eanCode
        this.afterExpirationDate = afterExpirationDate
        this.isSelected = isSelected
    }

}