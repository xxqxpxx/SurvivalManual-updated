package org.ligi.survivalmanual.refactor.data.local

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.R
import java.io.FileNotFoundException

class ImageDataSource(private val context: Context) {

    suspend fun getImageData(imageId: String): ByteArray? {
        val drawable = try {
            if (imageId.endsWith(".vd")) {
                when (imageId.substringBeforeLast(".")) {
                    "med_open_airway" -> VectorDrawableCompat.create(
                        context.resources,
                        R.drawable.ic_med_open_airway,
                        null
                    )

                    else -> throw DomainException.ContentNotFoundException("Vector drawable not found for ID: $imageId")
                }
            } else try {
                val resourceId =
                    context.resources.getIdentifier(imageId, "drawable", context.packageName)
                if (resourceId != 0) {
                    ContextCompat.getDrawable(context, resourceId)
                } else {
                    BitmapDrawable.createFromStream(
                        context.assets.open("md/$imageId"),
                        imageId
                    ) as BitmapDrawable
                }
            } catch (e: FileNotFoundException) {
                Log.e("ImageDataSource", "Could not find md/$imageId", e)
                throw DomainException.ContentNotFoundException("Image not found for ID: $imageId")
                null
            }
        } catch (e: Exception) {
            Log.e("ImageDataSource", "Could not find md/$imageId", e)
            throw DomainException.ContentNotFoundException("Image not found for ID: $imageId")

        }
    }

    private fun drawableToByteArray(drawable: Drawable?): ByteArray? {
        if (drawable == null) return null

        val bitmap = if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else {
            // For vector drawables or other drawable types, draw to a bitmap
            val bmp = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            bmp
        }
        return bitmap.toPNGByteArray() // Assuming you have an extension function to convert Bitmap to PNG ByteArray
    }
}