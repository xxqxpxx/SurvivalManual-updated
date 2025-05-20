package data.local.test

import android.content.Context
import android.content.res.AssetManager
import data.local.LocalSurvivalGuideDataSource
import domain.error.DomainException
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.io.ByteArrayInputStream
import java.io.FileNotFoundException

@RunWith(MockitoJUnitRunner::class)
class LocalSurvivalGuideDataSourceTest {

    @Mock
    private lateinit var mockContext: Context

    @Mock
    private lateinit var mockAssetManager: AssetManager

    private lateinit var dataSource: LocalSurvivalGuideDataSource

    @Before
    fun setUp() {
        `when`(mockContext.assets).thenReturn(mockAssetManager)
        dataSource = LocalSurvivalGuideDataSource(mockContext)
    }

    @Test
    fun testGetSurvivalContent_Success() {
        val dummyContent = "# Title\nSome content\n[img something.png]"
        val inputStream = ByteArrayInputStream(dummyContent.toByteArray())
        `when`(mockAssetManager.open("survival_manual.md")).thenReturn(inputStream)

        val content = dataSource.getSurvivalContent()

        assertNotNull(content)
        assertEquals("# Title\nSome content\n[img something.png]", content.content) // Verify linkImagesInMarkDown is applied
    }

    @Test(expected = DomainException.ContentNotFoundException::class)
    fun testGetSurvivalContent_FileNotFound() {
        `when`(mockAssetManager.open("survival_manual.md")).thenThrow(FileNotFoundException())

        dataSource.getSurvivalContent()
    }

    @Test
    fun testSearchSurvivalContent_Success() {
        val dummyContent = "# Title\nSome content with keyword\nAnother line"
        val inputStream = ByteArrayInputStream(dummyContent.toByteArray())
        `when`(mockAssetManager.open("survival_manual.md")).thenReturn(inputStream)

        val query = "keyword"
        val results = dataSource.searchSurvivalContent(query)

        assertEquals(1, results.size)
        assertEquals("# Title", results[0].title) // Assuming title extraction logic
        // Add more assertions to verify snippet and navigation info
    }

    @After
    fun tearDown() {
        // Teardown code here
    }

}