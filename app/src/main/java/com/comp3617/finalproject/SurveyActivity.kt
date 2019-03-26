package com.comp3617.finalproject

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.comp3617.finalproject.Adapter.SurveyAdapter
import com.comp3617.finalproject.Model.Goal
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_survey.*

class SurveyActivity : AppCompatActivity() {

    lateinit var dbRef : DatabaseReference
    lateinit var goalList: MutableList<Goal>
    lateinit var goal : Goal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

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

        close_btn.setOnClickListener {
            finish()
        }


//        Unused at the moment.
//        survey_list_view.setOnItemClickListener(object : AdapterView.OnItemClickListener {
//            override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val goal = survey_list_view.getItemAtPosition(position) as Goal
//            }
//        })

    }

    private fun refreshList(){
        val adapter = SurveyAdapter(applicationContext, goalList)
        survey_list_view.adapter = adapter
    }

    // Refreshes list on resume
    override fun onResume() {
        super.onResume()
        println("Resumed")
        refreshList()

    }

}
