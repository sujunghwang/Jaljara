package com.ssafy.jaljara.ui.vm

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.data.*
import com.ssafy.jaljara.network.ChildApiService
import com.ssafy.jaljara.network.ContentsApiService
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import retrofit2.http.Path
import java.io.File
import java.io.IOException


class ChildViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application
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
            val apiService = ChildApiService.getInstance(context)
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
            val apiService = ChildApiService.getInstance(context)
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
    fun getTodayMission(childId: Long) : TodayMission{
        var todayMission: TodayMission = TodayMission()
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance(context)
            try{
                Log.d("오늘의 미션 조회 API 호출 - childId","$childId")
                todayMission = apiService.getTodayMission(childId)
                todayMissionResponse = todayMission
            }
            catch (e:Exception){
                errorMessage = e.cause.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
        return todayMission
    }

    var usedCouponResponse: List<UsedCoupon> by mutableStateOf(listOf())
    fun getUsedCoupon(childId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance(context)
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
            val apiService = ChildApiService.getInstance(context)
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

    fun setMissionResult(childId: Long, file : MultipartBody.Part, context: Context){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance(context)
            try{
                Log.d("미션 수행 후 전송 API 호출 - childId","$childId, ${file.headers}")
                apiService.setMissionResult(childId, file)
                Toast.makeText(context, "전송 완료!", Toast.LENGTH_LONG).show()
            }
            catch (e:Exception){
                Toast.makeText(context, "전송 실패..ㅠㅠ", Toast.LENGTH_LONG).show()
                errorMessage = e.message.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }

    fun setCouponUsed(rewardId: Long){
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance(context)
            try{
                Log.d("쿠폰 사용 API 호출 - rewardId","$rewardId")
                apiService.setCouponUsed(rewardId)
            }
            catch (e:Exception){
                errorMessage = e.message.toString()
                Log.d("errorMessage","$errorMessage")
            }
        }
    }

    var rerollUiState: UiState<String> by mutableStateOf(UiState.Success(""))
    var reroll by mutableStateOf(3)
    fun getMissionReroll(userId : Long){
        rerollUiState = UiState.Loading
        viewModelScope.launch{
            val apiService = ChildApiService.getInstance(context)
            rerollUiState = try{
                Log.d("미션 재설정 API 호출 - userId","$userId")
                apiService.getMissionReroll(userId)
                UiState.Success("ok")
            }catch (e: IOException) {
                e.printStackTrace()
                UiState.Error("아이오 익셉션")
            } catch (e: HttpException) {
                e.printStackTrace()
                Log.d("errorCode",e.code().toString())
                if (e.code()==400){
                    reroll=0
                }
                UiState.Error("404나 400 같은거")
            } catch (e: Exception){
                e.printStackTrace()
                UiState.Error("알 수 없는 이유입니다.")
            }
        }
    }

}