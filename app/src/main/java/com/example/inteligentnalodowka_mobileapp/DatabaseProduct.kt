package com.example.inteligentnalodowka_mobileapp

import java.util.*

class DatabaseProduct {
    var id : String
    var nameProduct : String
    var eanCode : String

    constructor(id : String, nameProduct : String, eanCode : String){
        this.id = id
        this.nameProduct = nameProduct
        this.eanCode = eanCode
    }
}