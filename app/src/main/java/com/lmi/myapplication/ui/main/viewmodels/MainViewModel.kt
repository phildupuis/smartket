package com.lmi.myapplication.ui.main.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lmi.myapplication.datasources.impl.StockDataSourceMock
import com.lmi.myapplication.models.SocialMedia
import com.lmi.myapplication.usecases.RecommendationUseCase
import com.lmi.myapplication.usecases.impl.RecommendationUseCaseImpl
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {

    companion object {
        const val SOCIAL_MEDIA_POST_COUNT = "Number of social media posts for %s : %s"
    }

    val stockSymbolLabel: LiveData<String> = MutableLiveData("Stock Symbol")
    val stockSymbolPlaceholder: LiveData<String> = MutableLiveData("TSLA")
    val socialMediaLabel: LiveData<String> = MutableLiveData("Social Media")
    val socialMediaValues: LiveData<List<String>> = MutableLiveData(
        SocialMedia.values().map { it.name })
    val timeLapseInDayLabel: LiveData<String> = MutableLiveData("Time lapse in days")
    val checkForRecommendationButtonText: LiveData<String> = MutableLiveData("Check recommendation")
    val socialMediaCountResult: MutableLiveData<String> = MutableLiveData()

    val recommendations: MutableLiveData<List<RecommendationRowViewModel>> = MutableLiveData()

    private val compositeDisposable = CompositeDisposable()
    private val recommendationUseCase: RecommendationUseCase = RecommendationUseCaseImpl(
        StockDataSourceMock()
    )

    fun makeRecommendation(stockSymbol: String, timeLapseInDays: Int, socialMedia: String) {
        recommendationUseCase.getSocialMediaCount(stockSymbol, socialMedia)
            .subscribe {
                socialMediaCountResult.value = String.format(SOCIAL_MEDIA_POST_COUNT, stockSymbol, it)
            }
            .addTo(compositeDisposable)

        recommendationUseCase.getRecommendationForStock(stockSymbol, socialMedia, timeLapseInDays)
            .subscribe {
                val tableItemList = ArrayList<RecommendationRowViewModel>()
                tableItemList.add(RecommendationRowViewModel("Date", "Value", "Should"))

                val recommendations = it.map { dayRecommendation ->
                    val pattern = "MM/dd/yyyy"
                    val df: DateFormat = SimpleDateFormat(pattern)
                    val date: String = df.format(dayRecommendation.date())
                    RecommendationRowViewModel(
                        date,
                        dayRecommendation.stockValue().toString(),
                        dayRecommendation.recommendation()
                    )
                }
                tableItemList.addAll(recommendations)

                this.recommendations.value = tableItemList
            }
            .addTo(compositeDisposable)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}