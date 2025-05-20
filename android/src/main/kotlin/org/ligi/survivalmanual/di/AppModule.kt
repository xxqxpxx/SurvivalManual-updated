package org.ligi.survivalmanual.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import org.ligi.survivalmanual.data.local.ImageDataSource
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.ligi.survivalmanual.data.local.LocalSurvivalGuideDataSource
import org.ligi.survivalmanual.data.local.PreferencesDataSource 
import org.ligi.survivalmanual.domain.use_case.GetUserPreferencesUseCase
import org.ligi.survivalmanual.domain.use_case.SaveUserPreferencesUseCase 
import org.ligi.survivalmanual.data.repository.SurvivalGuideRepositoryImpl
import org.ligi.survivalmanual.domain.repository.SurvivalGuideRepository
import org.ligi.survivalmanual.domain.use_case.GetSurvivalContentUseCase
import org.ligi.survivalmanual.domain.use_case.GetImageUseCase
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
        return PreferencesDataSource(context)
    }

    @Provides
    @Singleton
    fun provideImageDataSource(@ApplicationContext context: Context): ImageDataSource {
        return ImageDataSource(context)
    }

    @Provides
    @Singleton
    fun provideSurvivalGuideRepository(localSurvivalGuideDataSource: LocalSurvivalGuideDataSource, preferencesDataSource: PreferencesDataSource 
 , imageDataSource: ImageDataSource
    ): SurvivalGuideRepository {
 return SurvivalGuideRepositoryImpl(localSurvivalGuideDataSource, preferencesDataSource, imageDataSource)
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
    fun provideGetUserPreferencesUseCase(survivalGuideRepository: SurvivalGuideRepository
    ): GetUserPreferencesUseCase {
        return GetUserPreferencesUseCase(survivalGuideRepository)
    }

    @Provides
    @Singleton
    fun provideSaveUserPreferencesUseCase(survivalGuideRepository: SurvivalGuideRepository
    ): SaveUserPreferencesUseCase {
        return SaveUserPreferencesUseCase(survivalGuideRepository)
    }

 @Provides
    @Singleton
 fun provideGetImageUseCase(survivalGuideRepository: SurvivalGuideRepository
 ): GetImageUseCase {
 return GetImageUseCase(survivalGuideRepository)
    }
}