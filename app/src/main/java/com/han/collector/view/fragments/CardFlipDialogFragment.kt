package com.han.collector.view.fragments

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.animation.doOnEnd
import androidx.fragment.app.DialogFragment
import com.han.collector.R
import com.han.collector.databinding.FragmentCardFlipBinding

class CardFlipDialogFragment : DialogFragment() {

    lateinit var binding: FragmentCardFlipBinding

    private var showingBack = false

    companion object{
        const val TAG = "CardFlipDialog"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCardFlipBinding.inflate(inflater, container, false)
        binding.fragment = this
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 0.9f, 0.9f)
    }

    fun flipCard(){
        try {
            lateinit var visibleView: View
            lateinit var invisibleView: View

            if(showingBack){
                visibleView = binding.cardFront
                invisibleView = binding.cardBack
                showingBack = false
            } else {
                invisibleView = binding.cardFront
                visibleView = binding.cardBack
                showingBack = true
            }
            visibleView.visibility = View.VISIBLE
            val scale = resources.displayMetrics.density
            val cameraDist = 8000 * scale
            visibleView.cameraDistance = cameraDist
            invisibleView.cameraDistance = cameraDist

            val flipOutAnimatorSet = AnimatorInflater.loadAnimator(
                context,
                R.animator.card_flip_out
            ) as AnimatorSet
            flipOutAnimatorSet.setTarget(invisibleView)

            val flipInAnimationSet = AnimatorInflater.loadAnimator(
                context,
                R.animator.card_flip_in
            ) as AnimatorSet
            flipInAnimationSet.setTarget(visibleView)

            flipOutAnimatorSet.start()
            flipInAnimationSet.start()

            flipInAnimationSet.doOnEnd{
                invisibleView.visibility = View.GONE
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT < 30) {

            val display = windowManager.defaultDisplay
            val size = Point()

            display.getSize(size)

            val window = dialogFragment.dialog?.window

            val x = (size.x * width).toInt()
            val y = (size.y * height).toInt()
            window?.setLayout(x, y)

        } else {

            val rect = windowManager.currentWindowMetrics.bounds

            val window = dialogFragment.dialog?.window

            val x = (rect.width() * width).toInt()
            val y = (rect.height() * height).toInt()

            window?.setLayout(x, y)
        }
    }
}
