package com.han.collector.model.module

import android.content.Context
import androidx.room.Room
import com.han.collector.model.data.database.ReviewDatabase
import com.han.collector.utils.Constants
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
            Constants.REVIEW_DATABASE
        ).fallbackToDestructiveMigration().build()
    }
}
