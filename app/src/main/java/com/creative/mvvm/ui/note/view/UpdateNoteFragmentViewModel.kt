package com.creative.mvvm.ui.note.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.google.AnalyticsHelper
import com.creative.mvvm.ui.base.BaseViewModel
import com.creative.mvvm.usecase.notes.DeleteNoteUseCase
import com.creative.mvvm.usecase.notes.GetTODONoteUseCase
import com.creative.mvvm.usecase.notes.InsertNoteUseCase
import com.creative.mvvm.usecase.notes.UpdateNoteUseCase
import com.creative.mvvm.utils.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateNoteFragmentViewModel
constructor(private val getTODONoteUseCase: GetTODONoteUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase) : BaseViewModel() {

    private val _noteLiveData: MutableLiveData<Note> = MutableLiveData()
    val noteLiveData: LiveData<Note> = _noteLiveData

    private val _noteUpdatedLiveData: MutableLiveData<Int> = MutableLiveData()
    val noteUpdatedLiveData: LiveData<Int> = _noteUpdatedLiveData

    private val _exitNoteLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val exitNoteLiveData: LiveData<Boolean> = _exitNoteLiveData

    fun getTodoNote(id: Int) {
        getTODONoteUseCase.execute(id)
            .subscribe({
                _noteLiveData.value = it
            }, {})
            .addTo(disposableCollector)
    }

    fun deleteNote(id: Int) {
        deleteNoteUseCase.execute(id)
            .subscribe({
                _exitNoteLiveData.value = true
            }, {
                _exitNoteLiveData.value = true
            }
            ).addTo(disposableCollector)
    }

    fun insertNote(note: Note, exit: Boolean = false) {
        if (note.description.isNotEmpty() || note.title.isNotEmpty() || note.photoPath.isNotEmpty()) {
            insertNoteUseCase.execute(note)
                .flatMap { i -> getTODONoteUseCase.execute(i.toInt()) }
                .subscribe({ t ->
                    _noteLiveData.value = t
                    _noteUpdatedLiveData.value = t.id
                    _exitNoteLiveData.value = exit
                }, {
                    _exitNoteLiveData.value = exit
                }).addTo(disposableCollector)
            AnalyticsHelper.logEventSizeNote(note.description.length.toLong())
        } else {
            _noteUpdatedLiveData.value = -1
            _exitNoteLiveData.value = exit
        }
    }

    fun updateNote(note: Note, exit: Boolean = false) {
        if (note.description.isNotEmpty() || note.title.isNotEmpty() || note.photoPath.isNotEmpty()) {
            updateNoteUseCase.execute(note)
                .ignoreElement()
                .andThen(getTODONoteUseCase.execute(note.id))
                .subscribe({ t ->
                    _noteLiveData.value = t
                    _noteUpdatedLiveData.value = t.id
                    _exitNoteLiveData.value = exit
                }, {
                    _exitNoteLiveData.value = exit
                }).addTo(disposableCollector)
            AnalyticsHelper.logEventSizeNote(note.description.length.toLong())
        } else {
            deleteNote(note.id)
        }
    }

    interface UpdateNoteFragmentUiEvent {
        fun addPhotoClick()
        fun fabSaveNoteClick()
    }
}