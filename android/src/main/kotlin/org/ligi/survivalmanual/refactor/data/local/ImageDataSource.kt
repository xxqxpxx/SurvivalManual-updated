package org.ligi.survivalmanual.refactor.data.local

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.R
import java.io.FileNotFoundException

class ImageDataSource(private val context: Context) {

    suspend fun getImageData(imageId: String): Drawable? {
        return try {
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
}