package com.example.streetpotholefinder

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.DataListVO
import com.example.accidentVO

class accidentAdapter (val dataList : MutableList<accidentVO>) : RecyclerView.Adapter<accidentAdapter.CustomViewHolder>()
{  private lateinit var context: Context



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): accidentAdapter.CustomViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.accidentlist, parent, false)
        return CustomViewHolder(view).apply {
            itemView.setOnClickListener {




            }

        }
    }



    override fun onBindViewHolder(holder: accidentAdapter.CustomViewHolder, position: Int) {


        holder.imgpot.setImageResource(R.drawable.cute_pepe)
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