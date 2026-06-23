package com.example.skillforge.data.repository

import com.example.skillforge.data.model.Category
import com.example.skillforge.data.model.Course
import com.example.skillforge.data.model.Instructor
import com.example.skillforge.data.model.Lesson
import com.example.skillforge.data.remote.SkillforgeApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.UUID

class SkillforgeRepository(
    private val api: SkillforgeApi = Retrofit.Builder()
        .baseUrl("https://raw.githubusercontent.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SkillforgeApi::class.java)
) {
    private var cachedCategories: List<Category>? = null

    suspend fun fetchCategories(): List<Category> {
        if (cachedCategories != null) {
            return cachedCategories!!
        }

        val response = api.getAppContent()
        
        val domainCategories = response.categories.map { apiCat ->
            val categoryId = UUID.randomUUID().toString()
            Category(
                id = categoryId,
                name = apiCat.name,
                courses = apiCat.courses.map { apiCourse ->
                    val courseId = UUID.randomUUID().toString()
                    Course(
                        id = courseId,
                        categoryId = categoryId,
                        categoryName = apiCat.name,
                        title = apiCourse.title,
                        rating = apiCourse.rating,
                        durationHours = apiCourse.durationHours,
                        thumbnailUrl = apiCourse.thumbnailUrl,
                        level = apiCourse.level,
                        instructor = Instructor(
                            name = apiCourse.instructor.name,
                            avatarUrl = apiCourse.instructor.avatarUrl,
                            title = apiCourse.instructor.title
                        ),
                        lessons = apiCourse.lessons.mapIndexed { index, apiLesson ->
                            Lesson(
                                id = UUID.randomUUID().toString(),
                                courseId = courseId,
                                title = apiLesson.title,
                                durationMinutes = apiLesson.durationMinutes,
                                isFree = apiLesson.isFree,
                                content = apiLesson.content,
                                orderIndex = index
                            )
                        }
                    )
                }
            )
        }

        cachedCategories = domainCategories
        return domainCategories
    }

    fun getCourseById(courseId: String): Course? {
        return cachedCategories?.flatMap { it.courses }?.find { it.id == courseId }
    }

    fun getLessonById(lessonId: String): Lesson? {
        return cachedCategories?.flatMap { it.courses }?.flatMap { it.lessons }?.find { it.id == lessonId }
    }
}
