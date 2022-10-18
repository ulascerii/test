package com.aydemir.formula1app.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.aydemir.formula1app.R
import com.aydemir.formula1app.view.main.MainActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


private const val CHANNEL_NAME = "ActonChannel"

class FirebaseMessagingService :
    FirebaseMessagingService() {
   
    private val notificationManager: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager }
    
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.notification?.let {
            val title = it.title
            val body = it.body
            val deepLink = remoteMessage.data["deeplink"]
            showNotification(title, body, deepLink)
            
        }
    }
    
    //--------------------------------------------------------------------------------------------//
    private fun showNotification(title: String?, body: String?, deepLink: String?) {
        createNotificationChannel()
        
        val intent = Intent(this, MainActivity::class.java)
        deepLink?.let { intent.putExtra("deeplink", it) }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            this, getRandomId(), intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_NAME)
            .setSmallIcon(R.drawable.f1logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(body)
            )
            .setContentIntent(pendingIntent)
        
        notificationManager.notify(getRandomId(), notificationBuilder.build())
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_NAME, CHANNEL_NAME, importance)
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    //--------------------------------------------------------------------------------------------//
    private fun getRandomId(): Int {
        return System.currentTimeMillis().toInt()
    }
    
    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        
    }
    
}