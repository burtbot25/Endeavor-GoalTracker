package com.comp3617.finalproject.Adapter

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.comp3617.finalproject.Model.Goal
import com.comp3617.finalproject.R
import kotlin.math.roundToInt

class CustomAdapter(private val ctx: Context, private val listGoal : List<Goal>?) : ArrayAdapter<Goal>(ctx, 0, listGoal) {
    lateinit var goal : Goal
    lateinit var row_layout : LinearLayout
    lateinit var row_title : TextView
    lateinit var row_icon : ImageView
    lateinit var end_date: TextView
    lateinit var progress_current: TextView
    lateinit var progress_goal: TextView
    lateinit var progress_bar: ProgressBar
    lateinit var complete_layout: LinearLayout
    lateinit var counter_layout: LinearLayout
    var progressBarValue = 0

    // Gets the view
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var rowView : View? = null

        // Inflate view
        if (convertView == null) {
            val inflater = ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = inflater.inflate(R.layout.row_layout, parent, false)
        } else {
            rowView = convertView
        }

        // Retrieve View IDs
        row_layout = rowView!!.findViewById<TextView>(R.id.row_container) as LinearLayout
        row_title = rowView.findViewById(R.id.row_title)
        row_icon  = rowView.findViewById(R.id.icon)
        end_date = rowView.findViewById(R.id.end_date)
        progress_current = rowView.findViewById(R.id.progress_current)
        progress_goal = rowView.findViewById(R.id.progress_goal)
        progress_bar = rowView.findViewById(R.id.progressBar)
        complete_layout = rowView.findViewById(R.id.complete_layout)
        counter_layout = rowView.findViewById(R.id.counter_layout)

        // Retrieve Goal
        goal = listGoal!![position]

        // Sets View values
        setViewValues()

        return rowView
    }



    // Sets the values of the views
    private fun setViewValues(){
        row_title.text = goal.title
        setIcon()
        end_date.text = goal.endDate
        progress_current.text = goal.progressCurrent.toString()
        progress_goal.text = goal.progressGoal.toString()

        setProgress()
    }

    // Sets the progress bar fill and color
    private fun setProgress(){

        var progress = (goal.progressCurrent.toDouble() / goal.progressGoal.toDouble()) * 100
        var progressInt = progress.roundToInt()
        print("PROGRESSINT = $progressInt")

        progress_bar.progress = progressInt

        // Using 98 to represent 100 since 1/7 days is 0.14xx and 14*7 would be 98
        if (progress_bar.progress >= 98){
            progress_bar.progress = 100
            // change color
            progress_bar.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
            // change view
            complete_layout.visibility = View.VISIBLE
        } else if (progress_bar.progress <= 3){
            progress_bar.progress = 0
            progress_bar.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
            complete_layout.visibility = View.INVISIBLE
        } else {
            progress_bar.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
            complete_layout.visibility = View.INVISIBLE
        }
    }

    // Sets icon based on goalType
    private fun setIcon(){
        when (goal.goalType){
            "Health" -> row_icon.setImageResource(R.drawable.health96)
            "Diet" -> row_icon.setImageResource(R.drawable.diet96)
            "Exercise" -> row_icon.setImageResource(R.drawable.exercise96)
            "Music" -> row_icon.setImageResource(R.drawable.music96)
            "Art" -> row_icon.setImageResource(R.drawable.art96)
            "Career" -> row_icon.setImageResource(R.drawable.career96)
            "Education" -> row_icon.setImageResource(R.drawable.education96)
            "Other" -> row_icon.setImageResource(R.drawable.other96)
        }
    }



}