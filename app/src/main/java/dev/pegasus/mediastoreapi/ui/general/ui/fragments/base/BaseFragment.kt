package dev.pegasus.mediastoreapi.ui.general.ui.fragments.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dev.pegasus.mediastoreapi.ui.general.helper.utils.Constants.TAG

/**
 * @Author: SOHAIB AHMED
 * @Date: 27-12-2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

abstract class BaseFragment<T : ViewBinding>(private val bindingFactory: (LayoutInflater) -> T) : Fragment() {

    private var _binding: T? = null
    protected val binding: T?
        get() {
            Log.e(TAG, "$bindingFactory", NullPointerException("Binding object is null"))
            return _binding
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = bindingFactory(layoutInflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    abstract fun onViewCreated()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}