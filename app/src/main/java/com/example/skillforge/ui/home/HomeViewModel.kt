package com.example.skillforge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillforge.data.model.Category
import com.example.skillforge.data.repository.SkillforgeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val categories: List<Category>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomeViewModel(
    private val repository: SkillforgeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchData()
    }

    fun fetchData() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            try {
                val categories = repository.fetchCategories()
                _uiState.value = HomeUiState.Success(categories)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(e.localizedMessage ?: "Unknown error occurred")
            }
        }
    }
}
