package dev.pegasus.mediastoreapi.ui.general.helper.extensions

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ProgressBar
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.fitCenterTransform
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
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
    /*val circularProgressDrawable = CircularProgressDrawable(context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
*/
    Glide
        .with(this)
        .setDefaultRequestOptions(
            /*RequestOptions()
                .placeholder(circularProgressDrawable)
                .error(R.drawable.gif_glide_error)
                .centerCrop()*/
            RequestOptions()
                .override(200)
                .centerCrop()
        )
        .load(filePath)
        .transition(DrawableTransitionOptions.withCrossFade(DrawableCrossFadeFactory.Builder(500).setCrossFadeEnabled(true).build()))
        .into(this)
}

fun ImageFilterView.loadImage(filePath: String) {
    val requestOptions = RequestOptions()
        .override(this.width)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .centerCrop()
    val transitionOptions = DrawableTransitionOptions
        .withCrossFade(
            DrawableCrossFadeFactory
                .Builder(100)
                .setCrossFadeEnabled(false)
                .build()
        )
    Glide
        .with(this)
        .setDefaultRequestOptions(requestOptions)
        .load(filePath)
        .transition(transitionOptions)
        .into(this)
}

fun ShapeableImageView.loadImage(filePath: String, progressBar: ProgressBar?) {
    Glide
        .with(this)
        .setDefaultRequestOptions(
            RequestOptions()
                .error(R.drawable.gif_glide_error)
                .centerCrop()
        )
        .load(filePath)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }

            override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>?, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                progressBar?.visibility = View.GONE
                return false
            }
        })
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}