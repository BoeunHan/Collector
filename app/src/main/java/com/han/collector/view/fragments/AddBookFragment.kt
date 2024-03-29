package com.han.collector.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.han.collector.databinding.FragmentAddBookBinding
import com.han.collector.viewmodel.BookViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlin.experimental.and

@SuppressLint("ClickableViewAccessibility")
@FlowPreview
@AndroidEntryPoint
class AddBookFragment : Fragment() {
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

        binding?.layoutSummary?.editText?.setOnTouchListener(onTouchListener)
        binding?.layoutReview?.editText?.setOnTouchListener(onTouchListener)
        binding?.layoutMemo?.editText?.setOnTouchListener(onTouchListener)
        return binding?.root
    }

    private val onTouchListener = View.OnTouchListener { view, event ->
        if (view == binding?.layoutSummary?.editText
            || view == binding?.layoutReview?.editText
            || view == binding?.layoutMemo?.editText
        ) {
            view?.parent?.requestDisallowInterceptTouchEvent(true)
            when (event?.action?.toByte()?.and(MotionEvent.ACTION_MASK.toByte())?.toInt()) {
                MotionEvent.ACTION_UP -> view?.parent?.requestDisallowInterceptTouchEvent(false)
            }
        }
        false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}