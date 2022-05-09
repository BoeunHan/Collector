package com.example.collectors.database

import android.app.Application

class MovieApp : Application() {
    val db by lazy{
        MovieDatabase.getInstance(this)
    }
}