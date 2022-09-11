package com.creative.mvvm.data.note

import com.creative.mvvm.data.AppDatabase
import com.creative.mvvm.data.note.entity.Note
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NoteRepo @Inject constructor (private val db: AppDatabase) {
    // insert notes
    fun insert(notes: Note) = db.getNoteDao().insertNotes(notes)

    // update notes
    fun update(notes: Note) = db.getNoteDao().updateNotes(notes)

    // get saved notes
    fun getSavedNotes() = db.getNoteDao().getNotes()

    // delete note by ID
    fun deleteNote(id: Int) = db.getNoteDao().deleteNote(id)

    fun getNote(id: Int) = db.getNoteDao().getNote(id)

    fun updateNotePhoto(id: Int, photoPath: String) = db.getNoteDao().updateNotePhoto(id, photoPath)
}