package org.ligi.survivalmanual.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ligi.survivalmanual.data.local.LocalSurvivalGuideDataSource
import org.ligi.survivalmanual.data.repository.SurvivalGuideRepositoryImpl
import org.ligi.survivalmanual.domain.repository.SurvivalGuideRepository
import org.ligi.survivalmanual.domain.use_case.GetSurvivalContentUseCase
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
        localSurvivalGuideDataSource: LocalSurvivalGuideDataSource
    ): SurvivalGuideRepository {
        return SurvivalGuideRepositoryImpl(localSurvivalGuideDataSource)
    }

    @Provides
    @Singleton
    fun provideGetSurvivalContentUseCase(
        survivalGuideRepository: SurvivalGuideRepository
    ): GetSurvivalContentUseCase {
        return GetSurvivalContentUseCase(survivalGuideRepository)
    }
}