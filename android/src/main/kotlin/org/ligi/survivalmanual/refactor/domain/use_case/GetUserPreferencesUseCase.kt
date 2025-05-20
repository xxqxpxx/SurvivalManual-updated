package org.ligi.survivalmanual.refactor.domain.use_case

 import org.ligi.survivalmanual.refactor.domain.UserPreferences
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository

class GetUserPreferencesUseCase(
    private val survivalGuideRepository: SurvivalGuideRepository
) {
    suspend operator fun invoke(): UserPreferences {
        return survivalGuideRepository.getUserPreferences()
    }
}