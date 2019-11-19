package com.example.kotlintest

import io.mockk.every
import io.mockk.mockk
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun `call to addNumbers should return addition of two parameters`(){
        val doc1: DOC1 = mockk()
        val doc2 = mockk<DOC2>()

        every { doc1.value } returns 5
        every { doc2.value } returns "8"

        val sut = SUT(doc1, doc2)
        // val sut = SUT(5,"8") will also work but when DOC has more complex dependencies then mock is useful

        assertEquals(sut.addNumbers(), 13)

    }
}
