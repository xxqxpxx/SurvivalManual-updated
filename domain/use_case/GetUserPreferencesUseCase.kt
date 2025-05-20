package domain.use_case

import domain.model.UserPreferences

class GetUserPreferencesUseCase(
 private val survivalGuideRepository: domain.repository.SurvivalGuideRepository
) {
 suspend operator fun invoke(): UserPreferences {
        return survivalGuideRepository.getUserPreferences()
 }
}