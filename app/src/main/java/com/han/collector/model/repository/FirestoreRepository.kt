package com.han.collector.model.repository

import android.content.Context
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.han.collector.model.data.database.ReviewDatabase
import com.han.collector.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    db: ReviewDatabase,
    @ApplicationContext val context: Context
) {
    val movieDao = db.movieDao()
    val bookDao = db.bookDao()

    val firestore = Firebase.firestore

    class MovieUpdates {
        companion object {
            val addedId = ArrayList<Int>()
            val removedId = ArrayList<Int>()
        }
    }

    class BookUpdates {
        companion object {
            val addedId = ArrayList<Int>()
            val removedId = ArrayList<Int>()
        }
    }

    suspend fun update(category: String, id: Int, mode: String) {
        if (Constants.isNetworkAvailable(context)) upload(category, id, mode)
        else updateState(category, id, mode)
    }

    suspend fun upload(category: String, id: Int, mode: String) {
        val uid = Firebase.auth.currentUser!!.uid
        val userDoc = firestore.collection("users").document(uid)

        when (category) {
            "영화" -> {
                val movie = movieDao.getData(id)
                when (mode) {
                    "I" -> userDoc.collection("movie").document(id.toString()).set(movie).await()
                    "R" -> userDoc.collection("movie").document(id.toString()).delete().await()
                }
            }
            "책" -> {
                val book = bookDao.getData(id)
                when (mode) {
                    "I" -> userDoc.collection("book").document(id.toString()).set(book).await()
                    "R" -> userDoc.collection("book").document(id.toString()).delete().await()
                }
            }
        }
    }

    fun updateState(category: String, id: Int, mode: String) {
        when (category) {
            "영화" -> {
                when (mode) {
                    "I" -> MovieUpdates.addedId.add(id)
                    "R" -> MovieUpdates.removedId.add(id)
                }
            }
            "책" -> {
                when (mode) {
                    "I" -> BookUpdates.addedId.add(id)
                    "R" -> BookUpdates.removedId.add(id)
                }
            }
        }
    }

    suspend fun uploadAll() {
        val uid = Firebase.auth.currentUser!!.uid
        val userDoc = firestore.collection("users").document(uid)
        movieDao.getAll()?.let {
            for (movie in movieDao.getAll()!!) {
                userDoc.collection("movie").document(movie.id.toString()).set(movie).await()
            }
        }
        bookDao.getAll()?.let {
            for (book in bookDao.getAll()!!) {
                userDoc.collection("book").document(book.id.toString()).set(book).await()
            }
        }
    }

    suspend fun uploadState() {
        val uid = Firebase.auth.currentUser!!.uid
        val userDoc = firestore.collection("users").document(uid)
        for (i in MovieUpdates.addedId) {
            val movie = movieDao.getData(i)
            userDoc.collection("movie").document(movie.id.toString()).set(movie).await()
        }
        for (i in MovieUpdates.removedId) {
            userDoc.collection("movie").document(i.toString()).delete().await()
        }
        for (i in BookUpdates.addedId) {
            val book = bookDao.getData(i)
            userDoc.collection("book").document(book.id.toString()).set(book).await()
        }
        for (i in BookUpdates.removedId) {
            userDoc.collection("book").document(i.toString()).delete().await()
        }

        MovieUpdates.addedId.clear()
        MovieUpdates.removedId.clear()
        BookUpdates.addedId.clear()
        BookUpdates.removedId.clear()
    }
}