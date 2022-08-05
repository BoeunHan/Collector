package com.han.collector.model.repository

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Blob
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.han.collector.model.data.database.*
import com.han.collector.utils.Constants
import com.han.collector.utils.Converters
import com.han.collector.view.activities.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    val db: ReviewDatabase,
    @ApplicationContext val context: Context
) {
    val movieDao = db.movieDao()
    val bookDao = db.bookDao()
    val placeDao = db.placeDao()

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

    class PlaceUpdates {
        companion object {
            val addedId = ArrayList<Int>()
            val removedId = ArrayList<Int>()
        }
    }

    fun bitmapToBlob(bitmap: Bitmap?) = Blob.fromBytes(Converters().toByteArray(bitmap))

    fun blobToBitmap(blob: Blob?) = Converters().toBitmap(blob?.toBytes())

    fun changeToUploadVersion(entity: Any): Any? {
        return when (entity) {
            is MovieEntity -> {
                UploadMovieEntity(
                    entity.id,
                    entity.title,
                    bitmapToBlob(entity.image),
                    entity.rate,
                    entity.summary,
                    entity.review,
                    entity.memo,
                    entity.uploadDate,
                    entity.editDate,
                    entity.like
                )
            }
            is BookEntity -> {
                UploadBookEntity(
                    entity.id,
                    entity.title,
                    bitmapToBlob(entity.image),
                    entity.rate,
                    entity.summary,
                    entity.review,
                    entity.memo,
                    entity.uploadDate,
                    entity.editDate,
                    entity.like
                )
            }
            is PlaceEntity -> {
                UploadPlaceEntity(
                    entity.id,
                    entity.title,
                    bitmapToBlob(entity.image),
                    entity.rate,
                    entity.goods,
                    entity.bads,
                    entity.memo,
                    entity.uploadDate,
                    entity.editDate,
                    entity.like
                )
            }
            else -> null
        }
    }

    fun changeToDownloadVersion(entity: Any): Any? {
        return when (entity) {
            is UploadMovieEntity -> {
                MovieEntity(
                    entity.id,
                    entity.title,
                    blobToBitmap(entity.image),
                    entity.rate,
                    entity.summary,
                    entity.review,
                    entity.memo,
                    entity.uploadDate,
                    entity.editDate,
                    entity.like
                )
            }
            is UploadBookEntity -> {
                BookEntity(
                    entity.id,
                    entity.title,
                    blobToBitmap(entity.image),
                    entity.rate,
                    entity.summary,
                    entity.review,
                    entity.memo,
                    entity.uploadDate,
                    entity.editDate,
                    entity.like
                )
            }
            is UploadPlaceEntity -> {
                PlaceEntity(
                    entity.id,
                    entity.title,
                    blobToBitmap(entity.image),
                    entity.rate,
                    entity.goods,
                    entity.bads,
                    entity.memo,
                    entity.uploadDate,
                    entity.editDate,
                    entity.like
                )
            }
            else -> null
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
                val uploadMovie = changeToUploadVersion(movie) as UploadMovieEntity
                when (mode) {
                    "I" -> userDoc.collection("movie").document(id.toString()).set(uploadMovie).await()
                    "R" -> userDoc.collection("movie").document(id.toString()).delete().await()
                }
            }
            "책" -> {
                val book = bookDao.getData(id)
                val uploadBook = changeToUploadVersion(book) as UploadBookEntity
                when (mode) {
                    "I" -> userDoc.collection("book").document(id.toString()).set(uploadBook).await()
                    "R" -> userDoc.collection("book").document(id.toString()).delete().await()
                }
            }
            "장소" -> {
                val place = placeDao.getData(id)
                val uploadPlace = changeToUploadVersion(place) as UploadPlaceEntity
                when (mode) {
                    "I" -> userDoc.collection("place").document(id.toString()).set(uploadPlace).await()
                    "R" -> userDoc.collection("place").document(id.toString()).delete().await()
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
            "장소" -> {
                when (mode) {
                    "I" -> PlaceUpdates.addedId.add(id)
                    "R" -> PlaceUpdates.removedId.add(id)
                }
            }
        }
    }

    suspend fun uploadAll() {
        val uid = Firebase.auth.currentUser!!.uid
        val userDoc = firestore.collection("users").document(uid)
        movieDao.getAll()?.let {
            for (movie in it) {
                val uploadMovie = changeToUploadVersion(movie) as UploadMovieEntity
                userDoc.collection("movie").document(uploadMovie.id.toString()).set(uploadMovie).await()
            }
        }
        bookDao.getAll()?.let {
            for (book in it) {
                val uploadBook = changeToUploadVersion(book) as UploadBookEntity
                userDoc.collection("book").document(uploadBook.id.toString()).set(uploadBook).await()
            }
        }
        placeDao.getAll()?.let {
            for (place in it) {
                val uploadPlace = changeToUploadVersion(place) as UploadPlaceEntity
                userDoc.collection("place").document(uploadPlace.id.toString()).set(uploadPlace).await()
            }
        }
    }

    suspend fun uploadState() {
        val uid = Firebase.auth.currentUser!!.uid
        val userDoc = firestore.collection("users").document(uid)
        for (i in MovieUpdates.addedId) {
            val movie = movieDao.getData(i)
            val uploadMovie = changeToUploadVersion(movie) as UploadMovieEntity
            userDoc.collection("movie").document(uploadMovie.id.toString()).set(uploadMovie).await()
        }
        for (i in MovieUpdates.removedId) {
            userDoc.collection("movie").document(i.toString()).delete().await()
        }
        for (i in BookUpdates.addedId) {
            val book = bookDao.getData(i)
            val uploadBook = changeToUploadVersion(book) as UploadBookEntity
            userDoc.collection("book").document(uploadBook.id.toString()).set(uploadBook).await()
        }
        for (i in BookUpdates.removedId) {
            userDoc.collection("book").document(i.toString()).delete().await()
        }
        for (i in PlaceUpdates.addedId) {
            val place = placeDao.getData(i)
            val uploadPlace = changeToUploadVersion(place) as UploadPlaceEntity
            userDoc.collection("place").document(uploadPlace.id.toString()).set(uploadPlace).await()
        }
        for (i in PlaceUpdates.removedId) {
            userDoc.collection("place").document(i.toString()).delete().await()
        }

        MovieUpdates.addedId.clear()
        MovieUpdates.removedId.clear()
        BookUpdates.addedId.clear()
        BookUpdates.removedId.clear()
        PlaceUpdates.addedId.clear()
        PlaceUpdates.removedId.clear()
    }

    fun clearDBTables() {
        db.clearAllTables()
    }


    fun clearUserData(uid: String) {
        val userDoc = firestore.collection("users").document(uid)
        userDoc.delete()
    }

    suspend fun download(scope: CoroutineScope) {
        val uid = Firebase.auth.currentUser!!.uid
        val userDoc = firestore.collection("users").document(uid)
        userDoc.collection("movie").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    scope.launch(Dispatchers.IO) {
                        val downloadMovie = document.toObject(UploadMovieEntity::class.java)
                        val movie = changeToDownloadVersion(downloadMovie) as MovieEntity
                        movieDao.insert(movie)
                    }
                }
            }
            .await()
        userDoc.collection("book").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    scope.launch(Dispatchers.IO) {
                        val downloadBook = document.toObject(UploadBookEntity::class.java)
                        val book = changeToDownloadVersion(downloadBook) as BookEntity
                        bookDao.insert(book)
                    }
                }
            }
        userDoc.collection("place").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    scope.launch(Dispatchers.IO) {
                        val downloadPlace = document.toObject(UploadPlaceEntity::class.java)
                        val place = changeToDownloadVersion(downloadPlace) as PlaceEntity
                        placeDao.insert(place)
                    }
                }
            }
    }

    private val sharedPref =
        context.getSharedPreferences(Constants.CATEGORY_PREF, Context.MODE_PRIVATE)

    fun downloadCategory(callback: MainActivity.Callback) {
        val uid = Firebase.auth.currentUser!!.uid
        val userDoc = firestore.collection("users").document(uid)

        userDoc.get()
            .addOnCompleteListener { document ->
                val categoryList = document.result.getString("category")
                sharedPref.edit().putString(Constants.CATEGORY_DATA, categoryList).apply()
                callback.event()
            }
    }

    fun uploadCategory(uid: String) {
        val userDoc = firestore.collection("users").document(uid)

        val categoryList = sharedPref.getString(Constants.CATEGORY_DATA, null)
        userDoc.set(hashMapOf("category" to categoryList))
    }

}