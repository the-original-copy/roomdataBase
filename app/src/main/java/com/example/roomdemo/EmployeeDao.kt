package com.example.roomdemo

import androidx.room.*
import kotlinx.coroutines.flow.Flow

//Define functions that will be used to query the database
@Dao
interface EmployeeDao {
    @Insert
    //This is a heavy process and therefore must be done in the background
    suspend fun insert(employeeEntity : EmployeeEntity)

    @Update
    suspend fun update(employeeEntity : EmployeeEntity)

    @Delete
    suspend fun delete(employeeEntity : EmployeeEntity)

    @Query("SELECT * from `employee-table`")
    fun fetchAllEmployees():Flow<List<EmployeeEntity>>

    @Query("SELECT * from `employee-table` where id=:id")
    fun fetchEmployeeById(id : Int):Flow<EmployeeEntity>
}