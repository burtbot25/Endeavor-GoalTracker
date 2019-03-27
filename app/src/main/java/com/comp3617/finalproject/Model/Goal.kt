package com.comp3617.finalproject.Model

class Goal(
    var uid: String,
    var title: String,
    var endDate: String,
    var progressCurrent: Int,
    var progressGoal: Int,
    var goalType : String,
    var sun : Int,
    var mon : Int,
    var tue : Int,
    var wed : Int,
    var thu : Int,
    var fri : Int,
    var sat : Int

)
{
    constructor() : this ("", "", "", 0, 0, "", 0,0,0,0,0,0,0)

    override fun toString(): String {
        return "uid=$uid; title=$title, endDate=$endDate; progressCurrent=$progressCurrent; " +
                "progressGoal=$progressGoal, goalType=$goalType, " +
                "sun=$sun, mon=$mon, tue=$tue, wed=$wed, thu=$thu, fri=$fri, sat=$sat"
    }
}