package com.ssafy.jaljara.ui.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.data.ChildSleepInfo
import com.ssafy.jaljara.data.ParentUiState
import com.ssafy.jaljara.data.TargetSleepInput
import com.ssafy.jaljara.network.ChildApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    var errorMessage: String by mutableStateOf("")
    var childSleepInfo: ChildSleepInfo by mutableStateOf(ChildSleepInfo())
    fun getChildSleepInfo(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("아이 수면 목표 조회 API 호출 - childId","$childId")
                val response = apiService.getChildSleepInfo(childId)
                childSleepInfo = response
            }
            catch (e:Exception){
                errorMessage = e.cause.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }

    fun setTargetSleepTime(childId: Long, targetBedTime: String, targetWakeupTime: String){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("목표 수면 시간 설정 API 호출","$childId, $targetBedTime, $targetWakeupTime")
                apiService.setTargetSleepTime(TargetSleepInput(childId, targetBedTime, targetWakeupTime))
            }
            catch (e:Exception){
                errorMessage = e.cause.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }
}