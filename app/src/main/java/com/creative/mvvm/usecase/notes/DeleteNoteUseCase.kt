package com.creative.mvvm.usecase.notes

import com.creative.mvvm.data.note.NoteRepo
import com.creative.mvvm.usecase.base.BaseSingleUseCase
import com.creative.mvvm.utils.BaseSchedulerProvider
import io.reactivex.Single
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(schedulerProvider: BaseSchedulerProvider, private val noteRepo: NoteRepo)
    : BaseSingleUseCase<Int, Int>(schedulerProvider.io(), schedulerProvider.main()) {
    override fun create(input: Int): Single<Int> {
        return noteRepo.deleteNote(input)
    }
}