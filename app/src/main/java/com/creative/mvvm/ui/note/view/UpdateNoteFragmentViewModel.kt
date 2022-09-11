package com.creative.mvvm.ui.note.view

import androidx.lifecycle.MutableLiveData
import com.creative.mvvm.data.note.NoteRepo
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.google.AnalyticsHelper
import com.creative.mvvm.ui.base.BaseViewModel
import com.creative.mvvm.utils.addTo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UpdateNoteFragmentViewModel(private val noteRepo: NoteRepo) : BaseViewModel() {

    val noteLiveData: MutableLiveData<Note> = MutableLiveData()
    val noteUpdatedLiveData: MutableLiveData<Int> = MutableLiveData()
    val exitNoteLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getTodoNote(id: Int) {
        noteRepo.getNote(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                noteLiveData.value = it
            }, {})
            .addTo(disposableCollector)
    }

    fun deleteNote(id: Int) {
        noteRepo.deleteNote(id).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ t ->
                exitNoteLiveData.value = true
            }, {
                exitNoteLiveData.value = true
            }
            ).addTo(disposableCollector)
    }

    fun insertNote(note: Note, exit: Boolean = false) {
        if (note.description.isNotEmpty() || note.title.isNotEmpty() || note.photoPath.isNotEmpty()) {
            noteRepo.insert(note)
                .flatMap { i -> noteRepo.getNote(i.toInt()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    noteLiveData.value = t
                    noteUpdatedLiveData.value = t.id
                    exitNoteLiveData.value = exit
                }, {
                    exitNoteLiveData.value = exit
                }).addTo(disposableCollector)
            AnalyticsHelper.logEventSizeNote(note.description.length.toLong())
        } else {
            noteUpdatedLiveData.value = -1
            exitNoteLiveData.value = exit
        }
    }

    fun updateNote(note: Note, exit: Boolean = false) {
        if (note.description.isNotEmpty() || note.title.isNotEmpty() || note.photoPath.isNotEmpty()) {
            noteRepo.update(note)
                .flatMap { noteRepo.getNote(note.id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ t ->
                    noteLiveData.value = t
                    noteUpdatedLiveData.value = t.id
                    exitNoteLiveData.value = exit
                }, {
                    exitNoteLiveData.value = exit
                }).addTo(disposableCollector)
            AnalyticsHelper.logEventSizeNote(note.description.length.toLong())
        } else {
            deleteNote(note.id)
        }
    }
}