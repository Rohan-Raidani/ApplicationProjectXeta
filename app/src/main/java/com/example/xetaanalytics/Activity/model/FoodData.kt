
package com.example.xetaanalytics.Activity.model

data class FoodData(
    val description: String,
    val genericFacts: List<String>,
    val healthRating: Double,
    val nutritionInfoScaled: List<NutritionInfoScaled>,
    val similarItems: List<SimilarItem>,
)
//
//
data class NutritionInfoScaled(
    val name: String,
    val value: Int,
    val units: String
)
//


//
data class SimilarItem(
    val id: String,
    val name: String,
    val image: String
)
