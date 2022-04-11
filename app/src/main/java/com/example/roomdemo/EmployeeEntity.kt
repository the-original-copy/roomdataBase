package com.example.roomdemo

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "employee-table")
data class EmployeeEntity(
    //Defining the columns of the table
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name : String = " ",
    //Giving a column an internal name
    @ColumnInfo(name = "email-id")
    val email : String = " "
)
