package com.itlab.ai.di

import com.itlab.ai.OpenVinoEngine
import com.itlab.ai.OpenVinoNoteAiService
import com.itlab.ai.ResultProcessor
import com.itlab.domain.ai.NoteAiService
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

val aiModule =
    module {
        single {
            val context = androidContext()
            val optimalRelativePath = OpenVinoEngine.getOptimalModelPath(context)
            val optimalAbsolutePath = File(context.filesDir, optimalRelativePath).absolutePath
            OpenVinoEngine(context, optimalAbsolutePath).also { engine ->
                runBlocking { engine.initialize() }
            }
        }
        single { ResultProcessor() }
        single<NoteAiService> {
            OpenVinoNoteAiService(engine = get(), processor = get())
        }
    }
