package dev.pegasus.mediastoreapi.ui.singleSelection.helper

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.pegasus.mediastoreapi.databinding.ItemPhotoBinding
import dev.pegasus.mediastoreapi.ui.general.helper.extensions.loadImage
import dev.pegasus.mediastoreapi.ui.general.helper.models.Photo

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

class SingleGalleryAdapter(private val callback: (photo: Photo) -> Unit) : PagingDataAdapter<Photo, SingleGalleryAdapter.CustomViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.context))
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem: Photo? = getItem(position)
        holder.binding.apply {
            currentItem?.let { item ->
                root.setOnClickListener { callback.invoke(item) }
                sivImageItemPhoto.loadImage(item.file.toString())
            }
        }
    }

    inner class CustomViewHolder(val binding: ItemPhotoBinding) : RecyclerView.ViewHolder(binding.root)

    object DiffCallback : DiffUtil.ItemCallback<Photo>() {
        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }
    }
}