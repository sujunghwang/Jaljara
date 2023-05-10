package com.ssafy.jaljara.ui.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.data.ContentsInfo
import com.ssafy.jaljara.data.ContentsListUiState
import com.ssafy.jaljara.data.ContentsUiState
import com.ssafy.jaljara.network.ContentsApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ContentsViewModel : ViewModel() {
    private val contentsListUiState = MutableStateFlow(ContentsListUiState())
    private val contentsUiState = MutableStateFlow(ContentsUiState())

    val _contentsListUiState: StateFlow<ContentsListUiState> = contentsListUiState.asStateFlow()
    val _contentsUiState: StateFlow<ContentsUiState> = contentsUiState.asStateFlow()

    fun setNavShow(isShow : Boolean) {
        contentsListUiState.update { currentState ->
            currentState.copy(
                showNavigation = isShow
            )
        }
    }

    var errorMessage: String by mutableStateOf("")


    var contentsListResponse: ContentsListUiState by mutableStateOf(ContentsListUiState())
    fun getContentsList() {
        viewModelScope.launch {
            val apiService = ContentsApiService.getInstance()
            try {
                Log.d("컨텐츠 리스트 API 호출", "g")
                    apiService.getContentsList()
                val contentsListUiState = apiService.getContentsList()
                contentsListResponse = contentsListUiState
            } catch (e:Exception) {
                errorMessage = e.message.toString()
                Log.d("errorMessage", "$errorMessage")
            }
        }
    }

    var contentsSoundListResponse: List<ContentsInfo> by mutableStateOf(listOf())
    fun getContentsSoundList() {
        viewModelScope.launch {
            val apiService = ContentsApiService.getInstance()
//            Log.d("컨텐츠 사운드 리스트 API 호출", "g123344444okjkl")
            try {
                Log.d("컨텐츠 사운드 리스트 API 호출", "컨텐츠 사운드 리스트 api 호출")
                apiService.getContentsTypeList("SOUND")
                val contentsSoundListUiState = apiService.getContentsTypeList("SOUND")
                contentsSoundListResponse = contentsSoundListUiState
            } catch (e:Exception) {
                errorMessage = e.message.toString()
                Log.d("errorMessage", "$errorMessage")
            }
        }
    }

    var contentsVideoListResponse: List<ContentsInfo> by mutableStateOf(listOf())
    fun getContentsVideoList() {
        viewModelScope.launch {
            val apiService = ContentsApiService.getInstance()
//            Log.d("컨텐츠 사운드 리스트 API 호출", "gq hfjnsadf")
            try {
                Log.d("컨텐츠 비디오 리스트 API 호출", "컨텐츠 비디오 리스트 api 호출")
                apiService.getContentsTypeList("VIDEO")
                val contentsVideoListUiState = apiService.getContentsTypeList("VIDEO")
                contentsVideoListResponse = contentsVideoListUiState
            } catch (e:Exception) {
                errorMessage = e.message.toString()
                Log.d("errorMessage", "$errorMessage")
            }
        }
    }

    var contentsResponse: ContentsUiState by mutableStateOf(ContentsUiState())
    fun getContents(contentsId: Long) {
        viewModelScope.launch {
            val apiService = ContentsApiService.getInstance()
            try {
                Log.d("컨텐츠 개별 API 호출", "g")
                apiService.getContents(contentsId)
                val contentsUiState = apiService.getContents(contentsId)
                contentsResponse = contentsUiState
            } catch (e:Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}