package com.programmerbox.dragswipeapp

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)

        val v = Tested(5)
        val f = Tested(5)//Tested::class.java.getConstructor(Tested::class.java).newInstance(65) as Tested
        val g = ::Tested
        //val g2 = ::Tested2.invoke(5)
        val g2 = Tested2.create(5)
        println("v is ${v.v} and f is ${f.v} and g is ${g.invoke(7).v}")

        `this is a test`()

    }

    class Tested(var v: Int)

    abstract class Tested2(var v: Int) {
        companion object {
            fun create(c: Int): Tested2 = object : Tested2(c) {}
        }
    }

    @Test
    fun `this is a test`() {
        println("This really works huh?")
    }

}
