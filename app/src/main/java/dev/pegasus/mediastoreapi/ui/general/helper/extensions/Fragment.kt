package dev.pegasus.mediastoreapi.ui.general.helper.extensions

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.withCreated
import androidx.lifecycle.withResumed
import androidx.lifecycle.withStarted
import kotlinx.coroutines.launch

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

fun Fragment.withDelay(delay: Long = 300, callback: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed({
        if (isAdded) callback.invoke()
    }, delay)
}

fun Fragment.showToast(any: Any) {
    context?.let {
        Toast.makeText(it, any.toString(), Toast.LENGTH_SHORT).show()
    }
}

fun Fragment.launchWhenCreated(callback: () -> Unit) {
    lifecycleScope.launch { lifecycle.withCreated(callback) }
}

fun Fragment.launchWhenStarted(callback: () -> Unit) {
    lifecycleScope.launch { lifecycle.withStarted(callback) }
}

fun Fragment.launchWhenResumed(callback: () -> Unit) {
    lifecycleScope.launch { lifecycle.withResumed(callback) }
}