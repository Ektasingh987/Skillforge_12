package com.example.skillforge

import android.app.Application
import com.example.skillforge.data.repository.SkillforgeRepository

class SkillforgeApplication : Application() {

    // Single shared instance of the repository
    lateinit var repository: SkillforgeRepository
        private set

    override fun onCreate() {
        super.onCreate()
        repository = SkillforgeRepository()
    }
}
