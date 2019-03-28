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

    // initialize shared preferences
    init {
        this.appSharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE)
        this.prefsEditor = appSharedPrefs.edit()
    }

    // get Hour
    fun getHour() : Int{
        return appSharedPrefs.getInt(hour, 20)
    }

    // set Hour
    fun setHour(h : Int){
        prefsEditor.putInt(hour, h)
        prefsEditor.commit()
    }


    // get Minute
    fun getMin() : Int {
        return appSharedPrefs.getInt(min, 0)
    }

    // set Minute
    fun setMin(m: Int){
        prefsEditor.putInt(min, m)
        prefsEditor.commit()
    }


    // get reminder status
    fun getReminderStatus(): Boolean {
        return appSharedPrefs.getBoolean(reminderStatus, false)
    }

    // set reminder status
    fun setReminderStatus(status: Boolean) {
        prefsEditor.putBoolean(reminderStatus, status)
        prefsEditor.commit()
    }

}
