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

            // Refreshes Dashboard with latest data on data change
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

    // Refreshes Survey Activity with new info
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
