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

//    fun setSelectedChildIdx(idx: Long) {
//        _uiState.update { currentState ->
//            currentState.copy(
//                selectedChildrenIdx = idx
//            )
//        }
//        Log.d("선택 된 idx", _uiState.value.selectedChildrenIdx.toString())
//    }

//    fun getSelectedChildIdx() : Long{
//        return _uiState.value.selectedChildrenIdx
//    }

    var errorMessage: String by mutableStateOf("")

    var childSleepResponse: ChildSleepInfo by mutableStateOf(ChildSleepInfo())
    fun getChildSleepInfo(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                Log.d("아이 수면 목표 조회 API 호출 - childId","$childId")
                val childSleepInfo = apiService.getChildSleepInfo(childId)
                childSleepResponse = childSleepInfo
                Log.d("아이 수면 목표 조회 API 호출 - childId","$childSleepResponse")
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

//    var children: List<ChildInfo> by mutableStateOf(listOf())
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

//    fun getChildren(){
//        children=childList
//    }

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

//    fun getSelectedChildIdx():Int{
//        return selectedChildIdx
//    }
//    fun setSelectedChildIdx(idx: Int){
//        selectedChildIdx = idx
//    }
}