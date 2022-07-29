package com.han.collector.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.han.collector.databinding.FragmentPlaceDetailBinding
import com.han.collector.utils.Constants
import com.han.collector.viewmodel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaceDetailFragment : Fragment() {
    private var binding: FragmentPlaceDetailBinding? = null
    private val viewModel: PlaceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceDetailBinding.inflate(inflater, container, false)

        binding?.lifecycleOwner = this
        val id = arguments?.getInt(Constants.SELECTED_ID)

        lifecycleScope.launch {
            viewModel.getPlaceDetail(id!!).collectLatest {
                binding?.place = it
            }
        }

        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}