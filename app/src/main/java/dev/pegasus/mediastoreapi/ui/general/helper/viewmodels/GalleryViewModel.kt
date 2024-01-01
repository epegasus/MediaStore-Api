package dev.pegasus.mediastoreapi.ui.general.helper.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.paging.PhotoPagingSource
import dev.pegasus.mediastoreapi.ui.general.helper.utils.Constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

class GalleryViewModel(application: Application) : AndroidViewModel(application) {

    fun fetchPhotos(): Flow<PagingData<Photo>> {
        Log.i(TAG, "GalleryViewModel: fetchPhotos: calling...")
        return Pager(
            config = PagingConfig(
                pageSize = 100
            ),
            pagingSourceFactory = {
                PhotoPagingSource(getApplication<Application>().contentResolver)
            }
        ).flow.cachedIn(viewModelScope)
    }
}