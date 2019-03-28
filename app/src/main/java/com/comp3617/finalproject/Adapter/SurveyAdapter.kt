package com.comp3617.finalproject.Adapter

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.comp3617.finalproject.Model.Goal
import com.comp3617.finalproject.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.math.roundToInt

class SurveyAdapter(private val ctx: Context, private val listGoal : List<Goal>?) : ArrayAdapter<Goal>(ctx, 0, listGoal) {
    lateinit var goal : Goal
    lateinit var row_layout : LinearLayout
    lateinit var row_title : TextView
    lateinit var progress_bar: ProgressBar
    lateinit var dbRef : DatabaseReference
    private var progress : Double = 0.0
    private var progressInt : Int = 0
    private lateinit var calendar: Calendar
    private var sun = 0
    private var mon = 0
    private var tue = 0
    private var wed = 0
    private var thu = 0
    private var fri = 0
    private var sat = 0


    // Gets the view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var rowView : View? = null
        calendar = Calendar.getInstance()

        // Inflate view
        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.survey_row_layout, parent, false)
        } else {
            rowView = convertView
        }

        // get goal
        goal = listGoal!![position]

        // set day value
        sun = goal.sun
        mon = goal.mon
        tue = goal.tue
        wed = goal.wed
        thu = goal.thu
        fri = goal.fri
        sat = goal.sat


        // Retrieve Goal
        var currentGoal = listGoal!![position]
        var goalId = listGoal!![position].uid

        dbRef = FirebaseDatabase.getInstance().getReference("goals")

        // Retrieve View IDs
        row_layout = rowView!!.findViewById<TextView>(R.id.row_container2) as LinearLayout
        row_title = rowView.findViewById(R.id.row_title2)
        progress_bar = rowView.findViewById(R.id.progressBar2)


        // Progress
        progress = (goal.progressCurrent.toDouble() / goal.progressGoal.toDouble()) * 100
        progressInt = progress.roundToInt()
        print("PROGRESSINT = $progressInt")


        // View holder and listener
        var viewHolder = ViewHolder()
        viewHolder.button = rowView.findViewById(R.id.survey_yes)
        viewHolder.button.setOnClickListener {
            // use currentGoal
            println("currentGoal=$currentGoal")
            println("currentGoal progress = ${currentGoal.progressCurrent}")

            var day = calendar.get(Calendar.DAY_OF_WEEK)
            Log.d("tag", "ADD TO DAY CALLED")
            // Add to day value depending on the day of the week the button is pressed
            when (day){
                Calendar.SUNDAY ->     currentGoal.sun += 1
                Calendar.MONDAY ->     currentGoal.mon += 1
                Calendar.TUESDAY ->    currentGoal.tue += 1
                Calendar.WEDNESDAY ->  currentGoal.wed += 1
                Calendar.THURSDAY ->   currentGoal.thu += 1
                Calendar.FRIDAY ->     currentGoal.fri += 1
                Calendar.SATURDAY ->   currentGoal.sat += 1
            }

            // increment progressCurrent
            currentGoal.progressCurrent += 1

            Log.d("1","DAY OF THE WEEK")
            Log.d("2", "mon = $mon")
            Log.d("2", "tue = $tue")
            Log.d("2", "wed = $wed")
            Log.d("2", "thu = $thu")
            Log.d("2", "fri = $fri")
            Log.d("2", "sat = $sat")
            Log.d("2", "sun = $sun")

            println("currentGoal progress updated = ${currentGoal.progressCurrent}")
            dbRef.child(goalId).setValue(currentGoal).addOnCompleteListener{
                Toast.makeText(context, " Updated: " + currentGoal.title, Toast.LENGTH_LONG).show()
            }


        }

        // Sets View values
        setViewValues()

        return rowView
    }

    // Sets the values of the views
    private fun setViewValues(){
        row_title.text = goal.title

        setProgress()
    }

    // sets progress bar fill and color
    private fun setProgress(){
        progress_bar.progress = progressInt

        // Using 98 to represent 100 since 1/7 days is 0.14xx and 14*7 would be 98
        if (progress_bar.progress >= 98){
            progress_bar.progress = 100
            // change color
            progress_bar.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
        } else {
            progress_bar.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
        }
    }

    public class ViewHolder(){
        lateinit var button : Button
    }

}