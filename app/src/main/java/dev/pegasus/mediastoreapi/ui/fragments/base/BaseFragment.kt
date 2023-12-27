package dev.pegasus.mediastoreapi.ui.fragments.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import dev.pegasus.mediastoreapi.databinding.ActivityMainBinding

/**
 * @Author: SOHAIB AHMED
 * @Date: 27-12-2023
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

abstract class BaseFragment<T : ViewBinding>(val bindingFactory: (LayoutInflater) -> T)  : Fragment(){

    private var _binding: T? = null
    val binding get() = _binding!!

    private var hasInitializedRootView = false
    private var rootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView?.let {
            _binding = DataBindingUtil.bind(it)
            (rootView?.parent as? ViewGroup)?.removeView(rootView)
            return it
        } ?: kotlin.run {
            _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
            rootView = binding.root
            return rootView!!
        }
    }

    /**
     *      Use the following method in onViewCreated from escaping reinitializing of views
     *      if (!hasInitializedRootView) {
     *          hasInitializedRootView = true
     *          // Your Code...
     *      }
     */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!hasInitializedRootView) {
            hasInitializedRootView = true
            onViewCreatedOneTime()
        }
        onViewCreatedEverytime()
    }

    /**
     *  @since : Write Code to be called onetime
     */
    abstract fun onViewCreatedOneTime()

    /**
     *  @since : Write Code to be called everytime
     */
    abstract fun onViewCreatedEverytime()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        hasInitializedRootView = false
        rootView = null
    }
}