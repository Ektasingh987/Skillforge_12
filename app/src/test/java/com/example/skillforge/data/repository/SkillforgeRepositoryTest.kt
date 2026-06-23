package com.example.skillforge.data.repository

import com.example.skillforge.data.model.ApiCategory
import com.example.skillforge.data.model.ApiCourse
import com.example.skillforge.data.model.ApiDataResponse
import com.example.skillforge.data.model.ApiInstructor
import com.example.skillforge.data.model.ApiLesson
import com.example.skillforge.data.remote.SkillforgeApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class SkillforgeRepositoryTest {

    @Test
    fun `fetchCategories maps ApiDataResponse to Domain Models correctly`() = runTest {
        // Arrange
        val mockApi = object : SkillforgeApi {
            override suspend fun getAppContent(): ApiDataResponse {
                return ApiDataResponse(
                    categories = listOf(
                        ApiCategory(
                            name = "Android Development",
                            courses = listOf(
                                ApiCourse(
                                    title = "Kotlin Fundamentals",
                                    rating = 4.7,
                                    durationHours = 6.5,
                                    thumbnailUrl = "url",
                                    level = "Beginner",
                                    instructor = ApiInstructor("Aarav", "avatar", "Engineer"),
                                    lessons = listOf(
                                        ApiLesson("Welcome", 8, true, "Content")
                                    )
                                )
                            )
                        )
                    )
                )
            }
        }
        val repository = SkillforgeRepository(api = mockApi)

        // Act
        val categories = repository.fetchCategories()

        // Assert
        assertEquals(1, categories.size)
        val category = categories.first()
        assertEquals("Android Development", category.name)
        
        assertEquals(1, category.courses.size)
        val course = category.courses.first()
        assertEquals("Kotlin Fundamentals", course.title)
        assertEquals("Beginner", course.level)
        assertNotNull(course.id) // Verify ID was generated
        
        assertEquals(1, course.lessons.size)
        val lesson = course.lessons.first()
        assertEquals("Welcome", lesson.title)
        assertEquals(true, lesson.isFree)
        assertNotNull(lesson.id) // Verify ID was generated
        
        // Test caching / lookup
        val fetchedCourse = repository.getCourseById(course.id)
        assertNotNull(fetchedCourse)
        assertEquals(course.id, fetchedCourse?.id)
    }
}
