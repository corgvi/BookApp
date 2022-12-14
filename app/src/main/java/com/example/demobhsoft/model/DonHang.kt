package com.example.demobhsoft.model

class DonHang {
    var listSach: ArrayList<GioHang> = ArrayList()
    var id: String = ""
    var total: Int = 0
    var userId: String = ""

    constructor()
    constructor(listSach: ArrayList<GioHang>, total: Int, userId: String) {
        this.listSach = listSach
        this.total = total
        this.userId = userId
    }

}