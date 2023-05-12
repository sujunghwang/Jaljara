package com.ssafy.jaljara.ui.vm

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.data.*
import com.ssafy.jaljara.network.ChildApiService
import com.ssafy.jaljara.network.ParentApi
import com.ssafy.jaljara.network.ParentApiService
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.IOException

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

    var childList: List<ChildInfo> by mutableStateOf(listOf())
    fun getChildList(parentId : Long): List<ChildInfo>{
        var childListResponse: List<ChildInfo> = listOf()
        viewModelScope.launch {
            try{
                UiState.Success(childList)
                childListResponse = ParentApi.retrofitService.getChildList(parentId)
                childList = childListResponse
            }catch (e:Exception){
                errorMessage = e.message.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
        return childListResponse
    }

    var selectedChildIdx by mutableStateOf(0)

    fun deleteChild(childId : Long){
        viewModelScope.launch {
            try{
                Log.d("아이 등록 해제 API 호출 - childId","$childId")
                ParentApi.retrofitService.deleteChild(childId)
            }catch (e:Exception){
                errorMessage = e.cause.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }

    fun setMissionClear(childId: Long){
        viewModelScope.launch {
            try{
                ParentApi.retrofitService.setMissionClear(childId)
            }catch (e:Exception){
                errorMessage = e.message.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }

    var selectedChildId by mutableStateOf(0L)
    fun getChildIdByIdx(){
        selectedChildId = childList[selectedChildIdx].userId
    }

    var parentCode: ParentCode by mutableStateOf(ParentCode())
    fun getParentCode(parentId: Long){
        viewModelScope.launch {
            try{
                Log.d("부모 코드 API 호출 - parentId","$parentId")
                val parentCodeResponse = ParentApi.retrofitService.getParentCode(parentId)
                parentCode = parentCodeResponse
            }catch (e:Exception){
                errorMessage = e.cause.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }
}