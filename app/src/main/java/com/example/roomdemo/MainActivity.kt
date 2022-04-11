package com.example.roomdemo

import android.app.AlertDialog
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdemo.databinding.ActivityMainBinding
import com.example.roomdemo.databinding.UpdateDialogBinding
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    var binding : ActivityMainBinding? = null
    @OptIn(InternalCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val employeeDao = (application as EmployeeApp).db.employeeDao()
        binding?.btnAdd?.setOnClickListener{
            addRecord(employeeDao)
        }
        //Loading data hence must run the background
        lifecycleScope.launch{
            //.collect extension returns a list of type EmployeeEntity
            employeeDao.fetchAllEmployees().collect{
                val list = ArrayList(it)
                setUpListRecyclerView(list,employeeDao)
            }
       }
    }

    fun addRecord(employeeDao: EmployeeDao){
        val name = binding?.etName?.text.toString()
        val email = binding?.etEmailId?.text.toString()

        if(email.isNotEmpty() && name.isNotEmpty()){
            //Done in the background using coroutines
            lifecycleScope.launch{
                employeeDao.insert(EmployeeEntity(name = name , email = email))
                Toast.makeText(this@MainActivity,"Record has been successfully saved",Toast.LENGTH_SHORT).show()
                //Clear text after inserting the data to give room to type in extra data
                binding?.etName?.text?.clear()
                binding?.etEmailId?.text?.clear()
            }
        }else{
            Toast.makeText(this@MainActivity,"Name and email can not be blank",Toast.LENGTH_LONG).show()
        }
    }

    private fun setUpListRecyclerView(employeeList:ArrayList<EmployeeEntity>,employeeDao:EmployeeDao){
        if(employeeList.isNotEmpty()){
            val itemAdapter = ItemAdapter(employeeList,
                {
                    updateId -> updateRecordDialog(updateId,employeeDao)
                },
                {
                    deleteId -> deleteRecordAlertDialog(deleteId,employeeDao)
                })
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = itemAdapter
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
            binding?.rvItemsList?.visibility = View.VISIBLE
        }else{
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
            binding?.rvItemsList?.visibility = View.GONE
        }
    }

    private fun updateRecordDialog(id:Int,employeeDao: EmployeeDao){
        //Setting up UI dialog
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        val binding = UpdateDialogBinding.inflate(layoutInflater)
        updateDialog.setContentView(binding.root)

        //Data operations must always occur in the background
        lifecycleScope.launch{
            employeeDao.fetchEmployeeById(id).collect{
                //Populate the text views of the editTexts
                binding.etUpdateName.setText(it.name)
                binding.etUpdateEmailId.setText(it.email)
            }
        }
        binding.tvUpdate.setOnClickListener{
            val name = binding.etUpdateName.text.toString()
            val email = binding.etUpdateEmailId.text.toString()

            if(name.isNotEmpty() && email.isNotEmpty()){
                lifecycleScope.launch{
                    employeeDao.update(EmployeeEntity(id,name,email))
                    Toast.makeText(this@MainActivity,"Record has been successfully updated",Toast.LENGTH_SHORT).show()
                    updateDialog.dismiss()
                }
            }else{
                Toast.makeText(this@MainActivity,"Name or Email can not be blank",Toast.LENGTH_SHORT).show()
            }
        }
        binding.tvCancel.setOnClickListener{
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    private fun deleteRecordAlertDialog(id:Int,employeeDao:EmployeeDao){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Record")
        builder.setIcon(R.drawable.ic_baseline_announcement_24)
        lifecycleScope.launch{
            employeeDao.fetchEmployeeById(id).collect{
                if(it != null){
                    builder.setMessage("Are you sure you want to delete user ${it.name}")
                }else{
                    builder.setMessage("This employee does not exist")
                }
            }
        }
        builder.setPositiveButton("Yes"){dialogInterface, _ ->
            lifecycleScope.launch{
                employeeDao.delete(EmployeeEntity(id))
                Toast.makeText(this@MainActivity,"Record has been successfully deleted",Toast.LENGTH_SHORT).show()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface,_ ->
            dialogInterface.dismiss()
        }
        //Set up the alertDialog
        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}

