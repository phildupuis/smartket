package com.lmi.myapplication.models

import java.util.*

interface DayRecommendation {

    fun date() : Date

    fun stockValue() : Double

    fun recommendation() : String
}