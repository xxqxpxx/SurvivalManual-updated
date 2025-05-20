package domain.use_case

import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository

class GetImageUseCase(private val survivalGuideRepository: SurvivalGuideRepository) {

    suspend operator fun invoke(imageIdentifier: String): ByteArray {
        // This is a placeholder. The repository method should be called here
        // to fetch the actual image data based on the identifier.
        // For now, returning an empty ByteArray as a placeholder.
        return survivalGuideRepository.getImageData(imageIdentifier)
    }
}