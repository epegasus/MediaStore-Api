package dev.pegasus.mediastoreapi.ui.general.helper.paging

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.os.Bundle
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

class PhotoPagingSource(private val contentResolver: ContentResolver, private val conversionUtils: ConversionUtils) : PagingSource<Int, Photo>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        return try {

            val pageNumber = params.key ?: 0
            val pageSize = params.loadSize
            val offset = pageNumber * pageSize

            // Load images from the MediaStore
            val photos = loadImagesFromMediaStore(pageSize = pageSize, offset = offset)
            Log.d(TAG, "PhotoPagingSource: submitting -> pageNumber: $pageNumber, pageSize: $pageSize, offset: $offset, Size: ${photos.size} photos")

            LoadResult.Page(
                data = photos,
                prevKey = if (pageNumber == 0) null else pageNumber - 1,
                nextKey = if (photos.isEmpty()) null else pageNumber + 1
            )
        } catch (e: Exception) {
            Log.d(TAG, "load: $e")
            LoadResult.Error(e)
        }
    }

    private fun loadImagesFromMediaStore(pageSize: Int, offset: Int): List<Photo> {
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

        val sort = MediaStore.Images.Media.DATE_MODIFIED
        val cursor = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.Q) {
            val bundle = Bundle().apply {
                // selection
                putString(ContentResolver.QUERY_ARG_SQL_SELECTION, null)
                putStringArray(ContentResolver.QUERY_ARG_SQL_SELECTION_ARGS, null)
                // sort
                putStringArray(ContentResolver.QUERY_ARG_SORT_COLUMNS, arrayOf(sort))
                putInt(ContentResolver.QUERY_ARG_SORT_DIRECTION, ContentResolver.QUERY_SORT_DIRECTION_DESCENDING)
                // limit, offset
                putInt(ContentResolver.QUERY_ARG_LIMIT, pageSize)
                putInt(ContentResolver.QUERY_ARG_OFFSET, offset)
            }
            contentResolver.query(uri, projection, bundle, null)
        } else {
            contentResolver.query(uri, projection, null, null, "$sort DESC LIMIT $pageSize OFFSET $offset")
        }

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

        return photos
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}


/*
class PhotoPagingSource(private val contentResolver: ContentResolver, private val conversionUtils: ConversionUtils) : PagingSource<Int, Photo>() {

    private var isShown = false

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        Log.d(TAG, "PhotoPagingSource: load: called")
        */
/*if (isShown) return LoadResult.Error(NullPointerException(""))
        isShown = true*//*

        return try {
            // Paging
            val pageNumber = params.key ?: 0
            val pageSize = params.loadSize
            val offset = pageNumber * pageSize

            // Initializing Cursor
            val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val queryUri = uri.buildUpon().encodedQuery("limit=$pageSize,$offset").build()
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.DATE_MODIFIED,
                MediaStore.Images.Media.SIZE
            )
            //val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT $offset,$pageSize"
            val sortOrder = "${MediaStore.Images.Media.DATE_MODIFIED} DESC"

            val cursor = contentResolver.query(
                queryUri,
                projection,
                null,
                null,
                sortOrder,
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
            Log.d(TAG, "PhotoPagingSource: submitting -> pageNumber: $pageNumber, pageSize: $pageSize, offset: $offset with Size: ${photos.size} photos")
            LoadResult.Page(
                data = photos,
                prevKey = null, // Only paging forward.
                nextKey = if (photos.isEmpty()) null else pageNumber + 1
            )
        } catch (e: Exception) {
            Log.e(TAG, "PhotoPagingSource: load: Exception", e)
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}*/