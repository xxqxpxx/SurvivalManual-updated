package org.ligi.survivalmanual.refactor.domain.use_case

 import org.ligi.survivalmanual.refactor.domain.UserPreferences
 import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository

class SaveUserPreferencesUseCase(
    private val survivalGuideRepository: SurvivalGuideRepository
) {
    suspend operator fun invoke(preferences: UserPreferences) {
        survivalGuideRepository.saveUserPreferences(preferences)
    }
}