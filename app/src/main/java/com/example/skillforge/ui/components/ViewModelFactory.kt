package com.example.skillforge.ui.components

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.skillforge.data.repository.SkillforgeRepository

class RepositoryViewModelFactory<T : ViewModel>(
    private val repository: SkillforgeRepository,
    private val creator: (SkillforgeRepository) -> T
) : ViewModelProvider.Factory {
    override fun <V : ViewModel> create(modelClass: Class<V>): V {
        @Suppress("UNCHECKED_CAST")
        return creator(repository) as V
    }
}
