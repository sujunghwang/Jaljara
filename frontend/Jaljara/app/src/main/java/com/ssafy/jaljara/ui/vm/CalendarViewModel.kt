package com.ssafy.jaljara.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.network.ParentApi
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CalendarViewModel : ViewModel() {
    var calendarUiState: UiState by mutableStateOf(UiState.Loading)
        private set

    fun getSimpleSleepLog(childId : Long, date : String){
        viewModelScope.launch {
            calendarUiState = try{
                val simpleSleepLog = ParentApi.retrofitService.getSleepLogSimple(childId, date)
                UiState.Success(simpleSleepLog)
            }catch (e: IOException) {
                e.printStackTrace()
                UiState.Error
            } catch (e: HttpException) {
                e.printStackTrace()
                UiState.Error
            } catch (e: Exception){
                e.printStackTrace()
                UiState.Error
            }
        }
    }
}