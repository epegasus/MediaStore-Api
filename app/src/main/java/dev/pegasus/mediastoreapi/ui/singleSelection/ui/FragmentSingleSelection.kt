package dev.pegasus.mediastoreapi.ui.singleSelection.ui

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import dev.pegasus.mediastoreapi.R
import dev.pegasus.mediastoreapi.databinding.FragmentSingleSelectionBinding
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.utils.Constants.TAG
import dev.pegasus.mediastoreapi.ui.general.helper.viewmodels.GalleryViewModel
import dev.pegasus.mediastoreapi.ui.general.ui.fragments.base.BaseFragment
import dev.pegasus.mediastoreapi.ui.singleSelection.helper.SingleGalleryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FragmentSingleSelection : BaseFragment<FragmentSingleSelectionBinding>(FragmentSingleSelectionBinding::inflate) {

    private val singleGalleryAdapter by lazy { SingleGalleryAdapter(callback) }
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onViewCreated() {
        initRecyclerView()
        askForPermissions()

        binding?.let {
            it.mbBack.setOnClickListener { findNavController().popBackStack() }
            it.srlRefresh.setOnRefreshListener { singleGalleryAdapter.refresh() }
        }
    }


    private fun initRecyclerView() {
        binding?.rvList?.adapter = singleGalleryAdapter
    }

    private fun askForPermissions() {
        when (checkStoragePermission()) {
            true -> fetchData()
            false -> askStoragePermission { fetchData() }
        }
    }

    private fun fetchData() {
        Log.d(TAG, "fetchData: Starting...")
        lifecycleScope.launch {
            galleryViewModel.fetchPhotos().collectLatest {
                singleGalleryAdapter.submitData(it)
            }
        }

        lifecycleScope.launch {
            binding?.mtvStates?.text = getString(R.string.state, "Fetching...")
            singleGalleryAdapter.loadStateFlow.collect { loadStates ->
                binding?.srlRefresh?.isRefreshing = loadStates.refresh is LoadState.Loading
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding?.mtvStates?.text = getString(R.string.state, "Loading...")
                    }

                    is LoadState.NotLoading -> {
                        binding?.mtvStates?.text = getString(R.string.state, "Not loading...")
                    }

                    is LoadState.Error -> {
                        val errorState = loadStates.refresh as LoadState.Error
                        binding?.mtvStates?.text = getString(R.string.state, "Error: ${errorState.error.message}")
                    }
                }
            }


        }
    }

    private val callback: (photo: Photo) -> Unit = {
        val action = FragmentSingleSelectionDirections.actionFragmentSingleSelectionToFragmentPhoto(photo = it)
        findNavController().navigate(action)
    }
}