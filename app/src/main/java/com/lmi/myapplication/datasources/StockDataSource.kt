package com.lmi.myapplication.datasources

import com.lmi.myapplication.models.DataResult
import com.lmi.myapplication.models.DayRecommendation
import io.reactivex.Observable
import java.util.*

interface StockDataSource {

    fun stockPriceGenerator(
        stockSymbol: String,
        startDate: String,
        endDate: String
    ): Observable<DataResult<List<Pair<Double, Date>>>>

    fun socialMediaCountGenerator(
        stockSymbok: String,
        socialMedia: String
    ): Observable<DataResult<Int>>

    fun recommendationAlgorithm(
        prices: List<Pair<Double, Date>>,
        socialMediaCount: Int
    ): Observable<DataResult<List<DayRecommendation>>>
}