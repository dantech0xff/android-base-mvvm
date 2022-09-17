package com.creative.mvvm.usecase

import com.creative.mvvm.SchedulerProviderTest
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.usecase.notes.InsertNoteUseCase
import io.mockk.every
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class InsertNoteUseCaseTest : NoteRepoMockTest() {

    private lateinit var insertNoteUseCase: InsertNoteUseCase

    @Before
    fun setUp() {
        insertNoteUseCase = InsertNoteUseCase(SchedulerProviderTest, noteRepo = mockNoteRepo)
    }

    @Test
    fun `test note repo insert success`() {
        every {
            mockNoteRepo.insert(
                Note(
                    1, "title", "desc"
                )
            )
        } returns Single.just(1)

        insertNoteUseCase.execute(Note(1, "title", "desc"))
            .test()
            .assertNoTimeout()
            .assertNoErrors()
            .assertValueCount(1)
            .assertValue(1)
    }

    @Test
    fun `test note repo insert error`() {
        every {
            mockNoteRepo.insert(
                Note(
                    1, "title", "desc"
                )
            )
        } returns Single.error(Throwable("Error!"))

        insertNoteUseCase.execute(Note(1, "title", "desc"))
            .test()
            .assertNoTimeout()
            .assertError {
                it.message == "Error!"
            }
            .assertNoValues()
    }
}