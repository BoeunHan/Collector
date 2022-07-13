package com.han.collector.view.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.han.collector.databinding.FlipviewFragmentMovieDetailBinding
import com.han.collector.databinding.FragmentMovieDetailBinding
import com.han.collector.utils.Constants
import com.han.collector.viewmodel.ItemViewModel
import com.han.collector.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private var binding: FlipviewFragmentMovieDetailBinding? = null
    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FlipviewFragmentMovieDetailBinding.inflate(inflater, container, false)

        binding?.lifecycleOwner = this
        val id = arguments?.getInt(Constants.SELECTED_ID)

        lifecycleScope.launch {
            viewModel.getMovieDetail(id!!).collectLatest {
                binding?.movie = it
            }
        }

        binding?.tvSummary?.movementMethod = ScrollingMovementMethod()
        binding?.tvReview?.movementMethod = ScrollingMovementMethod()

        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}