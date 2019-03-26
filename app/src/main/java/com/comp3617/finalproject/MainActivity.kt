package com.comp3617.finalproject

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.DialogFragment
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
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // dataSnapshot has all the values of the ref database node (e.g., of type "heroes")
                // this snapshot will contain all the heroes in Firebase database

                // Need a list to store the heroes (define a mutable list)
                // must check to see if we have anythign in the database first

                if (dataSnapshot.exists()){
                    goalList.clear() // this clear will take out the old values and the adapter adds them back again later
                    for (i in dataSnapshot.children){
                        val goal = i.getValue(Goal::class.java) // getvalue takes a class as a param
                        goalList.add(goal!!)
                    }
                    refreshList()
                }
            }
        }) // End of value event listener




        // Event Handler for the Row View "Clicked" by the User.  Sends user to Edit Activity
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

        // Listener for row item brings up delete dialog on long click
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

        add_goal.setOnClickListener {
            val intent = Intent(this@MainActivity, AddGoalActivity::class.java)
            startActivity(intent)
        }

        survey_btn.setOnClickListener {
            //val intent = Intent(this@MainActivity, SurveyActivity::class.java)
            val intent = Intent(this@MainActivity, SurveyActivity::class.java)
            startActivity(intent)
        }

        alarm.setOnClickListener {
            val intent = Intent(this@MainActivity, ReminderActivity::class.java)
            startActivity(intent)
        }
    }

    // Deletes task on confirmation
    override fun onDialogPositiveClick(dialog: DialogFragment) {
        itemReference = dbRef.child(goal.uid)
        itemReference.removeValue()
        println("ITEM REFERENCE IS" + itemReference)
    }

    // Show delete dialog fragment
    fun showDeleteDialog(){
        val dialog = DeleteFragment()
        dialog.show(supportFragmentManager, "TAG1")
    }

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

}
