package com.comp3617.finalproject


import android.app.DatePickerDialog
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.comp3617.finalproject.Fragments.DatePickerFragment
import com.comp3617.finalproject.Model.Goal
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_add_goal.*
import java.text.DateFormat
import java.util.*

class AddGoalActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private lateinit var calendar: Calendar
    private lateinit var chosenDate: Date
    private var formattedDate : String = ""

    lateinit var dbRef : DatabaseReference
    lateinit var goalList: MutableList<Goal>
    val INITIAL = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_goal)

        goalList = mutableListOf()
        dbRef = FirebaseDatabase.getInstance().getReference("goals")
        calendar = Calendar.getInstance()


        // Listener to add a task to the database
        btn_add.setOnClickListener {
            if (add_quantity.text.toString().toInt() <= 0){
                Toast.makeText(this, "Please Enter a Quantity higher than 0", Toast.LENGTH_LONG).show()
            } else {

                val goalID = dbRef.push().key
                // Create Goal to add to DB
                val goal = Goal(
                    goalID!!, // id
                    add_goal_title.text.toString(), // title
                    add_date.text.toString(), // end date
                    INITIAL, // progress current
                    add_quantity.text.toString().toInt(), // progress goal
                    0,0,0,0,0,0,0 // Initialize days of the week
                )
                dbRef.child(goalID).setValue(goal).addOnCompleteListener{
                    Toast.makeText(this, "Goal Saved Successfully", Toast.LENGTH_LONG).show()
                }
                finish()
            }
        } // end of btn

        add_btn_cancel.setOnClickListener {
            finish()
        }
    }

    // Show Date Picker Dialog
    fun showDatePickerDialog(v: View) {
        val dpFragment = DatePickerFragment()
        dpFragment.show(supportFragmentManager, "TAG1")
    }

    // Listener for when date is picked from calendar
    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        formattedDate = formatDate(year, month, day)
        add_date.text = formattedDate
    }

    // Formats the date
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
        add_date.text = savedInstanceState!!.getString("date")
        formattedDate = savedInstanceState!!.getString("date")
    }
}