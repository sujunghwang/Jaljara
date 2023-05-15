package com.ssafy.jaljara.ui.screen.child

import android.annotation.SuppressLint
import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Picture
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ssafy.jaljara.R
import com.ssafy.jaljara.activity.MainActivity
import okhttp3.internal.notify

class ChildAlarm : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        try {
            showNotification(context, "잘 시간이에요!", "이제 자러 갈까요?")
        }
        catch (e:Exception){
            Log.d("Receive Exception","${e.printStackTrace().toString()}")
        }
    }

    fun showNotification(context: Context, title:String, description:String){
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelName = "message_channel"
        val channelId = "message_id"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val clickPendingIntent = PendingIntent.getActivity(context,0,intent, PendingIntent.FLAG_MUTABLE)

        val builder = NotificationCompat.Builder(context, channelId)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.astronoutsleep)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.astronoutsleep))
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setContentIntent(clickPendingIntent)

        manager.notify(1, builder.build())
    }
}
