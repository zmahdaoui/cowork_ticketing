package com.example.cowork.activities.adapters

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.cowork.R
import com.example.cowork.activities.TicketDetailActivity
import com.example.cowork.activities.models.Tickets

class TicketAdapter(var tickets :List<Tickets>, val itemClickListener: View.OnClickListener):  RecyclerView.Adapter<TicketAdapter.ViewHolder>(){

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val cardView = itemView.findViewById<CardView>(R.id.ticket_cardview)
        val nameTickets = itemView.findViewById<TextView>(R.id.ticket_name)
        val typeTickets = itemView.findViewById<TextView>(R.id.ticket_type)
        val location = itemView.findViewById<TextView>(R.id.ticket_location)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val viewItem= inflater.inflate(R.layout.item_ticket, parent, false)
        return ViewHolder(viewItem)
    }

    override fun getItemCount(): Int {
        return tickets!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = tickets!!.get(position)
        holder.nameTickets.text = ticket.name
        holder.typeTickets.text = ticket.type
        holder.location.text = ticket.location
        holder.cardView.setOnClickListener {
            val intent = Intent(holder.cardView.context, TicketDetailActivity::class.java)
            var bundle = Bundle()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            bundle.putParcelable("ticket", ticket)
            intent.putExtra("bundle", bundle)
            holder.cardView.context.startActivity(intent)
        }
    }

}