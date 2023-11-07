package com.example.abschlussprojektandroide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.databinding.ListItemSurveyBinding

//aktuell kann jede Survey nur einmal abgestimmt werden
//Hier fehlt noch die Logik das jeder User das abstimmen kann


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
        holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()

        holder.binding.tvVoteCountTrue.text = surveyItem.percentageTrue()
        holder.binding.tvVoteCountNeutral.text = surveyItem.percentageNeutral()
        holder.binding.tvVoteCountFalse.text = surveyItem.percentageFalse()



        //_______________________________________________________________________

        //UP & Down Vote der Frage Logik
        holder.binding.btnVoteUp.setOnClickListener{
            surveyItem.questionUpVotes++
            surveyItem.totalUpDownVotes()
            disableVotingUpDownBtn(holder)
            holder.binding.tvVoteCounter.text =surveyItem.totalUpDownVotes()
        }
        holder.binding.btnVoteDown.setOnClickListener{
            surveyItem.questionDownVotes++
            disableVotingUpDownBtn(holder)
            holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()
        }

        //_______________________________________________________________________

        //Abstimmungslogik der Frage


        holder.binding.rbTrue.setOnClickListener{
            if (!surveyItem.hasVoted){
                surveyItem.votesTrue++
                surveyItem.totalVotes++
                surveyItem.hasVoted = true
                disableVotingButtons(holder)
                holder.binding.tvVoteCountTrue.text = surveyItem.percentageTrue()
                holder.binding.tvVoteCountNeutral.text = surveyItem.percentageNeutral()
                holder.binding.tvVoteCountFalse.text = surveyItem.percentageFalse()
            }
        }

        holder.binding.rbDontknow.setOnClickListener{
            if (!surveyItem.hasVoted){
                surveyItem.votesNeutral++
                surveyItem.totalVotes++
                disableVotingButtons(holder)
                holder.binding.tvVoteCountTrue.text = surveyItem.percentageTrue()
                holder.binding.tvVoteCountNeutral.text = surveyItem.percentageNeutral()
                holder.binding.tvVoteCountFalse.text = surveyItem.percentageFalse()
            }
        }

        holder.binding.rbFalse.setOnClickListener {
            if (!surveyItem.hasVoted){
                surveyItem.votesFalse++
                surveyItem.totalVotes++
                disableVotingButtons(holder)
                holder.binding.tvVoteCountTrue.text = surveyItem.percentageTrue()
                holder.binding.tvVoteCountNeutral.text = surveyItem.percentageNeutral()
                holder.binding.tvVoteCountFalse.text = surveyItem.percentageFalse()
            }
        }
    }
    private fun disableVotingButtons(holder: SurveyItemViewHolder) {
        holder.binding.rbTrue.isEnabled = false
        holder.binding.rbDontknow.isEnabled = false
        holder.binding.rbFalse.isEnabled = false
    }
    private fun disableVotingUpDownBtn(holder: SurveyItemViewHolder){
        holder.binding.btnVoteUp.isEnabled = false
        holder.binding.btnVoteDown.isEnabled = false
    }


    fun updateData(newData: List<SurveyItem>){
        this.dataset = newData
        notifyDataSetChanged()
    }



}