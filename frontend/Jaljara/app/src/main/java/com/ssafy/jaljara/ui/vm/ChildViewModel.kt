package com.ssafy.jaljara.ui.vm

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.data.*
import com.ssafy.jaljara.network.ChildApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ChildViewModel : ViewModel() {
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

    var childSleepResponse: ChildSleepInfo by mutableStateOf(ChildSleepInfo())
    fun getChildSleepInfo(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("아이 수면 목표 조회 API 호출 - childId","$childId")
                val childSleepInfo = apiService.getChildSleepInfo(childId)
                childSleepResponse = childSleepInfo
            }
            catch (e:Exception){
                errorMessage = e.cause.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }

    fun getReward(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("보상 획득 API 호출 - childId","$childId")
                apiService.getReward(childId)
                val childSleepInfo = apiService.getChildSleepInfo(childId)
                childSleepResponse = childSleepInfo
            }
            catch (e:Exception){
                errorMessage = e.message.toString()
            }
        }
    }
    
    var todayMissionResponse: TodayMission by mutableStateOf(TodayMission())
    fun getTodayMission(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("오늘의 미션 조회 API 호출 - childId","$childId")
                val todayMission = apiService.getTodayMission(childId)
                todayMissionResponse = todayMission
            }
            catch (e:Exception){
                errorMessage = e.cause.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }

    var usedCouponResponse: List<UsedCoupon> by mutableStateOf(listOf())
    fun getUsedCoupon(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("사용한 쿠폰 리스트 조회 API 호출 - childId","$childId")
                val usedCoupon = apiService.getUsedCoupon(childId)
                usedCouponResponse = usedCoupon
            }
            catch (e:Exception){
                errorMessage = e.message.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }


    var notUsedCouponResponse: List<NotUsedCoupon> by mutableStateOf(listOf())
    fun getNotUsedCoupon(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("사용하지 않은 쿠폰 리스트 조회 API 호출 - childId","$childId")
                val notUsedCoupon = apiService.getNotUsedCoupon(childId)
                notUsedCouponResponse = notUsedCoupon
            }
            catch (e:Exception){
                errorMessage = e.message.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }
}