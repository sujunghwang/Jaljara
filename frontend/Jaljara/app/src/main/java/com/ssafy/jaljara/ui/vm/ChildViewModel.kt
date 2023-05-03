package com.ssafy.jaljara.ui.vm

import android.graphics.Movie
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.data.ChildSleepInfo
import com.ssafy.jaljara.network.ChildApiService
import kotlinx.coroutines.launch


class ChildViewModel : ViewModel() {
    var childSleepResponse: ChildSleepInfo by mutableStateOf(ChildSleepInfo())
    var errorMessage: String by mutableStateOf("")

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
}