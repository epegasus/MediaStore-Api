package dev.pegasus.mediastoreapi.ui.fragments

import dev.pegasus.mediastoreapi.databinding.FragmentSplashBinding
import dev.pegasus.mediastoreapi.ui.fragments.base.BaseFragment

class FragmentSplash : BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated() {
        binding?.apply {
            mbSingleSelection
        }
    }

}