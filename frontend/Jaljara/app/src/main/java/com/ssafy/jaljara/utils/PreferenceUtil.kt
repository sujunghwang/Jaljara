package com.ssafy.jaljara.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceUtil<T>(context: Context, name : String) {
    private val perf : SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val gson : Gson = Gson()

    inline fun <T> Gson.fromJson(json: String) = fromJson<T>(json, object: TypeToken<T>() {}.type)

    fun getObject(key:String, defValue: String?) : T {
        val json = perf.getString(key, defValue).toString();
        val res : T = gson.fromJson(json);
        return res
    }

    fun setObject(key:String, defVal: T) {
        perf.edit().putString(key, gson.toJson(defVal)).apply();
    }
}