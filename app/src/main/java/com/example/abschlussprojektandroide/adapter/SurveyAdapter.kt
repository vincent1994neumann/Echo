package com.example.abschlussprojektandroide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojektandroide.data.dataclass.SurveyItem
import com.example.abschlussprojektandroide.databinding.ListItemSurveyBinding

class SurveyAdapter (
    private val context: Context,
    private var dataset : List<SurveyItem>

): RecyclerView.Adapter<SurveyAdapter.SurveyItemViewHolder>(){

    class SurveyItemViewHolder(val binding: ListItemSurveyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyItemViewHolder {
        val binding = ListItemSurveyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SurveyItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: SurveyItemViewHolder, position: Int) {
        val surveyItem = dataset[position]
        holder.binding.tvHeaderCv.text = surveyItem.header
        holder.binding.tvCategorieCv.text = surveyItem.category
        holder.binding.tvSurveyText.text = surveyItem.surveyText
        holder.binding.tvTimestamp.text = surveyItem.timestamp
        holder.binding.tvPublishedUsernameInput.setText(surveyItem.userId)
        holder.binding.tvVoteCounter.setText(surveyItem.totalVotes)
    }

    /*
    fun updateData(newData: List<SurveyItem>){
        this.dataset = newData
        notifyDataSetChanged()
    }
    */
}