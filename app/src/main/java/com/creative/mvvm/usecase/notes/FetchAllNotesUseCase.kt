package com.creative.mvvm.usecase.notes

import com.creative.mvvm.data.note.NoteRepo
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.usecase.base.BaseSingleUseCase
import com.creative.mvvm.utils.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class FetchAllNotesUseCase @Inject constructor(schedulerProvider: BaseSchedulerProvider,
                                               private val noteRepo: NoteRepo
) : BaseSingleUseCase<Unit, List<Note>>(schedulerProvider.io(), schedulerProvider.main()) {
    override fun create(input: Unit): Single<List<Note>> = noteRepo.getSavedNotes()
}