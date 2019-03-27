package com.comp3617.finalproject.Reminder

import android.annotation.TargetApi
import android.app.TimePickerDialog
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SwitchCompat
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.comp3617.finalproject.R
import kotlinx.android.synthetic.main.activity_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class ReminderActivity : AppCompatActivity() {


    lateinit var localData: LocalData

    lateinit var reminderSwitch: SwitchCompat
    lateinit var tvTime: TextView
    lateinit var ll_set_time: LinearLayout

    var hour: Int = 0
    var min: Int = 0

    lateinit var myClipboard: ClipboardManager

    lateinit var scheduler : NotificationScheduler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        scheduler = NotificationScheduler()

        localData = LocalData(applicationContext)

        myClipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        ll_set_time = findViewById(R.id.ll_set_time)

        tvTime = findViewById(R.id.tv_reminder_time_desc)

        reminderSwitch = findViewById(R.id.timerSwitch)


        hour = localData.getHour()
        min = localData.getMin()

        tvTime.text = getFormatedTime(hour, min)
        reminderSwitch.isChecked = localData.getReminderStatus()

        if (!localData.getReminderStatus())
            ll_set_time.alpha = 0.4f

        reminderSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            localData.setReminderStatus(isChecked)
            if (isChecked) {
                scheduler.setReminder(
                    this@ReminderActivity,
                    AlarmReceiver::class.java,
                    localData.getHour(),
                    localData.getMin()
                )
                ll_set_time.alpha = 1f
            } else {
                scheduler.cancelReminder(this@ReminderActivity, AlarmReceiver::class.java)
                ll_set_time.alpha = 0.4f
            }
        }

        set_btn.setOnClickListener {
            if (localData.getReminderStatus())
                showTimePickerDialog(localData.getHour(), localData.getMin())

        }

        close_btn_reminder.setOnClickListener {
            finish()
        }

    }


    private fun showTimePickerDialog(h: Int, m: Int) {

        val builder = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { timePicker, hour, min ->
                println( "Time Set: hour $hour")
                println( "Time Set: min $min")
                localData.setHour(hour)
                localData.setMin(min)
                tvTime.text = getFormatedTime(hour, min)
                Toast.makeText(this, "Reminder Set : " + tvTime.text, Toast.LENGTH_LONG).show()
                scheduler.setReminder(
                    this@ReminderActivity,
                    AlarmReceiver::class.java,
                    localData.getHour(),
                    localData.getMin()
                )
            }, h, m, false
        )

        builder.show()

    }

    fun getFormatedTime(h: Int, m: Int): String {
        val OLD_FORMAT = "HH:mm"
        val NEW_FORMAT = "hh:mm a"

        val oldDateString = h.toString() + ":" + m
        var newDateString = ""

        try {
            val sdf = SimpleDateFormat(OLD_FORMAT, getCurrentLocale())
            val d = sdf.parse(oldDateString)
            sdf.applyPattern(NEW_FORMAT)
            newDateString = sdf.format(d)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return newDateString
    }

    @TargetApi(Build.VERSION_CODES.N)
    fun getCurrentLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales.get(0)
        } else {

            resources.configuration.locale
        }
    }
}
