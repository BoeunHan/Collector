package com.example.collectors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.adapters.MovieSearchAdapter
import com.example.collectors.models.Item
import com.example.collectors.models.MovieList
import com.example.collectors.network.MovieApiService
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_movie_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MovieSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)

        btSearch.setOnClickListener {
            getSearchResult(etSearch.text.toString())
        }
    }


    private fun getSearchResult(value: String){
        if(Constants.isNetworkAvailable(this)){
            val gson = GsonBuilder().setLenient().create()

            val retrofit = Retrofit.Builder()
                .baseUrl(Constants.SEARCH_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val service = retrofit.create(MovieApiService::class.java)

            val listCall: Call<MovieList> = service.getSearchResult(
                Constants.NAVER_API_ID, Constants.NAVER_API_SECRET, "movie.json", value, 30
            )

            listCall.enqueue(object: Callback<MovieList>{
                override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {

                    if (response.isSuccessful && response.body()!=null) {
                        val result = response.body()
                        Log.e("Success! :", result.toString())

                        setupSearchRecyclerView(result!!.items)
                        val manager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        manager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                    } else {
                        when (response.code()) {
                            400 -> Log.e("Error 400", "Bad connection.")
                            404 -> Log.e("Error 404", "Not found.")
                            else -> Log.e("Error", "Generic error.")
                        }
                    }
                }

                override fun onFailure(call: Call<MovieList>, t: Throwable) {
                    Log.e("Error", "API call failed.")
                }

            })

        }
    }
    fun setupSearchRecyclerView(movieList: ArrayList<Item>){
        rvMovieSearchList.layoutManager = LinearLayoutManager(this)
        val movieSearchAdapter = MovieSearchAdapter(movieList, this)
        rvMovieSearchList.adapter = movieSearchAdapter
    }
}

