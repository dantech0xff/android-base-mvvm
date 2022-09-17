package com.creative.mvvm.usecase

import com.creative.mvvm.SchedulerProviderTest
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.usecase.notes.FetchAllNotesUseCase
import io.mockk.every
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class FetchAllNotesUseCaseTest : NoteRepoMockTest() {

    private lateinit var fetchAllNotesUseCase: FetchAllNotesUseCase

    @Before
    fun setUp() {
        fetchAllNotesUseCase = FetchAllNotesUseCase(SchedulerProviderTest, noteRepo = mockNoteRepo)
    }


    @Test
    fun `fetch Notes repo return empty`() {
        every { mockNoteRepo.getSavedNotes() } returns Single.just(listOf())

        fetchAllNotesUseCase.execute(Unit)
            .test()
            .assertNoTimeout()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValueAt(0, listOf())
    }

    @Test
    fun `fetch Notes repo return items`(){
        every { mockNoteRepo.getSavedNotes() } returns Single.just(listOf(
            Note(1, "title 1", "desc 1"),
            Note(2, "title 2", "desc 2")
        ))

        fetchAllNotesUseCase.execute(Unit)
            .test()
            .assertNoTimeout()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValueAt(0, listOf(
                Note(1, "title 1", "desc 1"),
                Note(2, "title 2", "desc 2")
            ))
    }

    @Test
    fun `fetch Notes repo return error`() {
        every { mockNoteRepo.getSavedNotes() } returns Single.error(Throwable("Error!"))

        fetchAllNotesUseCase.execute(Unit)
            .test()
            .assertNoTimeout()
            .assertError {
                it.message != "Hello World!"
            }
            .assertError {
                it.message == "Error!"
            }
            .assertValueCount(0)
    }
}