package com.comp3617.finalproject.Reminder

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.support.v4.app.NotificationCompat
import com.comp3617.finalproject.R
import java.util.*

class NotificationScheduler {

    fun setReminder(context: Context, cls: Class<*>, hour: Int, min: Int) {
        val calendar = Calendar.getInstance()

        val setcalendar = Calendar.getInstance()
        setcalendar.set(Calendar.HOUR_OF_DAY, hour)
        setcalendar.set(Calendar.MINUTE, min)
        setcalendar.set(Calendar.SECOND, 0)

        // cancel already scheduled reminders
        cancelReminder(context, cls)

        if (setcalendar.before(calendar)) {
            setcalendar.add(Calendar.DATE, 1)
        }
        // Enable broadcast receiver
        val receiver = ComponentName(context, cls)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        // Create pending intent for notification
        val intent1 = Intent(context, cls)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT)

        // Create alarm manager and set attributes for it
        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            setcalendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

    }

    // cancel reminder
    fun cancelReminder(context: Context, cls: Class<*>) {
        // Disable a receiver

        val receiver = ComponentName(context, cls)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
            PackageManager.DONT_KILL_APP
        )

        val intent1 = Intent(context, cls)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = context.getSystemService(ALARM_SERVICE) as AlarmManager
        am.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    // Show push notification
    fun showNotification(context: Context, cls: Class<*>, title: String, content: String) {
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationIntent = Intent(context, cls)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP

        // Build intent stack
        val stackBuilder = TaskStackBuilder.create(context)
        stackBuilder.addParentStack(cls)
        stackBuilder.addNextIntent(notificationIntent)

        val pendingIntent =
            stackBuilder.getPendingIntent(100, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(context)

        // create notification
        val notification = builder.setContentTitle(title)
            .setContentText(content)
            .setAutoCancel(true)
            .setSound(alarmSound)
            .setSmallIcon(R.drawable.logo_check3)
            .setContentIntent(pendingIntent).build()

        println("Creating notification manager")
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        println("notifying...")
        // Initiate the push notification
        notificationManager.notify(100, notification)

        println("END")
    }

}