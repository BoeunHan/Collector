package com.han.collector.model.repository

import android.content.Context
import android.util.Log
import com.han.collector.utils.Constants
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.han.collector.model.data.database.ReviewDatabase
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    @ApplicationContext val context: Context,
    val reviewDatabase: ReviewDatabase
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


    private val movieDao = reviewDatabase.movieDao()
    private val bookDao = reviewDatabase.bookDao()

}