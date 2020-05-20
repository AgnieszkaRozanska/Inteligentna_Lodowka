package com.example.inteligentnalodowka_mobileapp

import java.util.*

class DatabaseProduct {
    var id : String
    var nameProduct : String
    var eanCode : String
    var type : String

    constructor(id : String, nameProduct : String, eanCode : String, type : String){
        this.id = id
        this.nameProduct = nameProduct
        this.eanCode = eanCode
        this.type = type
    }
}