package com.example.skillforge.data.model

data class Category(
    val id: String,
    val name: String,
    val courses: List<Course>
)

data class Course(
    val id: String,
    val categoryId: String,
    val categoryName: String,
    val title: String,
    val rating: Double,
    val durationHours: Double,
    val thumbnailUrl: String,
    val level: String,
    val instructor: Instructor,
    val lessons: List<Lesson>
) {
    val totalLessons: Int get() = lessons.size
    val totalDurationMinutes: Int get() = lessons.sumOf { it.durationMinutes }
}

data class Instructor(
    val name: String,
    val avatarUrl: String,
    val title: String
)

data class Lesson(
    val id: String,
    val courseId: String,
    val title: String,
    val durationMinutes: Int,
    val isFree: Boolean,
    val content: String,
    val orderIndex: Int
)
