package com.example.xetaanalytics.Activity.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.xetaanalytics.R

class FactsRecyclerAdapter(val context : Context, private val itemList:ArrayList<String>) : RecyclerView.Adapter<FactsRecyclerAdapter.FactsViewHolder>(){
    class FactsViewHolder(view: View):RecyclerView.ViewHolder(view){
        val textView1 :TextView = view.findViewById(R.id.textView1)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FactsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_generic_fact,parent,false)
        return FactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: FactsViewHolder, position: Int) {
        val text = itemList[position]
        holder.textView1.text = text
    }

    override fun getItemCount(): Int {
       return itemList.size
    }
}