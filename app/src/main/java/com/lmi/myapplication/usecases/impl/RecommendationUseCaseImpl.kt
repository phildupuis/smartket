package com.lmi.myapplication.usecases.impl

import com.lmi.myapplication.datasources.StockDataSource
import com.lmi.myapplication.models.DayRecommendation
import com.lmi.myapplication.usecases.RecommendationUseCase
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class RecommendationUseCaseImpl(private val stockDataSource: StockDataSource) :
    RecommendationUseCase {

    override fun getRecommendationForStock(
        stockSymbols: String,
        socialMedia: String,
        timeLapseInDays: Int
    ): Observable<List<DayRecommendation>> {
        if (stockSymbols.isEmpty()) {
            return BehaviorSubject.createDefault(emptyList())
        }

        val now = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DATE_FIELD)
        val today: String = dateFormat
            .format(now)

        val calendar: Calendar = GregorianCalendar()
        calendar.time = now
        calendar.add(Calendar.DAY_OF_MONTH, timeLapseInDays)
        val recommendationEndDay: String = dateFormat
            .format(calendar.time)

        return Observables.combineLatest(
            stockDataSource.stockPriceGenerator(stockSymbols, today, recommendationEndDay),
            stockDataSource.socialMediaCountGenerator(stockSymbols, socialMedia)
        ) { pricesResult, mediaCountResult ->
            val prices: List<Pair<Double, Date>> =
                if (pricesResult.isSuccess()) {
                    pricesResult.getResult() ?: emptyList()
                } else {
                    emptyList()
                }

            val mediaCount =
                if (mediaCountResult.isSuccess()) {
                    mediaCountResult.getResult() ?: 0
                } else {
                    0
                }

            Pair(prices, mediaCount)
        }.switchMap { pricesAndMediaCount ->
            stockDataSource.recommendationAlgorithm(
                pricesAndMediaCount.first,
                pricesAndMediaCount.second
            ).map { recommendationResult ->
                if (recommendationResult.isSuccess() && recommendationResult.getResult() != null) {
                    recommendationResult.getResult()
                } else {
                    emptyList()
                }
            }
        }
    }

    override fun getSocialMediaCount(
        stockSymbols: String,
        socialMedia: String
    ): Observable<Int> {
        return stockDataSource.socialMediaCountGenerator(stockSymbols, socialMedia)
            .map {
                if (it.isSuccess()) {
                    it.getResult() ?: 0
                } else {
                    0
                }
            }
    }
}