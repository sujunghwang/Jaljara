package com.ssafy.jaljara.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent
import com.ssafy.jaljara.network.ChildApiService
import com.ssafy.jaljara.network.Result
import com.ssafy.jaljara.network.safeApiCall
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDateTime
import java.io.File
import java.time.Instant
import java.time.ZoneId

class SleepReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "SleepReceiver"
    }

    private val scope: CoroutineScope = MainScope()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive(): $intent")

        // Extract sleep events from intent.
        if (SleepSegmentEvent.hasEvents(intent)) {
            val sleepSegmentEvents: List<SleepSegmentEvent> =
                SleepSegmentEvent.extractEvents(intent)
            Log.d(TAG, "SleepSegmentEvent List: $sleepSegmentEvents")
            // 서버에 수면 기록 전송
            val childApi = ChildApiService.getInstance(context = context)
            scope.launch{
                when(val result = safeApiCall {childApi.sendSleepLog(sleepSegmentEvents)} ){
                    is Result.Success ->{ Log.e(TAG, "SleepLog Send Success") }
                    is Result.Error -> { Log.e(TAG, "SleepLog Send Fail") }
                }
                val file = File(context.filesDir, "sleep_segment_log.txt")
                if(!file.exists()) file.createNewFile()
                for(sleep in sleepSegmentEvents){
                    val startTime = Instant.ofEpochMilli(sleep.startTimeMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    val endTime = Instant.ofEpochMilli(sleep.endTimeMillis).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    file.writeText(text = "status : ${sleep.status} 시작시간 : ${startTime} 끝난 시간 : ${endTime}\n")
                }
            }
        } else if (SleepClassifyEvent.hasEvents(intent)) {
            val sleepClassifyEvents: List<SleepClassifyEvent> =
                SleepClassifyEvent.extractEvents(intent)
            Log.d(TAG, "SleepClassifyEvent List: $sleepClassifyEvents")

            // 서버 api 호출
        }
    }
}