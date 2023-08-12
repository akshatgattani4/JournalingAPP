package com.example.app_28_journal

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.app_28_journal.databinding.JournalCardBinding

class JournalRecyclerAdapter(val context: Context, val journalList : List<Journal>): RecyclerView.Adapter<JournalRecyclerAdapter.MyViewHolder>() {
    lateinit var binding : JournalCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = JournalCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return journalList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val journal = journalList[position]
        holder.bind(journal)

    }

    class MyViewHolder(var binding: JournalCardBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(journal: Journal) {
            binding.journal = journal
        }

    }


}

