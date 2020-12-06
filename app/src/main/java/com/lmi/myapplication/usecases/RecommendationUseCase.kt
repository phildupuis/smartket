package com.lmi.myapplication.usecases

import com.lmi.myapplication.models.DayRecommendation
import io.reactivex.Observable

interface RecommendationUseCase {

    fun getRecommendationForStock(
        stockSymbols: String,
        socialMedia: String,
        periodStartDate: Int
    ): Observable<List<DayRecommendation>>

    fun getSocialMediaCount(
        stockSymbols: String,
        socialMedia: String,
    ): Observable<Int>
}