package com.lmi.myapplication.models

interface DataResult<T> {

    fun getResult(): T?

    fun isSuccess(): Boolean
}