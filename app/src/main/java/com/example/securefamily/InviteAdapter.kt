package com.example.securefamily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InviteAdapter(private val listContacts: List<ContactModel>) : RecyclerView.Adapter<InviteAdapter.ViewHolder>(){
    class ViewHolder(private val item: View) : RecyclerView.ViewHolder(item) {
        val name = item.findViewById<TextView>(R.id.name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val item = inflater.inflate(R.layout.item_invite,parent,false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int {
        return listContacts.size
    }

    override fun onBindViewHolder(holder: InviteAdapter.ViewHolder, position: Int) {
        val item = listContacts[position]
        holder.name.text = item.name
    }


}
