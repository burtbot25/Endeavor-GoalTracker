package com.comp3617.finalproject.Reminder

import android.content.Context
import android.content.SharedPreferences

class LocalData(context: Context) {

    private val appSharedPrefs: SharedPreferences
    private val prefsEditor: SharedPreferences.Editor
    private val APP_SHARED_PREFS = "RemindMePref"

    private val reminderStatus = "reminderStatus"
    private val hour = "hour"
    private val min = "min"

    init {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE)
        this.prefsEditor = appSharedPrefs.edit()
    }

    // Settings Page Reminder Time (Hour)

    fun getHour() : Int{
        return appSharedPrefs.getInt(hour, 20)
    }

    fun setHour(h : Int){
        prefsEditor.putInt(hour, h)
        prefsEditor.commit()
    }


    // Settings Page Reminder Time (Minutes)

    fun getMin() : Int {
        return appSharedPrefs.getInt(min, 0)
    }

    fun setMin(m: Int){
        prefsEditor.putInt(min, m)
        prefsEditor.commit()
    }


    // Settings Page Set Reminder

    fun getReminderStatus(): Boolean {
        return appSharedPrefs.getBoolean(reminderStatus, false)
    }

    fun setReminderStatus(status: Boolean) {
        prefsEditor.putBoolean(reminderStatus, status)
        prefsEditor.commit()
    }

}
