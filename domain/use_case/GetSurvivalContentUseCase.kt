package domain.use_case

import domain.error.ContentNotFoundException
import domain.error.NetworkException
import domain.error.UnknownErrorException
import domain.model.SurvivalContent
import domain.repository.SurvivalGuideRepository
import java.io.IOException
import javax.inject.Inject

class GetSurvivalContentUseCase @Inject constructor(
    private val survivalGuideRepository: SurvivalGuideRepository
) {
    suspend operator fun invoke(): SurvivalContent {
        try {
            return survivalGuideRepository.getSurvivalContent()
        } catch (e: Exception) {
            // Anticipate potential exceptions from the repository
            when (e) {
                is IOException -> throw NetworkException("Network error getting survival content", e)
                // Add more specific catches for other potential data layer exceptions
                // For now, assume any other exception means content not found or an unknown error
                is ContentNotFoundException -> throw e
                else -> throw UnknownErrorException("Unknown error getting survival content", e)
            }
        }
    }
}