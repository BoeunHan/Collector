package com.example.collectors.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectors.Constants
import com.example.collectors.R
import com.example.collectors.adapters.MovieSearchAdapter
import com.example.collectors.models.Item
import com.example.collectors.models.MovieList
import com.example.collectors.network.MovieApiService
import com.example.collectors.textToFlow
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_movie_search.*
import kotlinx.android.synthetic.main.activity_movie_search.btCancel
import kotlinx.android.synthetic.main.activity_movie_search.tvNothingFound
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

class MovieSearchActivity : AppCompatActivity() {
    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_search)

        btCancel.setOnClickListener {
            etSearchMovie.setText("")
        }

        lifecycleScope.launch{
            val editTextFlow = etSearchMovie.textToFlow()
            editTextFlow
                .debounce(500)
                .filter{ it?.length!! > 0 }
                .onEach{ getSearchResult(it.toString())}
                .launchIn(this)
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

                        setupSearchRecyclerView(result!!.items)

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
        if(movieList.isNullOrEmpty()){
            tvNothingFound.visibility = View.VISIBLE
            rvMovieSearchList.visibility = View.GONE
        }
        else {
            tvNothingFound.visibility = View.GONE
            rvMovieSearchList.visibility = View.VISIBLE
            rvMovieSearchList.layoutManager = LinearLayoutManager(this)
            rvMovieSearchList.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
            val movieSearchAdapter = MovieSearchAdapter(movieList, this)
            rvMovieSearchList.adapter = movieSearchAdapter
        }
    }
}

