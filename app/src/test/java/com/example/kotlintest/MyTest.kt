package com.example.kotlintest

import io.mockk.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.lang.Exception
import java.lang.NullPointerException
import java.lang.RuntimeException


class MyTest {

    // Don't forget to tell this
    //   excludeRecords { mock.operation(any(), 5) }
    @Before
    fun init() {

    }

    @After
    fun cleanup() {

    }

    @Test
    fun `call to addNumbers should return addition of two parameters`() {
        val doc1: DOC1 = mockk()
        val doc2 = mockk<DOC2>()

        // given
        every { doc1.value } returns 5
        every { doc2.value } returns "8"

        //***********************************************************************//
        // commenting below code will throw MockkException. Here it is
        //different from java mock that it doesn't return default value it throws
        // Exception directly if behavior is not specified.
        every { doc1.call(6) } returns 8
        //***********************************************************************//

        // when
        val sut = SUT(doc1, doc2)
        //sut.addNumbers()
        // val sut = SUT(5,"8") will also work but when DOC has more complex dependencies then mock is useful

        // then
        assertEquals(21, sut.addNumbers())

    }

    // argument matching
    @Test
    fun `calling call method of mock with different arguments`() {
        val mock: DOC1 = mockk()

        every { mock.call(more(1)) } returns 1
        every { mock.call(or(less(1), eq(5))) } returns -1



        every { mock.callWithMultipleArg(more(5), 6) } returns 1 // equivalent to eq(6)
    }

    // expected behavior
    @Test(expected = RuntimeException::class)
    fun `expected behavior test case`() {
        val doc: DOC1 = mockk()

        every { doc.call(5) } returnsMany listOf(1, 2, 3)
        // or
        every { doc.call(5) } returns 1 andThen 2 andThen 3

        // exception
        every { doc.call(5) } throws RuntimeException("error happened")
//        every { doc.callReturningUnit(5) } just Runs
//
//        //every { doc.call(any()) } answers { arg<Int>(0) } // where 0 is index of argument passed to call method.
//
//        every {
//            doc.callTwoParam(any(),9, any())
//       } answers {
//            arg<Int>(0)+ arg<Int>(1) + arg<String>(2).toInt()
//        } // where 0 is index of argument passed to call method.

        //every { doc.call(8) } returns 1


        //assertEquals(doc.call(5), NullPointerException())

        //assertEquals(doc.callTwoParam(0, 9, "@"), 25)
//        assertEquals(doc.callTwoParam(6, 9, "6"), 25)
    }

    @Test
    fun `capture arguments and perform assertion on them`(){
        val doc1 = mockk<DOC1>()
        val doc2 = mockk<DOC2>()

        val topThreeIds = listOf(1, 2, 3)

        every { doc1.getTopRatedIds(3) } returns topThreeIds

        every { doc2.getTopStringWithId(any()) } returns listOf()

        every { doc1.value } returns 5
        every { doc2.value } returns "8"

        val sut = SUT(doc1, doc2)

        //doc1.getTopRatedIds(3)

        val slot = slot<List<Int>>()


        sut.getTopThreeStringNames()
        sut.addNumbers()


        verify {
            doc2.getTopStringWithId(capture(slot))
            doc1.getTopRatedIds(3)

        }


        assertEquals(slot.captured, topThreeIds)



    }
    // ****************behaviour verification checks that mocked DOC is called *****************//
    @Test
    fun `test case using UNORDERED behavior verification at least once`() {
        val doc1 = mockk<DOC1>()
        val doc2 = mockk<DOC2>()

        every { doc1.value } returns 5
        every { doc2.value } returns "6"
        every { doc1.call(5) } returns 8
        val sut = SUT(doc1, doc2)

        //SUT is asserted
        sut.addNumbers()

        // place verification step at the end of the test after SUT is checked/asserted
        verify {
            doc1.call(5)  // will fail
            doc1.value // checks that doc1.value is accessed at least once
            doc2.value
        }
    }

    @Test
    fun `test case using UNORDERED behavior verification at least once and at most`() {
        val doc1 = mockk<DOC1>()
        val doc2 = mockk<DOC2>()

        every { doc1.value } returns 5
        every { doc2.value } returns "6"

        val sut = SUT(doc1, doc2)

        //SUT is asserted
        sut.addNumbers()

        // place verification step at the end of the test after SUT is checked/asserted
        verify(atLeast = 1, atMost = 1) {
            //doc1.call(5)  will fail
            doc1.value // checks that doc1.value is called at least once
            doc2.value
        }
        verify(exactly = 0) {
            doc1.callReturningUnit(5)
        }
    }

    @Test
    fun `test case using UNORDERED behavior verification with exactly attribute value changed`() {
        val doc1 = mockk<DOC1>()
        val doc2 = mockk<DOC2>()

        every { doc1.value } returns 5
        every { doc2.value } returns "6"

        val sut = SUT(doc1, doc2)


        // place verification step at the end of the test after SUT is checked/asserted
        verify(exactly = 1) {
            //doc1.call(5)  will fail
            doc1.value // checks that doc1.value is called at least once
            doc2.value
        }

        verify(exactly = 0) {
            doc1.call(5)
        }

        // below code ensures that doc1 mock was never accessed during call to SUT method addNumbers()
        // it is similar to verifyZeroInteractions in Mockito
        verify { doc1 wasNot called }   //please change code of addNumbers() in SUT to pass it.

        //SUT is asserted
        assertEquals(11, sut.addNumbers())
    }

    @Test
    fun `test case using ALL behavior verification`() {
        val doc1 = mockk<DOC1>()
        val doc2 = mockk<DOC2>()

        every { doc1.value } returns 5
        every { doc2.value } returns "6"
        every { doc1.call(5) } returns 8
        val sut = SUT(doc1, doc2)

        sut.addNumbers()

        verifyAll {
            doc1.value
            //doc2.value
            //***********************************************************************//
            // this will fail if not uncommented since verifyAll make sure you do not interact
            // with mocked object more that what is specified inside this block.
            //doc1.call(5)
            //***********************************************************************//

        }
    }

    @Test
    fun `test case using ORDERED behavior verification`() {
        val doc1 = mockk<DOC1>()
        val doc2 = mockk<DOC2>()

        every { doc1.value } returns 5
        every { doc2.value } returns "6"
        every { doc1.call(5) } returns 8
        val sut = SUT(doc1, doc2)

        sut.addNumbers()

        verifyOrder {
            doc1.value
//            doc1.call(5)
        }
    }


    @Test
    fun `test case using SEQUENTIAL behavior verification`() {
        //TODO
    }

}