package com.creative.mvvm.di.module

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.creative.mvvm.data.note.NoteRepo
import com.creative.mvvm.factory.viewModelFactory
import com.creative.mvvm.ui.base.BaseFragment
import com.creative.mvvm.ui.note.NoteFragmentViewModel
import com.creative.mvvm.ui.note.view.UpdateNoteFragmentViewModel
import dagger.Module
import dagger.Provides

@Module
class FragmentModule (private val fragment: BaseFragment<*,*>) {

    @Provides
    fun provideLinearLayoutManager(): LinearLayoutManager = LinearLayoutManager(fragment.context)

    @Provides
    fun provideNoteFragmentViewModel(noteRepo: NoteRepo): NoteFragmentViewModel =
        ViewModelProvider(fragment, viewModelFactory { NoteFragmentViewModel(noteRepo) })[NoteFragmentViewModel::class.java]

    @Provides
    fun provideViewNoteFragmentViewModel(noteRepo: NoteRepo): UpdateNoteFragmentViewModel =
        ViewModelProvider(fragment, viewModelFactory { UpdateNoteFragmentViewModel(noteRepo) })[UpdateNoteFragmentViewModel::class.java]
}