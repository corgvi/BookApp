package com.example.demobhsoft.model

class DonHang {
    var id: String = ""
    var price: Int = 0
    var name: String = ""
    var sachId: String = ""
    var userId: String = ""
    var amount: Int = 0
    var status: String = ""

    constructor()

    constructor(id: String, price: Int, name: String, sachId: String, userId: String, amount: Int, status: String){
        this.id = id
        this.price = price
        this.name = name
        this.sachId = sachId
        this.userId = userId
        this.amount = amount
        this.status = status
    }
}