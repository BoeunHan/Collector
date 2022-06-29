package com.han.collector.view.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.han.collector.databinding.FragmentBookDetailBinding
import com.han.collector.utils.Constants
import com.han.collector.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class BookDetailFragment : Fragment(){
    private var binding: FragmentBookDetailBinding? = null
    private val viewModel: ItemViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailBinding.inflate(inflater, container, false)

        binding?.lifecycleOwner = this

        val id = requireArguments().getInt(Constants.SELECTED_ID)
        getData(id)

        binding?.tvSummary?.movementMethod = ScrollingMovementMethod()
        binding?.tvReview?.movementMethod = ScrollingMovementMethod()

        return binding?.root
    }

    private fun getData(id: Int){
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getBookDetail(id).collectLatest {
                binding?.item = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}