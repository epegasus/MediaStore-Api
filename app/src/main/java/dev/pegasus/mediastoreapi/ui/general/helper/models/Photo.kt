package dev.pegasus.mediastoreapi.ui.general.helper.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

@Parcelize
data class Photo(
    val id: String,
    var file: File,
    var fileUri: Uri,
    var fileName: String,
    var creationDateTime: String,
    var modifiedDateTime: String,
    var fileSize: String,
    var creationTimestamp: Long,
    var modifiedTimestamp: Long,
    var fileSizeBytes: Long,
    var isSelected: Int = 0
) : Parcelable