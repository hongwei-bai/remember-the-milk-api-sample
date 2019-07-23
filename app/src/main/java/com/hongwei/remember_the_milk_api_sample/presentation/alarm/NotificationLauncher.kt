package com.hongwei.remember_the_milk_api_sample.presentation.alarm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationCompat
import com.hongwei.remember_the_milk_api_sample.R
import com.hongwei.remember_the_milk_api_sample.presentation.main.MainActivity

class NotificationLauncher {
    companion object {

        /*
         * worked! tested on OS 5/6/9/8.1
         */
        fun notify(context: Context, title: String, msg: String) {
            val id = "my_channel_01"
            val name = "I'm channel name"
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
            var notification: Notification? = null
            val pendingIntent = PendingIntent.getActivity(context, 0, MainActivity.intent(context), 0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)

                /*
                 * About pluse LED light
                 *
                 * One word for solution: change this light option need to uninstall app and then install again, because of channel.
                 *
                 * https://stackoverflow.com/questions/53278255/notification-channel-is-it-possible-to-change-lightcolor-after-its-been-set
                 *
                 * One good answer:
                 * You can't change channel color notification, importance etc. programmatically without deleting the channel.
                 *
                 *  Because a user may have changed the light color manually.
                 *
                 *  To achieve this programmatically to get the channel and create a new channel with a new id. delete the old channel. Your changes will not reflect if create a channel with the previous id
                 *
                 *  for reference check WhatsApp application try to change the ringtone from an application and see in a channel at the bottom left x channel is deleted message
                 */
                mChannel.enableLights(true)
                mChannel.lightColor = Color.GREEN
                mChannel.enableVibration(true)
//            mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(true)
                notificationManager?.createNotificationChannel(mChannel)
                notification = Notification.Builder(context)
                        .setChannelId(id)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setAutoCancel(true)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setVibrate(longArrayOf())
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher).build()
            } else {
                val notificationBuilder = NotificationCompat.Builder(context)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true)
                        .setVibrate(longArrayOf())
                        .setContentIntent(pendingIntent)
                        .setChannelId(id)//无效
                notification = notificationBuilder.build()
            }
            notificationManager?.notify(111123, notification)
        }
    }
}
