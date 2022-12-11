package com.example.demobhsoft.screen.CartActivity

import com.example.demobhsoft.model.GioHang

interface OnClickChangeAmount {
    fun onClickPlus(gioHang: GioHang)
    fun onCLickMinus(gioHang: GioHang)
}