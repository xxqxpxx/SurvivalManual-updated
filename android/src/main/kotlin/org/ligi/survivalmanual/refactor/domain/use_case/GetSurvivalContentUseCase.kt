package org.ligi.survivalmanual.refactor.domain.use_case

import org.ligi.survivalmanual.refactor.domain.SurvivalContent
import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
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
                is IOException -> throw DomainException.NetworkException(
                    "Network error getting survival content"
                )
                // Add more specific catches for other potential data layer exceptions
                // For now, assume any other exception means content not found or an unknown error
                is DomainException.ContentNotFoundException -> throw e
                else -> throw DomainException.UnknownErrorException(
                    "Unknown error getting survival content"
                )
            }
        }
    }
}