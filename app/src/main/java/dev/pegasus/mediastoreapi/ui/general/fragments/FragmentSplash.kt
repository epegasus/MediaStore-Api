package dev.pegasus.mediastoreapi.ui.general.fragments

import androidx.navigation.fragment.findNavController
import dev.pegasus.mediastoreapi.R
import dev.pegasus.mediastoreapi.databinding.FragmentSplashBinding
import dev.pegasus.mediastoreapi.ui.general.fragments.base.BaseFragment

class FragmentSplash : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated() {
        binding?.let {
            it.mbSingleSelection.setOnClickListener { findNavController().navigate(R.id.action_fragmentSplash_to_fragmentSingleSelection) }
            it.mbMultipleSelection.setOnClickListener { findNavController().navigate(R.id.action_fragmentSplash_to_fragmentSingleSelection) }
        }
    }
}