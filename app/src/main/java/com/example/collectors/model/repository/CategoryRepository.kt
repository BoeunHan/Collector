package com.example.collectors.model.repository

import android.content.Context
import com.example.collectors.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    @ApplicationContext val context: Context
) {

    private val sharedPref = context.getSharedPreferences(Constants.CATEGORY_PREF, Context.MODE_PRIVATE)

    fun getCategory(): ArrayList<String> {
        val json = sharedPref.getString(Constants.CATEGORY_DATA, null)
        val type = object : TypeToken<ArrayList<String>>(){}.type
        return Gson().fromJson(json, type) ?: ArrayList()
    }
    fun setCategory(list: ArrayList<String>){
        val json = Gson().toJson(list)
        sharedPref.edit().putString(Constants.CATEGORY_DATA, json).apply()
    }
}