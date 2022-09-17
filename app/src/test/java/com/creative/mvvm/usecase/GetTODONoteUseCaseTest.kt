package com.creative.mvvm.usecase

import com.creative.mvvm.SchedulerProviderTest
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.usecase.notes.GetTODONoteUseCase
import io.mockk.every
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class GetTODONoteUseCaseTest : NoteRepoMockTest() {

    private lateinit var getTODONoteUseCase: GetTODONoteUseCase

    @Before
    fun setUp() {
        getTODONoteUseCase = GetTODONoteUseCase(SchedulerProviderTest, mockNoteRepo)
    }

    @Test
    fun `test note repo get success`() {
        every { mockNoteRepo.getNote(any()) } returns Single.just(Note(1, "title", "desc"))

        getTODONoteUseCase.execute(1)
            .test()
            .assertNoErrors()
            .assertNoTimeout()
            .assertValueCount(1)
            .assertValue(Note(1, "title", "desc"))
    }

    @Test
    fun `test note repo get error`(){
        every { mockNoteRepo.getNote(any()) } returns Single.error(Throwable("Error!"))

        getTODONoteUseCase.execute(1)
            .test()
            .assertNoTimeout()
            .assertValueCount(0)
            .assertError {
                it.message == "Error!"
            }
    }
}