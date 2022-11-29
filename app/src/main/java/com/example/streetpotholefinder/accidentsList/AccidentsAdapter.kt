package com.example.streetpotholefinder.accidentsList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.streetpotholefinder.R

class AccidentsAdapter (val dataList : MutableList<accidentVO>) : RecyclerView.Adapter<AccidentsAdapter.CustomViewHolder>()
{  private lateinit var context: Context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accidentlist, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {




            }

        }
    }



    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {


        holder.imgpot.setImageBitmap(dataList.get(position).potimg)
        holder.tvGps.text = dataList.get(position).gps
        holder.tvtime.text = dataList.get(position).time





    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgpot = itemView.findViewById<ImageView>(R.id.imgpot)
        val tvGps = itemView.findViewById<TextView>(R.id.tvGps)
        val tvtime = itemView.findViewById<TextView>(R.id.tvTime)






    }

}