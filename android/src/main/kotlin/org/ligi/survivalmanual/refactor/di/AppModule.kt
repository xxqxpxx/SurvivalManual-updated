package org.ligi.survivalmanual.refactor.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.ligi.survivalmanual.refactor.data.local.ImageDataSource
import org.ligi.survivalmanual.refactor.data.local.LocalSurvivalGuideDataSource
import org.ligi.survivalmanual.refactor.data.local.PreferencesDataSource
import org.ligi.survivalmanual.refactor.data.repository.SurvivalGuideRepositoryImpl
import org.ligi.survivalmanual.refactor.domain.repository.SurvivalGuideRepository
import org.ligi.survivalmanual.refactor.domain.use_case.GetUserPreferencesUseCase
import org.ligi.survivalmanual.refactor.domain.use_case.SaveUserPreferencesUseCase
import org.ligi.survivalmanual.refactor.domain.use_case.SearchSurvivalContentUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun providePreferencesDataSource(@ApplicationContext context: Context): PreferencesDataSource {
        // TODO: Replace with your actual PreferencesDataSource implementation
        return PreferencesDataSource(context)
    }

    @Provides
    @Singleton
    fun provideImageDataSource(@ApplicationContext context: Context): ImageDataSource {
        // TODO: Replace with your actual ImageDataSource implementation
 return ImageDataSource(context)
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

    @Provides
    @Singleton
    fun provideSearchSurvivalContentUseCase(
        survivalGuideRepository: SurvivalGuideRepository
    ): SearchSurvivalContentUseCase {
        return SearchSurvivalContentUseCase(survivalGuideRepository)
    }

    @Provides
    @Singleton
    fun provideGetUserPreferencesUseCase(
        survivalGuideRepository: SurvivalGuideRepository
    ): GetUserPreferencesUseCase {
        return GetUserPreferencesUseCase(survivalGuideRepository)
    }

    @Provides
    @Singleton
    fun provideSaveUserPreferencesUseCase(survivalGuideRepository: SurvivalGuideRepository): SaveUserPreferencesUseCase {
 return SaveUserPreferencesUseCase(survivalGuideRepository)
    }
}