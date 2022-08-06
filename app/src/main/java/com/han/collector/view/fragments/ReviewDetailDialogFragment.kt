package com.han.collector.view.fragments

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
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
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.han.collector.R
import com.han.collector.databinding.FragmentReviewDetailBinding
import com.han.collector.utils.Constants
import com.han.collector.view.activities.AddReviewActivity
import com.han.collector.viewmodel.ItemViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@FlowPreview
@AndroidEntryPoint
class ReviewDetailDialogFragment : DialogFragment() {

    lateinit var binding: FragmentReviewDetailBinding

    val viewModel: ItemViewModel by activityViewModels()

    private var showingBack = false
    private var isFlipping = false

    private var id: Int? = null
    private var category: String? = null
    private var title: String? = null
    private var image: Bitmap? = null

    companion object{
        const val TAG = "CardFlipDialog"
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReviewDetailBinding.inflate(inflater, container, false)
        binding.fragment = this

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        id = arguments?.getInt(Constants.SELECTED_ID)
        category = arguments?.getString(Constants.CATEGORY)

        getData()

        val bundle = Bundle()
        bundle.putInt(Constants.SELECTED_ID, id!!)

        if (savedInstanceState == null) {
            when (category) {
                "영화" -> childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<MovieDetailFragment>(
                        binding.flipViewBack.detailFragmentContainer.id,
                        args = bundle
                    )
                }
                "책" -> childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<BookDetailFragment>(
                        binding.flipViewBack.detailFragmentContainer.id,
                        args = bundle
                    )
                }
                "장소" -> childFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace<PlaceDetailFragment>(
                        binding.flipViewBack.detailFragmentContainer.id,
                        args = bundle
                    )
                }
            }
        }

        return binding.root
    }

    fun getData(){
        binding.flipViewFront.lifecycleOwner = this
        binding.flipViewFront.id = id
        binding.flipViewFront.viewModel = viewModel
        binding.flipViewBack.fragment = this

        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getDetailInfo(id!!).collectLatest {
                    image = it.image
                    title = it.title
                    if(it.image!=null) {
                        binding.flipViewFront.ivImage.setImageBitmap(it.image)
                        binding.flipViewFront.ivImage.scaleType = ImageView.ScaleType.CENTER_CROP
                    } else Glide.with(requireContext()).load(R.drawable.ic_no_image).into(binding.flipViewFront.ivImage)
                    binding.flipViewFront.rating = it.rate
                    binding.flipViewFront.like = it.like
                    binding.flipViewBack.title = it.title
                    binding.flipViewBack.uploadDate = it.uploadDate
                    binding.flipViewBack.editDate = it.editDate
                }
            }
        }
    }

    fun editCard(){
        val intent = Intent(context, AddReviewActivity::class.java)
        intent.putExtra(Constants.SELECTED_ID, id)
        intent.putExtra(Constants.CATEGORY, category)
        intent.putExtra(Constants.TITLE, title)
        startActivity(intent)
    }


    fun flipCard(){
        try {
            if(isFlipping) return

            isFlipping = true

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
            val cameraDist = 15000 * scale
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
                isFlipping = false
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        requireContext().dialogFragmentResize(this, 0.9f, 0.9f)
    }

    private fun Context.dialogFragmentResize(dialogFragment: DialogFragment, width: Float, height: Float) {
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
