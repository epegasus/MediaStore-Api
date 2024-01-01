package dev.pegasus.mediastoreapi.ui.general.helper.extensions

import android.util.Log
import dev.pegasus.mediastoreapi.ui.general.helper.utils.Constants.TAG
import java.io.File

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

fun File.isFileExist(): Boolean {
    return try {
        this.exists()
    } catch (ex: SecurityException) {
        Log.e(TAG, "isFileExist: ", ex)
        false
    }
}