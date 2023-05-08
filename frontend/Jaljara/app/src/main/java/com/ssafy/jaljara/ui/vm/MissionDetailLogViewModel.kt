package com.ssafy.jaljara.ui.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.data.MissionLog
import com.ssafy.jaljara.data.SleepLog
import com.ssafy.jaljara.network.ParentApi
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MissionDetailLogViewModel : ViewModel() {
    var detailSleepLogUiState: UiState<SleepLog> by mutableStateOf(UiState.Loading)
        private set

    var missionLogUiState: UiState<MissionLog> by mutableStateOf(UiState.Loading)
        private set

    fun getDetailSleepLog(childId : Long, date : String){
        viewModelScope.launch {
            detailSleepLogUiState = try{
                val sleepLog = ParentApi.retrofitService.getSleepLogDetail(childId, date)
                UiState.Success(sleepLog)
            }catch (e: IOException) {
                e.printStackTrace()
                UiState.Error("아이오 익셉션")
            } catch (e: HttpException) {
                e.printStackTrace()
                UiState.Error("404나 400 뭐 그런거")
            } catch (e: Exception){
                e.printStackTrace()
                UiState.Error("알 수 없는 에러")
            }
        }
    }

    fun getMissionLog(userId : Long, date : String){
        viewModelScope.launch {
            missionLogUiState = try{
                val missionLog = ParentApi.retrofitService.getMissionLog(userId, date)
                UiState.Success(missionLog)
            }catch (e: IOException) {
                e.printStackTrace()
                UiState.Error("아이오 익셉션")
            } catch (e: HttpException) {
                e.printStackTrace()
                UiState.Error("404나 400 뭐 그런거")
            } catch (e: Exception){
                e.printStackTrace()
                UiState.Error("알 수 없는 에러")
            }
        }
    }
}