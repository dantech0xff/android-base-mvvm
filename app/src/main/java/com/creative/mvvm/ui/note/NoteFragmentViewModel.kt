package com.creative.mvvm.ui.note

import androidx.lifecycle.MutableLiveData
import com.creative.mvvm.data.note.NoteRepo
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.ui.base.BaseViewModel
import com.creative.mvvm.utils.addTo
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.disposables.Disposables
import io.reactivex.schedulers.Schedulers

class NoteFragmentViewModel(private val noteRepo: NoteRepo) : BaseViewModel() {

    val listNoteLiveData: MutableLiveData<List<Note>> = MutableLiveData()

    fun updateListNotes() {
        noteRepo.getSavedNotes().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                listNoteLiveData.value = it
            }, {
                listNoteLiveData.value = ArrayList()
            }).addTo(disposableCollector)
    }
}