package com.han.collector.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.han.collector.databinding.FragmentAddMovieBinding
import com.han.collector.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlin.experimental.and

@SuppressLint("ClickableViewAccessibility")
@FlowPreview
@AndroidEntryPoint
class AddMovieFragment : Fragment() {
    private var binding: FragmentAddMovieBinding? = null

    private val viewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddMovieBinding.inflate(inflater, container, false)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        binding?.etSummary?.setOnTouchListener(onTouchListener)
        binding?.etReview?.setOnTouchListener(onTouchListener)
        binding?.etMemo?.setOnTouchListener(onTouchListener)

        return binding?.root
    }

    val onTouchListener = View.OnTouchListener { view, event ->
        if (view?.id == binding?.etSummary?.id
            || view?.id == binding?.etReview?.id
            || view?.id == binding?.etMemo?.id
        ) {
            view?.parent?.requestDisallowInterceptTouchEvent(true)
            when (event?.action?.toByte()?.and(MotionEvent.ACTION_MASK.toByte())?.toInt()) {
                MotionEvent.ACTION_UP -> {
                    view?.parent?.requestDisallowInterceptTouchEvent(false)
                }
            }
        }
        false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}