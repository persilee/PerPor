package net.lishaoy.common.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import net.lishaoy.library.util.PerViewUtil

@BindingAdapter(value = ["url"])
fun ImageView.loadUrl(url: String) {
    if (PerViewUtil.isActivityDestroyed(context)) return
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadUrl(url: String, callback: (Drawable) -> Unit) {
    if (PerViewUtil.isActivityDestroyed(context)) return
    Glide.with(context).load(url).into(object : SimpleTarget<Drawable>() {
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            callback(resource)
        }
    })
}

fun ImageView.loadCircle(url: String) {
    if (PerViewUtil.isActivityDestroyed(context)) return
    Glide.with(this).load(url).transform(CenterCrop()).into(this)
}

@BindingAdapter(value = ["url", "corner"], requireAll = true)
fun ImageView.loadCorner(url: String, corner: Int) {
    if (PerViewUtil.isActivityDestroyed(context)) return
    if (corner > 0) {
        Glide.with(this).load(url).transform(CenterCrop(), RoundedCorners(corner)).into(this)
    } else {
        Glide.with(this).load(url).transform(CenterCrop()).into(this)
    }
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE
) {
    if (PerViewUtil.isActivityDestroyed(context)) return
    Glide.with(this).load(url).transform(CircleBorderTransform(borderWidth, borderColor)).into(this)
}

class CircleBorderTransform(private val borderWidth: Float, private val borderColor: Int) : CircleCrop() {

    var paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val transform = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(transform)
        val halfWidth = (outWidth / 2).toFloat()
        val halfHeight = (outHeight / 2).toFloat()

        canvas.drawCircle(
            halfWidth,
            halfHeight,
            halfHeight.coerceAtMost(halfWidth) - borderWidth / 2,
            paint
        )

        canvas.setBitmap(null)

        return transform
    }

}