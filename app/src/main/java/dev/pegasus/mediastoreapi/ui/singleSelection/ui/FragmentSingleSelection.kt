package dev.pegasus.mediastoreapi.ui.singleSelection.ui

import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import dev.pegasus.mediastoreapi.R
import dev.pegasus.mediastoreapi.databinding.FragmentSingleSelectionBinding
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.viewmodels.GalleryViewModel
import dev.pegasus.mediastoreapi.ui.general.ui.fragments.base.BaseFragment
import dev.pegasus.mediastoreapi.ui.singleSelection.helper.SingleGalleryAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


const val TAG = "dummy"

class FragmentSingleSelection : BaseFragment<FragmentSingleSelectionBinding>(FragmentSingleSelectionBinding::inflate) {

    private val singleGalleryAdapter by lazy { SingleGalleryAdapter(callback) }
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onViewCreated() {
        initRecyclerView()
        askForPermissions()
        initObserver()

        binding?.let {
            it.mbBack.setOnClickListener { findNavController().popBackStack() }
            it.srlRefresh.setOnRefreshListener { singleGalleryAdapter.refresh() }
        }
    }

    private fun initRecyclerView() {
        binding?.rvList?.adapter = singleGalleryAdapter
        binding?.rvList?.itemAnimator = DefaultItemAnimator()
    }

    private fun askForPermissions() {
        when (checkStoragePermission()) {
            true -> fetchData()
            false -> askStoragePermission { fetchData() }
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            withContext(Dispatchers.Default) {
                galleryViewModel.fetchPhotos().collectLatest {
                    singleGalleryAdapter.submitData(it)
                }
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            singleGalleryAdapter.loadStateFlow.collect { loadStates ->
                binding?.srlRefresh?.isRefreshing = loadStates.refresh is LoadState.Loading
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding?.mtvStates?.text = getString(R.string.state, "Loading...")
                    }

                    is LoadState.NotLoading -> {
                        binding?.mtvStates?.text = getString(R.string.state, "Completed")
                    }

                    is LoadState.Error -> {
                        val errorState = loadStates.refresh as LoadState.Error
                        binding?.mtvStates?.text = getString(R.string.state, "Error: ${errorState.error}")
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