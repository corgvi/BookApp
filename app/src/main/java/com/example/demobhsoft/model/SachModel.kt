package com.example.demobhsoft.model

import java.io.Serializable

class SachModel : Serializable {
    var sachId: String = ""
    var name: String = ""
    var description: String = ""
    var price: Int = 0
    var thumbnail: String = ""
    var rate: Float = 0F
    var author: String = ""
    var publisher: String = ""

    constructor()

    constructor(sachId: String, name: String, description: String, price: Int, thumbnail: String, rate: Float, author: String, publisher: String){
        this.sachId = sachId
        this.name = name
        this.description = description
        this.price = price
        this.thumbnail = thumbnail
        this.rate = rate
        this.author = author
        this.publisher = publisher
    }

    override fun toString(): String {
        return "SachModel(sachId='$sachId', name='$name', description='$description', price=$price, thumbnail='$thumbnail', rate=$rate, author='$author', publisher='$publisher')"
    }


}