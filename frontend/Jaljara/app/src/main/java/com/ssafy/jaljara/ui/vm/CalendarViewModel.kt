package com.ssafy.jaljara.ui.vm

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.network.ParentApiService
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    var calendarUiState: UiState<List<Int>> by mutableStateOf(UiState.Loading)
        private set

    private val parentApiService = ParentApiService.getInstance(context = application)

    fun getSimpleSleepLog(childId : Long, date : String){
        viewModelScope.launch {
            calendarUiState = try{
                val simpleSleepLog = parentApiService.getSleepLogSimple(childId, date)
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