package com.ssafy.jaljara.ui.vm

import android.graphics.Movie
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

    fun getChildSleepInfo(){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance()
            try{
                val childSleepInfo = apiService.getChildSleepInfo(15)
                childSleepResponse = childSleepInfo
            }
            catch (e:Exception){
                errorMessage = e.message.toString()
            }
        }
    }
}