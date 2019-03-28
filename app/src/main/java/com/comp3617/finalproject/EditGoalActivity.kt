package com.comp3617.finalproject

import android.app.DatePickerDialog
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.comp3617.finalproject.Fragments.DatePickerFragment
import com.comp3617.finalproject.Fragments.DeleteFragment
import java.text.DateFormat
import java.util.*
import com.comp3617.finalproject.Model.Goal
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_edit_goal.*
import kotlin.math.roundToInt


class EditGoalActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    DeleteFragment.DeleteFragmentListener {

    // NEED INSTANCE OF DATABASE
    lateinit var uid: String
    lateinit var title: String
    lateinit var endDate: String
    var progressCurrent: Int = 0
    var progressGoal: Int = 0
    lateinit var goal: Goal
    private lateinit var calendar: Calendar
    private lateinit var chosenDate: Date
    private lateinit var itemReference: DatabaseReference
    var formattedDate: String = ""
    var incrementer : Double = 0.0
    var incrementerInt : Int = 0
    var goalType : String = ""
    var sun = 0
    var mon = 0
    var tue = 0
    var wed = 0
    var thu = 0
    var fri = 0
    var sat = 0

    lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_goal)

        dbRef = FirebaseDatabase.getInstance().getReference("goals")
        calendar = Calendar.getInstance()


        // Get data from the Main Activity
        uid = intent.getStringExtra("uid")
        title = intent.getStringExtra("title")
        endDate = intent.getStringExtra("endDate")
        progressCurrent = intent.getIntExtra("progressCurrent", 0)
        progressGoal = intent.getIntExtra("progressGoal", 0)
        goalType = intent.getStringExtra("goalType")
        sun = intent.getIntExtra("sun", 0)
        mon = intent.getIntExtra("mon", 0)
        tue = intent.getIntExtra("tue", 0)
        wed = intent.getIntExtra("wed", 0)
        thu = intent.getIntExtra("thu", 0)
        fri = intent.getIntExtra("fri", 0)
        sat = intent.getIntExtra("sat", 0)

        // Set goal from Main Activity data
        goal = Goal(uid, title, endDate, progressCurrent, progressGoal, goalType, sun, mon, tue, wed, thu, fri, sat)
        itemReference = dbRef.child(uid)
        println("goal: " + goal.toString())
        incrementer = (1.0 / progressGoal) * 100
        incrementerInt = incrementer.roundToInt()
        setViewValues()

        // Button to Update Goal
        btn_save.setOnClickListener {
            goal.title = edit_goal_title.text.toString()
            goal.endDate = edit_date.text.toString()
            goal.progressCurrent = progressCurrent
            goal.progressGoal = edit_quantity.text.toString().toInt()
            goal.goalType = goalType
            goal.sun = sun
            goal.mon = mon
            goal.tue = tue
            goal.wed = wed
            goal.thu = thu
            goal.fri = fri
            goal.sat = sat

            itemReference.setValue(goal).addOnCompleteListener {
                Toast.makeText(this, "Goal Saved Successfully", Toast.LENGTH_LONG).show()
            }
            print(goal)
            finish()
        }

        //Button to Delete Goal
        btn_delete.setOnClickListener {
            showDeleteDialog()
        }

        // Adds to Progress Circle
        progress_add.setOnClickListener {

            if (progressCurrent >= 0) {
                progress_circle.progress += incrementerInt
            }
            // set progressCurrent to the progress
            progressCurrent += 1 // Increase by one day

            // Increment day counter of the Goal
            addToDay()

            setProgressColor()
            println("ITEM REFERNCE IS : " + itemReference.toString())
            println("Current goal is : " + goal.toString())
            edit_progress_counter.text = progressCurrent.toString()
            // Note to self: add if statements for digits that are single, double, triple digit etc...
            // Depending on how many digits, you need to multiply the 1/progressGoal by the number of digits
        }

        // Subtract from Progress Circle
        progress_subtract.setOnClickListener {
            // decrement by a number
            if ((progressCurrent.toDouble() / progressGoal) <= 1.0)
                progress_circle.progress -= incrementerInt
            // set progressCurrent to the progress
            progressCurrent -= 1

            // Decrement day counter of the Goal
            subtractFromDay()

            setProgressColor()
            edit_progress_counter.text = progressCurrent.toString()
        }

        // Close edit screen without save
        edit_btn_cancel.setOnClickListener {
            finish()
        }
    }

    // increment day depending on the current day
    fun addToDay(){
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY -> sun += 1
            Calendar.MONDAY -> mon += 1
            Calendar.TUESDAY -> tue += 1
            Calendar.WEDNESDAY -> wed += 1
            Calendar.THURSDAY -> thu += 1
            Calendar.FRIDAY -> fri += 1
            Calendar.SATURDAY -> sat += 1
        }
    }

    // decrement day depending on the current day
    fun subtractFromDay(){
        var day = calendar.get(Calendar.DAY_OF_WEEK)
        when (day){
            Calendar.SUNDAY -> sun -= 1
            Calendar.MONDAY -> mon -= 1
            Calendar.TUESDAY -> tue -= 1
            Calendar.WEDNESDAY -> wed -= 1
            Calendar.THURSDAY -> thu -= 1
            Calendar.FRIDAY -> fri -= 1
            Calendar.SATURDAY -> sat -= 1
        }
    }

    // Deletes goal when confirmed
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        itemReference.removeValue()
        println(itemReference)
        println("deleted: " + title)
        finish()
    }

    // Show delete dialog
    private fun showDeleteDialog() {
        val dialog = DeleteFragment()
        dialog.show(supportFragmentManager, "TAG1")
    }

    // Sets view fields with values from the goal selected
    private fun setViewValues() {
        edit_goal_title.setText(title)
        edit_quantity.setText(progressGoal.toString())
        edit_date.text = endDate
        var progress = (progressCurrent.toDouble() / progressGoal) * 100
        var progressInt = progress.roundToInt()
        progress_circle.progress = progressInt
        setProgressColor()
        edit_progress_counter.text = progressCurrent.toString()
        edit_progress_goal.text = progressGoal.toString()
    }

    // sets progress color
    private fun setProgressColor() {
        // we choose 98 instead of 100 because daily 1/7 = 14.xx%
        // 14 * 7 = 98
        if (progress_circle.progress >= 98) {
            progress_circle.progress = 100
            progress_circle.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
        } else if (progress_circle.progress <= 3){
            progress_circle.progress = 0
        } else {
            progress_circle.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }


    // Show date picker
    fun showDatePickerDialog(v: View) {
        val dpFragment = DatePickerFragment()
        dpFragment.show(supportFragmentManager, "TAG2")
    }

    // Handler when date is selected. Sets Date
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        formattedDate = formatDate(year, month, day)
        edit_date.text = formattedDate
    }

    // Formats date
    private fun formatDate(year: Int, month: Int, day: Int): String {
        calendar.set(year, month, day, 0, 0, 0)
        chosenDate = calendar.time

        val df = DateFormat.getDateInstance(DateFormat.MEDIUM)
        return df.format(chosenDate)
    }

    // Saves the state when the phone is rotated
    override fun onSaveInstanceState(outState: Bundle?) {
        outState!!.putString("date", formattedDate)
        super.onSaveInstanceState(outState)
    }

    // Restores the state upon rotation
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        edit_date.text = savedInstanceState!!.getString("date")
        formattedDate = savedInstanceState!!.getString("date")
    }

}
