package com.creative.mvvm.usecase

import com.creative.mvvm.SchedulerProviderTest
import com.creative.mvvm.data.note.entity.Note
import com.creative.mvvm.usecase.notes.UpdateNoteUseCase
import io.mockk.every
import io.reactivex.Single
import org.junit.Before
import org.junit.Test

class UpdateNoteUseCaseTest : NoteRepoMockTest() {
    private lateinit var updateNoteUseCase: UpdateNoteUseCase

    @Before
    fun setUp() {
        updateNoteUseCase = UpdateNoteUseCase(SchedulerProviderTest, mockNoteRepo)
    }

    @Test
    fun `test note repo update success`() {
        every { mockNoteRepo.update(any()) } returns Single.just(1)

        updateNoteUseCase.execute(Note(1, "title", "desc"))
            .test()
            .assertNoErrors()
            .assertNoTimeout()
            .assertValue(1)
            .assertValueCount(1)
    }

    @Test
    fun `test note repo update error` () {
        every { mockNoteRepo.update(any()) } returns Single.error(Throwable("Error!"))

        updateNoteUseCase.execute(Note(1, "title", "desc"))
            .test()
            .assertNoTimeout()
            .assertError {
                it.message == "Error!"
            }
    }
}