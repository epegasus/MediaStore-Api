package dev.pegasus.mediastoreapi.ui.general.helper.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

fun Fragment.showToast(any: Any) {
    context?.let {
        Toast.makeText(it, any.toString(), Toast.LENGTH_SHORT).show()
    }
}