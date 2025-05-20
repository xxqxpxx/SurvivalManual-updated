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
import java.io.ByteArrayOutputStream

class ImageDataSource(private val context: Context) {

    suspend fun getImageData(imageId: String): ByteArray? {
        val drawable: Drawable? = try {
            if (imageId.endsWith(".vd")) {
                val drawableName = imageId.substringBeforeLast(".")
                val resourceId = context.resources.getIdentifier(drawableName, "drawable", context.packageName)
                if (resourceId != 0) {
                    VectorDrawableCompat.create(context.resources, resourceId, null)
                } else {
                    throw DomainException.ContentNotFoundException("Vector drawable resource not found for ID: $imageId")
                }
            } else {
                if (resourceId != 0) {
                    ContextCompat.getDrawable(context, resourceId)
                } else {
                    BitmapDrawable.createFromStream(
                        context.assets.open("md/$imageId"),
                        imageId
                    ) as BitmapDrawable
                }            } catch (e: FileNotFoundException) {
                Log.e("ImageDataSource", "Could not find md/$imageId", e)
                throw DomainException.ContentNotFoundException("Image not found for ID: $imageId")
                null
            }
        } catch (e: Exception) {
            Log.e("ImageDataSource", "Error loading image for ID: $imageId", e)
            throw DomainException.UnknownErrorException("Error loading image: ${e.message}")
        }

        return drawableToByteArray(drawable)
    }

    private fun drawableToByteArray(drawable: Drawable?): ByteArray? {
        if (drawable == null) return null
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

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        bitmap.recycle() // Recycle the bitmap to free up memory
        return byteArray
    }
}