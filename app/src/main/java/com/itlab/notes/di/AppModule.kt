package com.itlab.notes.di

import com.itlab.domain.usecase.CreateFolderUseCase
import com.itlab.domain.usecase.CreateNoteUseCase
import com.itlab.domain.usecase.DeleteFolderUseCase
import com.itlab.domain.usecase.DeleteNoteUseCase
import com.itlab.domain.usecase.ObserveFoldersUseCase
import com.itlab.domain.usecase.ObserveNotesByFolderUseCase
import com.itlab.domain.usecase.UpdateNoteUseCase
import org.koin.dsl.module

// import com.itlab.notes.ui.NotesUseCases
// import com.itlab.notes.ui.NotesViewModel
// import org.koin.androidx.viewmodel.dsl.viewModel
val appModule =
    module {
        factory { CreateNoteUseCase(get()) }
        factory { CreateFolderUseCase(get()) }
        factory { DeleteFolderUseCase(get()) }
        factory { DeleteNoteUseCase(get()) }
        factory { UpdateNoteUseCase(get()) }
        factory { ObserveNotesByFolderUseCase(get()) }
        factory { ObserveFoldersUseCase(get()) }
//        factory {
//            NotesUseCases(
//                createFolderUseCase = get(),
//                deleteFolderUseCase = get(),
//                createNoteUseCase = get(),
//                deleteNoteUseCase = get(),
//                updateNoteUseCase = get(),
//                observeNotesByFolderUseCase = get(),
//                observeFoldersUseCase = get(),
//            )
//        }
//
//        viewModel {
//            NotesViewModel(
//                useCases = get(),
//            )
//        }
    }
