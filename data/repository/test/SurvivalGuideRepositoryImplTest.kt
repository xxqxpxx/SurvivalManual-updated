package data.repository.test

import com.example.mylibrary.data.local.ImageDataSource // Replace with your actual import
import com.example.mylibrary.data.local.LocalSurvivalGuideDataSource // Replace with your actual import
import com.example.mylibrary.data.local.PreferencesDataSource // Replace with your actual import
import com.example.mylibrary.data.repository.SurvivalGuideRepositoryImpl // Replace with your actual import
import com.example.mylibrary.domain.error.ContentNotFoundException // Replace with your actual import
import com.example.mylibrary.domain.error.NetworkException // Replace with your actual import
import com.example.mylibrary.domain.error.PreferencesSaveException // Replace with your actual import
import com.example.mylibrary.domain.error.UnknownErrorException // Replace with your actual import
import com.example.mylibrary.domain.model.SearchResult // Replace with your actual import
import com.example.mylibrary.domain.model.SurvivalContent // Replace with your actual import
import com.example.mylibrary.domain.model.UserPreferences // Replace with your actual import
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SurvivalGuideRepositoryImplTest {

    private lateinit var localDataSource: LocalSurvivalGuideDataSource
    private lateinit var preferencesDataSource: PreferencesDataSource
    private lateinit var imageDataSource: ImageDataSource
    private lateinit var repository: SurvivalGuideRepositoryImpl

    @Before
    fun setup() {
        localDataSource = mock()
        preferencesDataSource = mock()
        imageDataSource = mock()
        repository = SurvivalGuideRepositoryImpl(localDataSource, preferencesDataSource, imageDataSource)
    }

    @Test
    fun `getSurvivalContent calls localDataSource and returns result`() = runBlocking {
        val expectedContent = SurvivalContent("Test Content", emptyList(), emptyList())
        whenever(localDataSource.getSurvivalContent()).thenReturn(expectedContent)

        val result = repository.getSurvivalContent()

        assertEquals(expectedContent, result)
        verify(localDataSource).getSurvivalContent()
    }

    @Test
    fun `getSurvivalContent propagates ContentNotFoundException from localDataSource`() = runBlocking {
        whenever(localDataSource.getSurvivalContent()).thenThrow(ContentNotFoundException("Not found"))

        assertThrows(ContentNotFoundException::class.java) {
            runBlocking { repository.getSurvivalContent() }
        }
        verify(localDataSource).getSurvivalContent()
    }

    @Test
    fun `searchSurvivalContent calls localDataSource and returns result`() = runBlocking {
        val query = "test"
        val expectedResults = listOf(SearchResult("Title", "Snippet", "Id"))
        whenever(localDataSource.searchSurvivalContent(query)).thenReturn(expectedResults)

        val result = repository.searchSurvivalContent(query)

        assertEquals(expectedResults, result)
        verify(localDataSource).searchSurvivalContent(query)
    }

    @Test
    fun `searchSurvivalContent propagates NetworkException from localDataSource`() = runBlocking {
        val query = "test"
        whenever(localDataSource.searchSurvivalContent(query)).thenThrow(NetworkException("Network error"))

        assertThrows(NetworkException::class.java) {
            runBlocking { repository.searchSurvivalContent(query) }
        }
        verify(localDataSource).searchSurvivalContent(query)
    }

    @Test
    fun `getUserPreferences calls preferencesDataSource and returns result`() = runBlocking {
        val expectedPreferences = UserPreferences(isNightMode = true)
        whenever(preferencesDataSource.getUserPreferences()).thenReturn(expectedPreferences)

        val result = repository.getUserPreferences()

        assertEquals(expectedPreferences, result)
        verify(preferencesDataSource).getUserPreferences()
    }

    @Test
    fun `getUserPreferences propagates UnknownErrorException from preferencesDataSource`() = runBlocking {
        whenever(preferencesDataSource.getUserPreferences()).thenThrow(UnknownErrorException("Unknown error"))

        assertThrows(UnknownErrorException::class.java) {
            runBlocking { repository.getUserPreferences() }
        }
        verify(preferencesDataSource).getUserPreferences()
    }

    @Test
    fun `saveUserPreferences calls preferencesDataSource`() = runBlocking {
        val preferencesToSave = UserPreferences(isNightMode = false)

        repository.saveUserPreferences(preferencesToSave)

        verify(preferencesDataSource).saveUserPreferences(preferencesToSave)
    }

    @Test
    fun `saveUserPreferences propagates PreferencesSaveException from preferencesDataSource`() = runBlocking {
        val preferencesToSave = UserPreferences(isNightMode = false)
        whenever(preferencesDataSource.saveUserPreferences(preferencesToSave)).thenThrow(PreferencesSaveException("Save failed"))

        assertThrows(PreferencesSaveException::class.java) {
            runBlocking { repository.saveUserPreferences(preferencesToSave) }
        }
        verify(preferencesDataSource).saveUserPreferences(preferencesToSave)
    }

    @Test
    fun `getImageData calls imageDataSource and returns result`() = runBlocking {
        val imageId = "test_image"
        val expectedImageData = mock<android.graphics.drawable.Drawable>() // Mock Drawable or use a different return type
        whenever(imageDataSource.getImageData(imageId)).thenReturn(expectedImageData)

        val result = repository.getImageData(imageId)

        assertEquals(expectedImageData, result)
        verify(imageDataSource).getImageData(imageId)
    }

    @Test
    fun `getImageData propagates ContentNotFoundException from imageDataSource`() = runBlocking {
        val imageId = "non_existent_image"
        whenever(imageDataSource.getImageData(imageId)).thenThrow(ContentNotFoundException("Image not found"))

        assertThrows(ContentNotFoundException::class.java) {
            runBlocking { repository.getImageData(imageId) }
        }
        verify(imageDataSource).getImageData(imageId)
    }
}