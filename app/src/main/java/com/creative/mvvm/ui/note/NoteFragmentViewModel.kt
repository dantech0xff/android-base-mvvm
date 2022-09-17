package com.creative.mvvm.ui.note

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.ui.base.BaseViewModel
import com.creative.mvvm.usecase.notes.FetchAllNotesUseCase
import com.creative.mvvm.utils.addTo

class NoteFragmentViewModel(
    private val fetchAllNotesUseCase: FetchAllNotesUseCase) : BaseViewModel() {

    private val _listNoteLiveData: MutableLiveData<List<Note>> = MutableLiveData()
    val listNoteLiveData: LiveData<List<Note>> = _listNoteLiveData

    fun updateListNotes() {
        fetchAllNotesUseCase.execute(Unit)
            .subscribe({
                _listNoteLiveData.value = it
            }, {
                _listNoteLiveData.value = ArrayList()
            }).addTo(disposableCollector)
    }

    interface NoteFragmentUiEvent {
        fun addNewNoteClick()
    }
}