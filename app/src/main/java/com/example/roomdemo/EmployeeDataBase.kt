package com.example.roomdemo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [EmployeeEntity::class], version = 1)
abstract class EmployeeDataBase: RoomDatabase() {
    //Connect the dao to the database
    abstract fun employeeDao() : EmployeeDao

    //Help to avoid repeatedly instantiating the database
    //Database is not cached hence changes made by one thread will be visible by others
    companion object {
        @Volatile
        private var INSTANCE : EmployeeDataBase? = null

        //Singleton pattern is used where only one instance of the database is created
        fun getInstance(context: Context) : EmployeeDataBase{
            //Used to ensure that only one thread will access the database at any given time
            synchronized(this){
                var instance = INSTANCE
                //Create a database if it doesn't exist
                if(instance == null){
                    //Wipes and rebuild the database if properties such as columns are added or removed
                    instance = Room.databaseBuilder(context.applicationContext,EmployeeDataBase::class.java,"employee_database").fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}