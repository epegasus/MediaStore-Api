package dev.pegasus.mediastoreapi.ui.general.helper.viewmodels

import android.app.Application
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.utils.Constants.TAG
import dev.pegasus.mediastoreapi.ui.general.helper.utils.ConversionUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.time.measureTime

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

class GalleryViewModel(private val application: Application) : AndroidViewModel(application) {

    private val conversionUtils by lazy { ConversionUtils() }

    private val _photosLiveData = MutableLiveData<List<Photo>>()
    val photosLiveData: LiveData<List<Photo>> get() = _photosLiveData

    fun fetchPhotos() {
        val photos = mutableListOf<Photo>()
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "fetchPhotos: called")

            val measureTime = measureTime {
                // Initializing Cursor
                val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

                val projection = arrayOf(
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DATE_ADDED,
                    MediaStore.Images.Media.DATE_MODIFIED,
                    MediaStore.Images.Media.SIZE
                )
                val orderBy = MediaStore.Images.Media.DATE_MODIFIED + " DESC"
                val cursor: Cursor? = application.applicationContext.contentResolver.query(
                    uri,
                    projection,
                    null,
                    null,
                    orderBy
                )

                cursor?.use { c ->
                    val columnId = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                    val columnData = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    val columnAdded = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                    val columnModified = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
                    val columnSize = c.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                    while (c.moveToNext()) {
                        val filePath = c.getString(columnData)

                        val photo = Photo(filePath = filePath)
                        photos.add(photo)
                    }
                }
                Log.d(TAG, "fetchPhotos: called : ${photos.size}")
                _photosLiveData.postValue(photos)
            }
            Log.d(TAG, "fetchPhotos: called : $measureTime")
        }
    }
}