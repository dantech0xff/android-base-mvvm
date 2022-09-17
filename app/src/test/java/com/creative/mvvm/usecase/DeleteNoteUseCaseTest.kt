package com.creative.mvvm.usecase

import com.creative.mvvm.SchedulerProviderTest
import com.creative.mvvm.usecase.notes.DeleteNoteUseCase
import io.mockk.every
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class DeleteNoteUseCaseTest : NoteRepoMockTest() {

    private lateinit var deleteNoteUseCase: DeleteNoteUseCase

    @Before
    fun setUp() {
        deleteNoteUseCase = DeleteNoteUseCase(SchedulerProviderTest, mockNoteRepo)
    }

    @Test
    fun `test note repo delete note success`() {
        every { mockNoteRepo.deleteNote(any()) } returns Single.just(1)

        deleteNoteUseCase.execute(1)
            .test().assertNoTimeout().assertNoErrors().assertValueCount(1)
    }

    @Test
    fun `test note repo delete note failed`() {
        every { mockNoteRepo.deleteNote(any()) } returns Single.error(Throwable("Error!"))

        deleteNoteUseCase.execute(1)
            .test().assertNoTimeout().assertError {
                it.message == "Error!"
            }.assertValueCount(0)
    }
}