package com.comp3617.finalproject.Reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.comp3617.finalproject.SurveyActivity

class AlarmReceiver : BroadcastReceiver() {

    lateinit var scheduler : NotificationScheduler

    // action on Receive
    override fun onReceive(context: Context?, intent: Intent) {
        scheduler = NotificationScheduler()

        // Retrieve alarm hour & minute and set to notificationScheduler
        if (intent.action != null && context != null) {
            if (intent.action!!.equals(Intent.ACTION_BOOT_COMPLETED, ignoreCase = true)) {
                // Set the alarm here.

                val localData = LocalData(context)
                scheduler = NotificationScheduler()
                scheduler.setReminder(
                    context,
                    AlarmReceiver::class.java,
                    localData.getHour(),
                    localData.getMin()
                )
                return
            }
        }


        //Trigger the notification
        scheduler.showNotification(
            context!!, SurveyActivity::class.java,
            "Time to evaluate your day", "Tap for daily survey"
        )

    }
}