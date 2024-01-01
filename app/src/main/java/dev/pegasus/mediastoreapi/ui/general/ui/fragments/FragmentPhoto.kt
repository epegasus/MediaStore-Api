package dev.pegasus.mediastoreapi.ui.general.ui.fragments

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dev.pegasus.mediastoreapi.databinding.FragmentPhotoBinding
import dev.pegasus.mediastoreapi.ui.general.helper.extensions.loadImage
import dev.pegasus.mediastoreapi.ui.general.helper.extensions.showToast
import dev.pegasus.mediastoreapi.ui.general.ui.fragments.base.BaseFragment

class FragmentPhoto : BaseFragment<FragmentPhotoBinding>(FragmentPhotoBinding::inflate) {

    private val navArgs by navArgs<FragmentPhotoArgs>()

    override fun onViewCreated() {
        setUI()

        binding?.let {
            it.mbBack.setOnClickListener { findNavController().popBackStack() }
            it.mbInfo.setOnClickListener { onInfoClick() }
        }
    }

    private fun setUI() {
        binding?.sivImage?.loadImage(navArgs.photo.file.toString())
    }

    private fun onInfoClick() {
        val text = "Name: ${navArgs.photo.fileName} --- Size: ${navArgs.photo.fileSize}"
        showToast(text)
    }
}