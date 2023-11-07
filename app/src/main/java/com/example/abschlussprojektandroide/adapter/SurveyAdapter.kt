package com.example.abschlussprojektandroide.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.databinding.ListItemSurveyBinding

class SurveyAdapter (
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
        holder.binding.tvPublishedUsernameInput.text = surveyItem.publishedBy

        //
        holder.binding.tvVoteCounter.text = surveyItem.totalVotes.toString()
        holder.binding.tvVoteCountFalse.text = surveyItem.votesFalse.toString()
        holder.binding.tvVoteCountTrue.text = surveyItem.votesTrue.toString()
        holder.binding.tvVoteCountNeutral.text = surveyItem.votesNeutral.toString()

        //wenn Live Data eingefügt wurde, dann müssen die
        //holder.binding.tvPublishedUsernameInput.setText(surveyItem.userId)
        //holder.binding.tvVoteCounter.setText(surveyItem.totalVotes)
        //holder.binding.tvVoteCountFalse.setText(surveyItem.votesFalse)
        //holder.binding.tvVoteCountTrue.setText(surveyItem.votesTrue)
        //holder.binding.tvVoteCountNeutral.setText(surveyItem.votesNeutral)

    }


    fun updateData(newData: List<SurveyItem>){
        this.dataset = newData
        notifyDataSetChanged()
    }

}