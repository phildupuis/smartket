package com.lmi.myapplication.datasources.impl

import com.lmi.myapplication.datasources.StockDataSource
import com.lmi.myapplication.models.DataResult
import com.lmi.myapplication.models.DayRecommendation
import com.lmi.myapplication.models.Recommendation
import com.lmi.myapplication.models.impl.DataResultImpl
import com.lmi.myapplication.models.impl.DayRecommendationImpl
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class StockDataSourceMock : StockDataSource {

    companion object {
        const val PRICE_VARIATION_RANGE_PERCENTAGE = 0.2
    }

    override fun stockPriceGenerator(
        stockSymbol: String,
        startDate: String,
        endDate: String
    ): Observable<DataResult<List<Pair<Double, Date>>>> {
        val prices = BehaviorSubject.create<DataResult<List<Pair<Double, Date>>>>()

        val dateFormat = SimpleDateFormat.getDateInstance(DateFormat.DATE_FIELD)
        val startDay: Date? = dateFormat.parse(startDate)
        val endDay: Date? = dateFormat.parse(endDate)

        if (endDay != null && startDay != null) {
            val diff = endDay.time - startDay.time
            val timeLapseInDays = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)

            var latestPrice = generateRandomNumberInRange(0.0, 1000.0)

            val now = Calendar.getInstance().time
            val calendar: Calendar = GregorianCalendar()
            calendar.time = now

            val pricesList: ArrayList<Pair<Double, Date>> = ArrayList()
            for (i in 0 until timeLapseInDays) {
                latestPrice = generateRandomNumberInRange(latestPrice * (1 - PRICE_VARIATION_RANGE_PERCENTAGE), latestPrice * (1+ PRICE_VARIATION_RANGE_PERCENTAGE))
                pricesList.add(Pair(latestPrice, calendar.time))
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            prices.onNext(DataResultImpl(true, pricesList))
        } else {
            prices.onNext(DataResultImpl(true, emptyList()))
        }

        return prices
    }

    override fun socialMediaCountGenerator(
        stockSymbok: String,
        socialMedia: String
    ): Observable<DataResult<Int>> {
        val mediaCount = BehaviorSubject.create<DataResult<Int>>()

        mediaCount.onNext(DataResultImpl(true, generateRandomNumberInRange(0.0, 2000.0).toInt()))

        return mediaCount
    }

    override fun recommendationAlgorithm(
        prices: List<Pair<Double, Date>>,
        socialMediaCount: Int
    ): Observable<DataResult<List<DayRecommendation>>> {
        val recommendations = BehaviorSubject.create<DataResult<List<DayRecommendation>>>()

        val recommendationList = prices.map {
            val stockValue =
                BigDecimal(it.first.toString()).setScale(2, RoundingMode.HALF_UP).toDouble()
            DayRecommendationImpl(it.second, stockValue, Recommendation.HOLD.name)
        }

        recommendations.onNext(DataResultImpl(true, recommendationList))

        return recommendations
    }

    private fun generateRandomNumberInRange(min: Double, max: Double): Double {
        return min + (Math.random() * ((max - min) + 1))
    }
}