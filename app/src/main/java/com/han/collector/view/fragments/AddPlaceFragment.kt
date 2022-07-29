package com.han.collector.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.han.collector.databinding.FragmentAddPlaceBinding
import com.han.collector.viewmodel.PlaceViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlin.experimental.and

@SuppressLint("ClickableViewAccessibility")
@FlowPreview
@AndroidEntryPoint
class AddPlaceFragment : Fragment() {
    private var binding: FragmentAddPlaceBinding? = null

    private val viewModel: PlaceViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddPlaceBinding.inflate(inflater, container, false)

        binding?.viewModel = viewModel
        binding?.lifecycleOwner = this

        binding?.etGoods?.setOnTouchListener(onTouchListener)
        binding?.etBads?.setOnTouchListener(onTouchListener)
        binding?.etMemo?.setOnTouchListener(onTouchListener)

        return binding?.root
    }

    val onTouchListener = View.OnTouchListener { view, event ->
        if (view?.id == binding?.etGoods?.id
            || view?.id == binding?.etBads?.id
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