package com.example.collectors

import com.example.collectors.database.BaseDao
import com.example.collectors.database.BaseEntity
import com.example.collectors.database.BasicInfo
import com.example.collectors.database.MovieDao

data class Categoryy(
        val category: String,
        val list: ArrayList<BasicInfo>,
        val dao: BaseDao<BaseEntity>
)