package com.lmi.myapplication.models.impl

import com.lmi.myapplication.models.DataResult

class DataResultImpl<T>(private val isSuccess: Boolean, private val result: T?) : DataResult<T> {

    override fun getResult(): T? {
        return result
    }

    override fun isSuccess(): Boolean {
        return isSuccess
    }
}