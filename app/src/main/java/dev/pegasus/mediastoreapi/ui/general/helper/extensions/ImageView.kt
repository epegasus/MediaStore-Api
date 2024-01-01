package dev.pegasus.mediastoreapi.ui.general.helper.extensions

import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import dev.pegasus.mediastoreapi.R

/**
 * @Author: SOHAIB AHMED
 * @Date: 01-01-2024
 * @Accounts
 *      -> https://github.com/epegasus
 *      -> https://stackoverflow.com/users/20440272/sohaib-ahmed
 */

fun ShapeableImageView.loadImage(filePath: String) {
    Glide
        .with(this)
        .load(filePath)
        .placeholder(R.drawable.gif_glide_placeholder)
        .error(R.drawable.gif_glide_error)
        .into(this)
}