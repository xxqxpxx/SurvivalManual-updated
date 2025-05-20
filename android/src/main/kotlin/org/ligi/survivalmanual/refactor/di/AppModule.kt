package org.ligi.survivalmanual.refactor.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ligi.survivalmanual.refactor.data.local.ImageDataSource
import org.ligi.survivalmanual.refactor.data.local.LocalSurvivalGuideDataSource
import org.ligi.survivalmanual.refactor.data.local.PreferencesDataSource
import org.ligi.survivalmanual.refactor.data.repository.SurvivalGuideRepositoryImpl
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
import org.ligi.survivalmanual.refactor.domain.use_case.GetSurvivalContentUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideLocalSurvivalGuideDataSource(): LocalSurvivalGuideDataSource {
        return LocalSurvivalGuideDataSource()
    }

    @Provides
    @Singleton
    fun provideSurvivalGuideRepository(
        localSurvivalGuideDataSource: LocalSurvivalGuideDataSource,
        preferencesDataSource: PreferencesDataSource,
        imageDataSource: ImageDataSource // Add ImageDataSource as a dependency
    ): SurvivalGuideRepository {
        return SurvivalGuideRepositoryImpl(
            localSurvivalGuideDataSource,
            preferencesDataSource,
            imageDataSource
        )
    }

    @Provides
    @Singleton
    fun provideGetSurvivalContentUseCase(
        survivalGuideRepository: SurvivalGuideRepository
    ): GetSurvivalContentUseCase {
        return GetSurvivalContentUseCase(survivalGuideRepository)
    }
}