package com.example.collectors.database

import android.app.Application

class CollectorApp : Application() {
    val db by lazy{
        ReviewDatabase.getInstance(this)
    }
}