package com.example.demobhsoft.model

class GioHang {
    var id: String = ""
    var sachId: String = ""
    var userId: String = ""
    var amount: Int = 0
    var status: Int = 0

    constructor()

    constructor(sachId: String, userId: String, amount: Int, status: Int){
        this.sachId = sachId
        this.userId = userId
        this.amount = amount
        this.status = status
    }
}