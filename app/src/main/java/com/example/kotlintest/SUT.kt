package com.example.kotlintest

class DOC1(val value : Int){
    fun call(arg: Int): Int = 8
    fun callTwoParam(arg: Int, secondParam : Int, stringParam : String): Int =
        arg + secondParam + stringParam.toInt() + call(5)
    fun callWithMultipleArg(arg1: Int, arg2 : Int) = arg1 + arg2
    fun callReturningUnit(arg: Int) {

    }

    fun getTopRatedIds(num: Int): List<Int> {
        return listOf()
    }
}

class DOC2(val value : String) {
    fun getTopStringWithId(ids: List<Int>?): List<String> {
        return listOf()
    }
}

public class SUT (val doC1: DOC1, val doC2: DOC2){


    fun addNumbers() = doC1.value + doC2.value.toInt() + doC1.call(5)
    // fun addNumbers() = 0
    fun getTopThreeStringNames(): List<String> {
        val ids = doC1.getTopRatedIds(3)
        return doC2.getTopStringWithId(ids)
    }
}