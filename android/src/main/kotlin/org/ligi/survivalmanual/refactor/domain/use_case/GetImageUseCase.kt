package org.ligi.survivalmanual.refactor.domain.use_case

import org.ligi.survivalmanual.refactor.domain.error.DomainException
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
import javax.inject.Inject

class GetImageUseCase @Inject constructor(private val survivalGuideRepository: SurvivalGuideRepository) {

    suspend operator fun invoke(imageId: String): Result<ByteArray?> = try {
        val result = survivalGuideRepository.getImageData(imageId)
        Result.success(result)
    } catch (e: Exception) {
        Result.failure(DomainException.UnknownErrorException("Error fetching image data: ${e.message}"))
    }
}