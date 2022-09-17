package com.creative.mvvm.usecase

import com.creative.mvvm.data.note.NoteRepo
import io.mockk.mockk

abstract class NoteRepoMockTest {
    protected val mockNoteRepo: NoteRepo = mockk()
}