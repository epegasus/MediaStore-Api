package dev.pegasus.mediastoreapi.ui.general.ui.fragments.single

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import dev.pegasus.mediastoreapi.R
import dev.pegasus.mediastoreapi.databinding.FragmentSingleSelectionBinding
import dev.pegasus.mediastoreapi.ui.general.helper.adapters.AdapterSingleSelection
import dev.pegasus.mediastoreapi.ui.general.helper.extensions.withDelay
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.utils.Constants.TAG
import dev.pegasus.mediastoreapi.ui.general.helper.viewmodels.GalleryViewModel
import dev.pegasus.mediastoreapi.ui.general.ui.fragments.base.BaseFragment

class FragmentSingleSelection : BaseFragment<FragmentSingleSelectionBinding>(FragmentSingleSelectionBinding::inflate) {

    private val adapterSingleSelection by lazy { AdapterSingleSelection(callback) }
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onViewCreated() {
        initRecyclerView()
        askForPermissions()
        initObserver()

        binding?.mbBack?.setOnClickListener { findNavController().popBackStack() }
    }

    private fun initRecyclerView() {
        binding?.rvList?.adapter = adapterSingleSelection
    }

    private fun askForPermissions() {
        when (checkStoragePermission()) {
            true -> withDelay(500) { fetchData() }
            false -> askStoragePermission { fetchData() }
        }
    }

    private fun fetchData() {
        binding?.mtvStates?.text = getString(R.string.state, "Loading...")
        galleryViewModel.fetchPhotos()
    }

    private fun initObserver() {
        galleryViewModel.photosLiveData.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver: called")
            binding?.progressBar?.visibility = View.GONE
            adapterSingleSelection.submitList(it)
        }
    }

    private val callback: (photo: Photo) -> Unit = {
        val action = FragmentSingleSelectionDirections.actionFragmentSingleSelectionToFragmentPhoto(photo = it)
        findNavController().navigate(action)
    }
}