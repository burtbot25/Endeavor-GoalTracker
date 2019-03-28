package com.comp3617.finalproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.activity_bar_chart.*

class BarChartActivity : AppCompatActivity() {

    lateinit var title : String
    var sun = 0
    var mon = 0
    var tue = 0
    var wed = 0
    var thu = 0
    var fri = 0
    var sat = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)

        // Get values from Dashboard Activity
        title = intent.getStringExtra("title")
        sun = intent.getIntExtra("sun", 0)
        mon = intent.getIntExtra("mon", 0)
        tue = intent.getIntExtra("tue", 0)
        wed = intent.getIntExtra("wed", 0)
        thu = intent.getIntExtra("thu", 0)
        fri = intent.getIntExtra("fri", 0)
        sat = intent.getIntExtra("sat", 0)

        // Create array of values per day
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(sun.toFloat(), 0))
        entries.add(BarEntry(mon.toFloat(), 1))
        entries.add(BarEntry(tue.toFloat(), 2))
        entries.add(BarEntry(wed.toFloat(), 3))
        entries.add(BarEntry(thu.toFloat(), 4))
        entries.add(BarEntry(fri.toFloat(), 5))
        entries.add(BarEntry(sat.toFloat(), 6))

        val bardataset = BarDataSet(entries, "Cells")

        // Assign labels to bar chart
        val labels = ArrayList<String>()
        labels.add("Sun")
        labels.add("Mon")
        labels.add("Tue")
        labels.add("Wed")
        labels.add("Thu")
        labels.add("Fri")
        labels.add("Sat")

        // set the data and list of lables into chart
        val data = BarData(labels, bardataset)
        barChart.data = data

        barChart.setDescription("$title")  // set the description
        barChart.setDescriptionTextSize(100f)

        bardataset.setColors(ColorTemplate.COLORFUL_COLORS)
        bardataset.valueTextSize = 16f

        // Shows animation
        barChart.animateY(1000)
    }
}
