package com.ssafy.jaljara.ui.vm

import androidx.lifecycle.ViewModel
import com.ssafy.jaljara.data.ParentUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ParentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ParentUiState())
    val uiState: StateFlow<ParentUiState> = _uiState.asStateFlow()

    fun setNavShow(isShow : Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
               showNavigation = isShow
            )
        }
    }


}