package com.creative.mvvm.di.module

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.creative.mvvm.data.note.NoteRepo
import com.creative.mvvm.factory.viewModelFactory
import com.creative.mvvm.ui.base.BaseFragment
import com.creative.mvvm.ui.note.NoteFragmentViewModel
import com.creative.mvvm.ui.note.view.UpdateNoteFragmentViewModel
import com.creative.mvvm.usecase.notes.*
import dagger.Module
import dagger.Provides

@Module
class FragmentModule (private val fragment: BaseFragment<*,*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideNoteFragmentViewModel(fetchAllNotesUseCase: FetchAllNotesUseCase): NoteFragmentViewModel =
        ViewModelProvider(
            fragment,
            viewModelFactory { NoteFragmentViewModel(fetchAllNotesUseCase) })[NoteFragmentViewModel::class.java]

    @Provides
    fun provideViewNoteFragmentViewModel(
        getTODONoteUseCase: GetTODONoteUseCase,
        insertNoteUseCase: InsertNoteUseCase,
        deleteNoteUseCase: DeleteNoteUseCase,
        updateNoteUseCase: UpdateNoteUseCase
    ): UpdateNoteFragmentViewModel =
        ViewModelProvider(fragment, viewModelFactory {
            UpdateNoteFragmentViewModel(getTODONoteUseCase, insertNoteUseCase, deleteNoteUseCase, updateNoteUseCase)
        })[UpdateNoteFragmentViewModel::class.java]
}