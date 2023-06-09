package com.ssafy.jaljara.manager

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.google.android.gms.location.ActivityRecognition
import com.google.android.gms.location.SleepSegmentRequest
import com.ssafy.jaljara.receiver.SleepReceiver

object SleepManager {

    private const val TAG = "SleepManager"

    private fun createSleepReceiverPendingIntent(context: Context): PendingIntent {
        // Prepare intent flags
        val flags = PendingIntent.FLAG_MUTABLE
        // Create the PendingIntent
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, SleepReceiver::class.java),
            flags
        )
    }

    /**
     * Register for sleep updates. Has optional success/failure callbacks.
     */
    @SuppressLint("MissingPermission")
    fun registerForSleepUpdates(
        context: Context,
        onSuccess: (() -> Unit)? = null,
        onFailure: ((exception: Exception) -> Unit)? = null
    ) {
        // Ensure storage is initialised
        // NB. Keep storage init here, as Activity#onCreate does not run for BOOT_COMPLETED event.


        // Register for sleep updates
        ActivityRecognition.getClient(context)
            .requestSleepSegmentUpdates(
                createSleepReceiverPendingIntent(context),
                SleepSegmentRequest.getDefaultSleepSegmentRequest()
            )
            .addOnSuccessListener {
                Log.d(TAG, "Successfully subscribed to sleep data.")
                // onSuccess callback
                onSuccess?.invoke()
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "Exception when subscribing to sleep data: $exception")
                // onFailure callback
                onFailure?.invoke(exception)
            }
    }
}