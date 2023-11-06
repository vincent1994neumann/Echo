package com.example.abschlussprojektandroide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojektandroide.data.dataclass.SurveyItem
import com.example.abschlussprojektandroide.databinding.ListItemSurveyBinding

class SurveyAdapter (
    private val context: Context,
    private val dataset : List<SurveyItem>

): RecyclerView.Adapter<SurveyAdapter.ItemViewHolder>(){

    class ItemViewHolder(val binding: ListItemSurveyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemSurveyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val data = dataset[position]
        holder.binding.tvHeaderCv.text = data.header
        holder.binding.tvCategorieCv.text = data.category
        holder.binding.tvSurveyText.text = data.surveyText
        holder.binding.tvTimestamp.text = data.timestamp
        holder.binding.tvPublishedUsernameInput.setText(data.userId)
        holder.binding.tvVoteCounter.setText(data.totalVotes)
    }

}