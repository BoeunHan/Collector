package com.example.collectors.model.module

import android.content.Context
import androidx.room.Room
import com.example.collectors.model.data.database.ReviewDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DBModule {

    @Singleton
    @Provides
    fun provideReviewDatabase(@ApplicationContext context: Context): ReviewDatabase {
        return Room.databaseBuilder(
            context,
            ReviewDatabase::class.java,
            "review_database"
        ).fallbackToDestructiveMigration().build()
    }
}
