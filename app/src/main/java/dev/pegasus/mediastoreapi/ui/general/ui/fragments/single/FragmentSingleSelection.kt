package dev.pegasus.mediastoreapi.ui.general.ui.fragments.single

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DefaultItemAnimator
import dev.pegasus.mediastoreapi.R
import dev.pegasus.mediastoreapi.databinding.FragmentSingleSelectionBinding
import dev.pegasus.mediastoreapi.ui.general.helper.adapters.AdapterSingleSelection
import dev.pegasus.mediastoreapi.ui.general.helper.extensions.withDelay
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.viewmodels.GalleryViewModel
import dev.pegasus.mediastoreapi.ui.general.ui.fragments.base.BaseFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentSingleSelection : BaseFragment<FragmentSingleSelectionBinding>(FragmentSingleSelectionBinding::inflate) {

    private val adapterSingleSelection by lazy { AdapterSingleSelection(callback) }
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onViewCreated() {
        initRecyclerView()
        askForPermissions()
        initObserver()

        binding?.let {
            it.mbBack.setOnClickListener { findNavController().popBackStack() }
            it.srlRefresh.setOnRefreshListener { adapterSingleSelection.refresh() }
        }
    }

    private fun initRecyclerView() {
        binding?.rvList?.adapter = adapterSingleSelection
        binding?.rvList?.itemAnimator = DefaultItemAnimator()
    }

    private fun askForPermissions() {
        when (checkStoragePermission()) {
            true -> withDelay(500) { fetchData() }
            false -> askStoragePermission { fetchData() }
        }
    }

    private fun fetchData() {
        lifecycleScope.launch {
            galleryViewModel.fetchPhotos().collectLatest {
                adapterSingleSelection.submitData(it)
            }
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            adapterSingleSelection.loadStateFlow.collect { loadStates ->
                binding?.srlRefresh?.isRefreshing = loadStates.refresh is LoadState.Loading
                when (loadStates.refresh) {
                    is LoadState.Loading -> {
                        binding?.mtvStates?.text = getString(R.string.state, "Loading...")
                    }

                    is LoadState.NotLoading -> {
                        binding?.progressBar?.visibility = View.GONE
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