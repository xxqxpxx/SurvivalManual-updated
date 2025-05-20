package org.ligi.survivalmanual

import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.use_case.GetImageUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository

class GetImageUseCaseTest {

    private lateinit var getImageUseCase: GetImageUseCase
    private val survivalGuideRepository = mock<SurvivalGuideRepository>()

    @Before
    fun setUp() {
        getImageUseCase = GetImageUseCase(survivalGuideRepository)
    }

    @Test
    fun `invoke calls repository getImageData with correct imageId and returns result`() =
        runBlocking {
            val imageId = "test_image_id"
            val expectedImageData = "dummy_image_data" // Replace with appropriate dummy data type
            whenever(survivalGuideRepository.getImageData(imageId)).thenReturn(expectedImageData)

            val result = getImageUseCase(imageId)

            assertEquals(expectedImageData, result)
        }

    @Test
    fun `invoke throws ContentNotFoundException when repository returns null`() = runBlocking {
        val imageId = "non_existent_image"
        whenever(survivalGuideRepository.getImageData(imageId)).thenReturn(null)

        assertThrows<DomainException.ContentNotFoundException> {
            getImageUseCase(imageId)
        }
    }

    @Test
    fun `invoke throws exception when repository throws exception`() = runBlocking {
        val imageId = "image_with_error"
        val errorMessage = "Failed to load image"
        whenever(survivalGuideRepository.getImageData(imageId)).thenThrow(
            RuntimeException(
                errorMessage
            )
        )

        val exception = assertThrows<DomainException.UnknownErrorException> {
            getImageUseCase(imageId)
        }
        assertEquals("An unknown error occurred: $errorMessage", exception.message)
    }
}