package dev.pegasus.mediastoreapi.ui.general.ui.fragments.single

import dev.pegasus.mediastoreapi.databinding.FragmentSingleSelectionBinding
import dev.pegasus.mediastoreapi.ui.general.ui.fragments.base.BaseFragment

class FragmentSingleSelection2 : BaseFragment<FragmentSingleSelectionBinding>(FragmentSingleSelectionBinding::inflate) {

    /* private val adapterSingleSelection by lazy { AdapterSingleSelection(callback) }
    private val galleryViewModel: GalleryViewModel by viewModels()

    override fun onViewCreated() {
        initRecyclerView()
        askForPermissions()
        initObserver()

        binding?.let {
            it.mbBack.setOnClickListener { findNavController().popBackStack() }
            it.srlRefresh.setOnRefreshListener {  }
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
    }*/
    override fun onViewCreated() {

    }
}