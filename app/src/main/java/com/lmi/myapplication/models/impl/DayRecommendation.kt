package com.lmi.myapplication.models.impl

import com.lmi.myapplication.models.DayRecommendation
import java.util.*

class DayRecommendationImpl(
    private val date: Date,
    private val stockValue: Double,
    private val recommendation: String
) : DayRecommendation {
    override fun date(): Date {
        return date
    }

    override fun stockValue(): Double {
        return stockValue
    }

    override fun recommendation(): String {
        return recommendation
    }
}