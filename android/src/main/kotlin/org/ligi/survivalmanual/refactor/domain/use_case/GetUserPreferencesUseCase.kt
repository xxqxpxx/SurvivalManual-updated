package domain.use_case

import domain.model.UserPreferences
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository

class GetUserPreferencesUseCase(
    private val survivalGuideRepository: SurvivalGuideRepository
) {
    suspend operator fun invoke(): UserPreferences {
        return survivalGuideRepository.getUserPreferences()
    }
}