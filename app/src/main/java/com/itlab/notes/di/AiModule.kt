package com.itlab.notes.di

import android.content.Context
import com.itlab.ai.NoteAiServiceImpl
import com.itlab.domain.ai.NoteAiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AiModule {
    
    @Provides
    @Singleton
    fun provideNoteAiService(@ApplicationContext context: Context): NoteAiService {
        return NoteAiServiceImpl(context)
    }
}
