package com.ssafy.jaljara.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferenceUtil<T>(context: Context, name : String) {
    private val perf : SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)
    private val gson : Gson = Gson()

    inline fun <T> Gson.fromJson(json: String, typeToken: TypeToken<T>): T = fromJson(json, typeToken.type)

    fun hasValue(key : String) : Boolean {
        return perf.contains(key);
    }

    fun getValue(key:String, defValue: String?, typeToken: TypeToken<T>) : T? {
        return when {
            perf.contains(key) -> gson.fromJson(perf.getString(key, defValue)!!, typeToken)
            else -> null
        }
    }

    fun setValue(key:String, defVal: T) {
        perf.edit().putString(key, gson.toJson(defVal)).apply();
    }

    fun deleteValue(key: String) : Boolean{
        return when {
            perf.contains(key) -> {
                perf.edit().remove(key)
                true
            }
            else -> true
        }
    }
}