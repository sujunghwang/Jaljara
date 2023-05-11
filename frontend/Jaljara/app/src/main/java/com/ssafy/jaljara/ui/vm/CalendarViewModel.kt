package com.ssafy.jaljara.ui.vm

import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.network.ParentApi
import com.ssafy.jaljara.ui.component.ErrorScreen
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CalendarViewModel : ViewModel() {
    var calendarUiState: UiState<List<Int>> by mutableStateOf(UiState.Loading)
        private set

    fun getSimpleSleepLog(childId : Long, date : String){
        viewModelScope.launch {
            calendarUiState = try{
                val simpleSleepLog = ParentApi.retrofitService.getSleepLogSimple(childId, date)
                UiState.Success(simpleSleepLog)
            }catch (e: IOException) {
                e.printStackTrace()
                UiState.Error("아이오 익셉션")
            } catch (e: HttpException) {
                e.printStackTrace()
                UiState.Error("404나 400 같은거")
            } catch (e: Exception){
                e.printStackTrace()
                UiState.Error("알 수 없는 이유입니다.")
            }
        }
    }
}