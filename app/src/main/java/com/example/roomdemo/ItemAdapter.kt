package com.example.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdemo.databinding.ItemRowBinding

class ItemAdapter(private val items:ArrayList<EmployeeEntity>,
                    private val updateListener:(id:Int) -> Unit,
                    private val deleteListener:(id:Int) -> Unit ): RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
    class ViewHolder(binding: ItemRowBinding) : RecyclerView.ViewHolder(binding.root){
        //Accessing the ui components
        val llMain = binding.llMain
        val tvName = binding.tvName
        val tvEmail = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        //Get the data for each item from the arraylist
        val item = items[position]

        //Attach data to the UI components
        holder.tvName.text = item.name
        holder.tvEmail.text = item.email
        //Color background
        if(position % 2 == 0){
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.colorLightGray))
        }else{
            holder.llMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.white))
        }
        //Since the recyclerView doesn't implement onClickListeners,update listeners have to be passed in
        //Will be implemented in the main activity
        holder.ivEdit.setOnClickListener{
            updateListener.invoke(item.id)
        }
        holder.ivDelete.setOnClickListener{
            deleteListener.invoke(item.id)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}