package com.han.collector.view.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.han.collector.databinding.FlipviewFragmentBookDetailBinding
import com.han.collector.databinding.FlipviewFragmentMovieDetailBinding
import com.han.collector.databinding.FragmentBookDetailBinding
import com.han.collector.utils.Constants
import com.han.collector.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class BookDetailFragment : Fragment(){
    private var binding: FlipviewFragmentBookDetailBinding? = null
    private val viewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FlipviewFragmentBookDetailBinding.inflate(inflater, container, false)

        binding?.lifecycleOwner = this
        val id = arguments?.getInt(Constants.SELECTED_ID)

        lifecycleScope.launch {
            viewModel.getBookDetail(id!!).collectLatest {
                binding?.book = it
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