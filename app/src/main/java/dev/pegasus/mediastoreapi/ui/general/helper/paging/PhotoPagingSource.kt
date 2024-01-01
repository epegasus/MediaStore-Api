package dev.pegasus.mediastoreapi.ui.general.helper.paging

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import dev.pegasus.mediastoreapi.ui.general.helper.extensions.isFileExist
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo
import dev.pegasus.mediastoreapi.ui.general.helper.utils.Constants.TAG
import dev.pegasus.mediastoreapi.ui.general.helper.utils.ConversionUtils
import java.io.File

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

class PhotoPagingSource(private val contentResolver: ContentResolver) : PagingSource<Int, Photo>() {

    private val conversionUtils by lazy { ConversionUtils() }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {
            // Paging
            val pageNumber = params.key ?: 0
            val pageSize = params.loadSize
            val offset = pageNumber * pageSize
            Log.d(TAG, "PhotoPagingSource: load: pageNumber: $pageNumber, pageSize: $pageSize, offset: $offset")

            // Initializing Cursor
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val queryUri: Uri = uri.buildUpon().encodedQuery("limit=$pageSize,$offset").build()
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE
            )
            //val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $offset,$pageSize"
            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            val cursor = contentResolver.query(
                queryUri,
                projection,
                null,
                null,
                sortOrder
            )
            val photos = mutableListOf<Photo>()

            cursor?.use { c ->
                val columnId = c.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val columnData = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val columnAdded = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED)
                val columnModified = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)
                val columnSize = c.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

                while (c.moveToNext()) {
                    val size = c.getLong(columnSize)
                    val dateModified = c.getLong(columnModified)
                    val dateAdded = c.getLong(columnAdded)
                    val fileUri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.getContentUri("external"), c.getLong(columnId))
                    val file = File(c.getString(columnData))
                    val pid = "${file}_${c.getLong(columnId)}_${dateAdded}"

                    val photo = Photo(
                        id = pid,
                        file = file,
                        fileUri = fileUri,
                        fileName = file.name,
                        creationDateTime = conversionUtils.getDateTime(dateAdded * 1000),
                        modifiedDateTime = conversionUtils.getDateTime(dateModified * 1000),
                        fileSize = conversionUtils.getFileSize(size),
                        creationTimestamp = dateAdded,
                        modifiedTimestamp = dateModified,
                        fileSizeBytes = size
                    )
                    if (photo.file.isFileExist()) {
                        photos.add(photo)
                    }
                }
            }
            Log.i(TAG, "PhotoPagingSource: submitting Page: $pageNumber with ${photos.size} photos")
            LoadResult.Page(
                data = photos,
                prevKey = if (pageNumber == 0) null else pageNumber - 1,
                nextKey = if (photos.isEmpty()) null else pageNumber + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "PhotoPagingSource: load: Exception", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int {
        return 0
    }
}