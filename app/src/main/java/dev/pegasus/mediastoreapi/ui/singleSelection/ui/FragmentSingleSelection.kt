package dev.pegasus.mediastoreapi.ui.singleSelection.ui

import androidx.navigation.fragment.findNavController
import dev.pegasus.mediastore.GalleryManager
import dev.pegasus.mediastoreapi.databinding.FragmentSingleSelectionBinding
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.ui.fragments.base.BaseFragment
import dev.pegasus.mediastoreapi.ui.singleSelection.helper.SingleGalleryAdapter

class FragmentSingleSelection : BaseFragment<FragmentSingleSelectionBinding>(FragmentSingleSelectionBinding::inflate) {

    private val singleGalleryAdapter by lazy { SingleGalleryAdapter(callback) }
    private val galleryManager by lazy { GalleryManager() }

    override fun onViewCreated() {
        fetchData()
        initRecyclerView()
    }

    private fun fetchData() {
        galleryManager.fetchImages()
    }

    private fun initRecyclerView() {
        binding?.rvList?.adapter = singleGalleryAdapter
    }

    private val callback: (photo: Photo) -> Unit = {
        val action = FragmentSingleSelectionDirections.actionFragmentSingleSelectionToFragmentPhoto(photo = it)
        findNavController().navigate(action)
    }
}