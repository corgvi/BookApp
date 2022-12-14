package com.example.demobhsoft.utils

import java.text.DecimalFormat
import java.text.NumberFormat

    fun ConvertToVND(price: Int): String{
        val formatter: NumberFormat = DecimalFormat("#,###")
        return "${formatter.format(price)} VND"
    }