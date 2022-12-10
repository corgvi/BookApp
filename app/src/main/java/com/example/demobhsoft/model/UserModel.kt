package com.example.demobhsoft.model

import java.io.Serializable

class UserModel : Serializable {

    var userId: String = "0"
    var fireBaseId: String = ""
    var email: String = ""
    var password: String = ""
    var fullName: String = ""
    var online: Boolean = false
    var avatar: String = ""
    var imgBackground: String = ""

    constructor()

    constructor(userId: String, email: String, password: String, fullName: String){
        this.userId = userId
        this.email = email
        this.password = password
        this.fullName = fullName
    }
}