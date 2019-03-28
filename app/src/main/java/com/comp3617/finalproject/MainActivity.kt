package com.comp3617.finalproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import com.comp3617.finalproject.Model.Goal
import com.comp3617.finalproject.Adapter.CustomAdapter
import com.comp3617.finalproject.Fragments.DeleteFragment
import com.comp3617.finalproject.Reminder.ReminderActivity
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), DeleteFragment.DeleteFragmentListener {

    lateinit var dbRef : DatabaseReference
    lateinit var goalList: MutableList<Goal>
    lateinit var goal : Goal
    lateinit var itemReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goalList = mutableListOf()
        dbRef = FirebaseDatabase.getInstance().getReference("goals")


        // this listener will read the values from the firebase database
        dbRef.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
            }

            // when data changes, refresh the view with latest data
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if (dataSnapshot.exists()){
                    goalList.clear()
                    for (i in dataSnapshot.children){
                        val goal = i.getValue(Goal::class.java)
                        goalList.add(goal!!)
                    }
                    refreshList()
                }
            }
        }) // End of value event listener




        // Item click brings up the BarChartActivity
        list_view.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                goal = list_view.getItemAtPosition(position) as Goal
                val intent = Intent(this@MainActivity, BarChartActivity::class.java)
                intent.putExtra("title", goal.title)
                intent.putExtra("sun", goal.sun)
                intent.putExtra("mon", goal.mon)
                intent.putExtra("tue", goal.tue)
                intent.putExtra("wed", goal.wed)
                intent.putExtra("thu", goal.thu)
                intent.putExtra("fri", goal.fri)
                intent.putExtra("sat", goal.sat)
                startActivity(intent)

            }
        })

        // Long Click to bring up EditGoalActivity
        list_view.onItemLongClickListener = object : AdapterView.OnItemLongClickListener{
            override fun onItemLongClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
                val intent = Intent(this@MainActivity, EditGoalActivity::class.java)
                val goal = list_view.getItemAtPosition(position) as Goal
                println(goal)
                intent.putExtra("uid", goal.uid)
                intent.putExtra("title", goal.title)
                intent.putExtra("endDate", goal.endDate)
                intent.putExtra("progressCurrent", goal.progressCurrent)
                intent.putExtra("progressGoal", goal.progressGoal)
                intent.putExtra("goalType", goal.goalType)
                intent.putExtra("sun", goal.sun)
                intent.putExtra("mon", goal.mon)
                intent.putExtra("tue", goal.tue)
                intent.putExtra("wed", goal.wed)
                intent.putExtra("thu", goal.thu)
                intent.putExtra("fri", goal.fri)
                intent.putExtra("sat", goal.sun)

                startActivity(intent)
//                showDeleteDialog()
                return true
            }
        }

        // Button to bring up AddGoalActivity
        toolbar_add.setOnClickListener {
            val intent = Intent(this@MainActivity, AddGoalActivity::class.java)
            startActivity(intent)
        }

        // Button to bring up SurveyActivity
        toolbar_survey.setOnClickListener {
            //val intent = Intent(this@MainActivity, SurveyActivity::class.java)
            val intent = Intent(this@MainActivity, SurveyActivity::class.java)
            startActivity(intent)
        }

        // Button to bring up ReminderActivity
        toolbar_reminder.setOnClickListener {
            val intent = Intent(this@MainActivity, ReminderActivity::class.java)
            startActivity(intent)
        }
    }

    // Deletes task on confirmation
    // not used
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        itemReference = dbRef.child(goal.uid)
        itemReference.removeValue()
        println("ITEM REFERENCE IS" + itemReference)
    }

    // Show delete dialog fragment
    // not used
    fun showDeleteDialog(){
        val dialog = DeleteFragment()
        dialog.show(supportFragmentManager, "TAG1")
    }

    // Refreshes Dashboard
    private fun refreshList(){
        val adapter = CustomAdapter(applicationContext, goalList)
        list_view.adapter = adapter
    }

    // Refreshes list on resume
    override fun onResume() {
        super.onResume()
        println("Resumed")
        refreshList()

    }

    // Events for menu items
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Opens Info Activity
        R.id.toolbar_info -> {
            val intent = Intent(this, InfoActivity::class.java)
            startActivity(intent)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    // Inflates menu items in appbar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

}
