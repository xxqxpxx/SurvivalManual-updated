package domain.test

import domain.model.SurvivalContent
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
import org.ligi.survivalmanual.refactor.domain.use_case.GetSurvivalContentUseCase
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.stub
import org.mockito.kotlin.verify
import java.io.IOException

class GetSurvivalContentUseCaseTest {

    @Mock
    private lateinit var repository: SurvivalGuideRepository

    private lateinit var getSurvivalContentUseCase: GetSurvivalContentUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getSurvivalContentUseCase = GetSurvivalContentUseCase(repository)
    }

    @Test
    fun `invoke calls repository and returns content`() = runBlocking {
        val dummyContent = SurvivalContent("Test Content", "Test Category")
        repository.stub {
            onBlocking { getSurvivalContent() } doReturn dummyContent
        }

        val result = getSurvivalContentUseCase.invoke()

        verify(repository).getSurvivalContent()
        kotlin.test.assertEquals(dummyContent, result)
    }

    @Test(expected = IOException::class) // Assuming repository throws IOException
    fun `invoke throws exception when repository throws exception`() = runBlocking {
        repository.stub {
            onBlocking { getSurvivalContent() } doReturn { throw IOException("Network Error") }
        }

        getSurvivalContentUseCase.invoke()
    }
}