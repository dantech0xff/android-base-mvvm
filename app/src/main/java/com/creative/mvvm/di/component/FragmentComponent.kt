package com.creative.mvvm.di.component

import com.creative.mvvm.di.FragmentScope
import com.creative.mvvm.di.module.FragmentModule
import com.creative.mvvm.ui.note.NoteFragment
import com.creative.mvvm.ui.note.view.UpdateNoteFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class],
modules = [FragmentModule::class])
interface FragmentComponent {
    fun inject(fragment: NoteFragment)
    fun inject(fragment: UpdateNoteFragment)
}