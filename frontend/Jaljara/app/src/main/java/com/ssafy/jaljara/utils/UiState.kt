package com.ssafy.jaljara.utils

sealed class UiState {
    data class Success<T>(val data: T): UiState()
    companion object Error: UiState()
    object Loading: UiState()
}