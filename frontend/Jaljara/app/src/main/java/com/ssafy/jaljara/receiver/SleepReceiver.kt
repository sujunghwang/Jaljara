package com.ssafy.jaljara.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.SleepClassifyEvent
import com.google.android.gms.location.SleepSegmentEvent
import com.ssafy.jaljara.network.ChildApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SleepReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "SleepReceiver"
    }

    private val scope: CoroutineScope = MainScope()

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
                childApi.sendSleepLog(sleepSegmentEvents)
            }
        } else if (SleepClassifyEvent.hasEvents(intent)) {
            val sleepClassifyEvents: List<SleepClassifyEvent> =
                SleepClassifyEvent.extractEvents(intent)
            Log.d(TAG, "SleepClassifyEvent List: $sleepClassifyEvents")

            // 서버 api 호출
        }
    }
}