package com.han.collector.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        binding?.fragment = this
        binding?.lifecycleOwner = this

        binding?.etGoods?.setOnTouchListener(onTouchListener)
        binding?.etBads?.setOnTouchListener(onTouchListener)
        binding?.etMemo?.setOnTouchListener(onTouchListener)

        return binding?.root
    }

    val getGalleryImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    val uri = it.data?.data!!
                    val bitmap = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(
                            requireActivity().contentResolver, uri))
                    } else{
                        MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
                    }
                    viewModel.setPlaceImage(bitmap)
                }
                Activity.RESULT_CANCELED -> {
                    Toast.makeText(
                        context, "사진을 가져오지 못했습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    fun startGalleryApp() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        getGalleryImageLauncher.launch(intent)
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startGalleryApp()
            } else {
                Toast.makeText(
                    context, "사진 접근 권한이 거부되었습니다.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                startGalleryApp()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("권한 요청")
                    .setMessage("이 기능을 이용하려면 사진 접근 권한을 허용해야 합니다.")
                    .setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss()
                        requestPermissionLauncher.launch(
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                    }
                    .show()
            }
            else -> {
                requestPermissionLauncher.launch(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
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
}