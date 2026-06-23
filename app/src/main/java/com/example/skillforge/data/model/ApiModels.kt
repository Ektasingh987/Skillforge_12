package com.example.skillforge.data.model

import com.google.gson.annotations.SerializedName

data class ApiDataResponse(
    @SerializedName("categories") val categories: List<ApiCategory>
)

data class ApiCategory(
    @SerializedName("name") val name: String,
    @SerializedName("courses") val courses: List<ApiCourse>
)

data class ApiCourse(
    @SerializedName("title") val title: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("durationHours") val durationHours: Double,
    @SerializedName("thumbnailUrl") val thumbnailUrl: String,
    @SerializedName("level") val level: String,
    @SerializedName("instructor") val instructor: ApiInstructor,
    @SerializedName("lessons") val lessons: List<ApiLesson>
)

data class ApiInstructor(
    @SerializedName("name") val name: String,
    @SerializedName("avatarUrl") val avatarUrl: String,
    @SerializedName("title") val title: String
)

data class ApiLesson(
    @SerializedName("title") val title: String,
    @SerializedName("durationMinutes") val durationMinutes: Int,
    @SerializedName("isFree") val isFree: Boolean,
    @SerializedName("content") val content: String
)
