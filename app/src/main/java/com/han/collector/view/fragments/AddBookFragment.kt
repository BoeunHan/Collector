package com.han.collector.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.han.collector.databinding.FragmentAddBookBinding
import com.han.collector.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@FlowPreview
@AndroidEntryPoint
class AddBookFragment : Fragment(){
    private var binding: FragmentAddBookBinding? = null

    private val viewModel: BookViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBookBinding.inflate(inflater, container, false)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        return binding?.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}