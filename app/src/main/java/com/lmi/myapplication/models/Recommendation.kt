package com.lmi.myapplication.models

enum class Recommendation(name: String) {
    SELL("sell"),
    HOLD("hold"),
    BUY("buy");

    companion object {
        fun findRecommendationByName(name: String) : Recommendation? {
            return values()
                .find {
                    it.name == name
                }
        }
    }
}