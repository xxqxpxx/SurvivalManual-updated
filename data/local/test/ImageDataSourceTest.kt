package data.local.test

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.After
import org.junit.Before
import org.junit.Test
import data.local.ImageDataSource
import domain.error.ContentNotFoundException
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue

class ImageDataSourceTest {

    private lateinit var mockContext: Context
    private lateinit var mockResources: Resources
    private lateinit var mockAssetManager: AssetManager
    private lateinit var imageDataSource: ImageDataSource

    @Before
    fun setUp() {
        mockContext = mockk(relaxed = true)
        mockResources = mockk(relaxed = true)
        mockAssetManager = mockk(relaxed = true)

        every { mockContext.resources } returns mockResources
        every { mockContext.assets } returns mockAssetManager
        every { mockContext.packageName } returns "com.example.app" // Replace with your package name

        imageDataSource = ImageDataSource(mockContext)
    }

    @After
    fun tearDown() {
        // No specific teardown needed for mockk(relaxed = true)
    }

    @Test
    fun `getImageData returns drawable for existing drawable resource`() = runBlocking {
        val drawableId = 123 // Example resource ID
        val imageName = "existing_drawable"
        val mockDrawable: Drawable = mockk()

        every { mockResources.getIdentifier(imageName, "drawable", mockContext.packageName) } returns drawableId
        every { mockContext.getDrawable(drawableId) } returns mockDrawable // Use ContextCompat.getDrawable in real implementation, but mocking Context directly allows this

        val result = imageDataSource.getImageData(imageName)

        assertNotNull(result)
        assertTrue(result is Drawable) // More specific check if possible, e.g., result == mockDrawable
    }

    @Test
    fun `getImageData returns null for non-existing drawable resource`() = runBlocking {
        val imageName = "non_existing_drawable"

        every { mockResources.getIdentifier(imageName, "drawable", mockContext.packageName) } returns 0
        every { mockContext.getDrawable(0) } returns null

        val result = imageDataSource.getImageData(imageName)

        assertNull(result)
    }
}