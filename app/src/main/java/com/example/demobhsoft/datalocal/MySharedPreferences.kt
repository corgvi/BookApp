package com.example.demobhsoft.datalocal

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.demobhsoft.model.UserModel
import com.google.gson.Gson

class MySharedPreferences {

    private val PREFERENCE_NAME = "DemoBHSoft"
    private val TAG = "mysharedpreference"

    fun setModel(context: Context, userModel: UserModel){
        val sharedPreference: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        val gson = Gson()
        val model = gson.toJson(userModel)
        Log.d(TAG, "setModel: $model")
        editor.putString("model", model)
        editor.apply()
    }

    fun getModel(context: Context):UserModel?{
        val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val result = sharedPreferences.getString("model", null)
        return if(result != null) {
            val gson: Gson = Gson()
            Log.d(TAG, "getModel: ${gson.fromJson(result, UserModel::class.java)}")
            gson.fromJson(result, UserModel::class.java)
        } else UserModel()
    }

    fun clearModel(context: Context) {
        val sharedPreference: SharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreference.edit()
        editor.clear().commit()
    }

}