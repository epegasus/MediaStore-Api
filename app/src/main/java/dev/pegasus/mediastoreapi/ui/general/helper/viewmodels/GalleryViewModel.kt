package dev.pegasus.mediastoreapi.ui.general.helper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.paging.PhotoPagingSource
import dev.pegasus.mediastoreapi.ui.general.helper.utils.ConversionUtils
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

class GalleryViewModel(private val application: Application) : AndroidViewModel(application) {

    private val conversionUtils by lazy { ConversionUtils() }
    private val pageSize = 100

    fun fetchPhotos(): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                initialLoadSize = pageSize,
                prefetchDistance = 30,
            ),
            pagingSourceFactory = {
                PhotoPagingSource(application.contentResolver, conversionUtils)
            }
        ).flow
            .flowOn(Dispatchers.IO)
            .cachedIn(viewModelScope)
    }
}