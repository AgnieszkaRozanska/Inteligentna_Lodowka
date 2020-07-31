package com.example.inteligentnalodowka_mobileapp.ShoppingList

class ShoppingProduct {
    var id : String
    var nameShoppingProduct : String
    var howMuch : String
    var type : String
    var ifBuy : Int

    constructor(id : String, nameShoppingProduct : String, howMuch: String, type : String, ifBuy : Int){
        this.id = id
        this.nameShoppingProduct = nameShoppingProduct
        this.howMuch = howMuch
        this.type = type
        this.ifBuy = ifBuy
    }
}