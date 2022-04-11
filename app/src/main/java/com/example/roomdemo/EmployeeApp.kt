package com.example.roomdemo

import android.app.Application

class EmployeeApp:Application() {
    //by lazy means that the loads the loaded variable only when its needed ie. val db
    val db by lazy{
        //Create the database first before creating the package
        EmployeeDataBase.getInstance(this)
    }
}