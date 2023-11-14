package com.example.abschlussprojektandroide.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.databinding.ListItemSurveyBinding

//aktuell kann jede Survey nur einmal abgestimmt werden
//Hier fehlt noch die Logik das jeder User  abstimmen kann


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


        // View wird aufgebaut!
        holder.binding.tvHeaderCv.text = surveyItem.header
        holder.binding.tvCategorieCv.text = surveyItem.category
        holder.binding.tvSurveyText.text = surveyItem.surveyText
        holder.binding.tvTimestamp.text = surveyItem.getFormattedTime()
        holder.binding.tvPublishedUsernameInput.text = surveyItem.publishedBy
        holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()

        // die Sichtbarkeit der Prozentangaben basierend auf der showPercentages-Eigenschaft - erst nach Abstimmung sichtbar
        val percentageVisibility = if (surveyItem.showPercentage) View.VISIBLE else View.INVISIBLE
        holder.binding.tvVoteCountTrue.visibility = percentageVisibility
        holder.binding.tvVoteCountNeutral.visibility = percentageVisibility
        holder.binding.tvVoteCountFalse.visibility = percentageVisibility



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
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem.showPercentage)
                holder.binding.tvVoteCountTrue.text = surveyItem.percentageTrue()
                holder.binding.tvVoteCountNeutral.text = surveyItem.percentageNeutral()
                holder.binding.tvVoteCountFalse.text = surveyItem.percentageFalse()
            }
        }

        holder.binding.rbDontknow.setOnClickListener{
            if (!surveyItem.hasVoted){
                surveyItem.votesNeutral++
                surveyItem.totalVotes++
                surveyItem.hasVoted = true
                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem.showPercentage)
                holder.binding.tvVoteCountTrue.text = surveyItem.percentageTrue()
                holder.binding.tvVoteCountNeutral.text = surveyItem.percentageNeutral()
                holder.binding.tvVoteCountFalse.text = surveyItem.percentageFalse()
            }
        }

        holder.binding.rbFalse.setOnClickListener {
            if (!surveyItem.hasVoted){
                surveyItem.votesFalse++
                surveyItem.totalVotes++
                surveyItem.hasVoted = true
                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem.showPercentage)
                holder.binding.tvVoteCountTrue.text = surveyItem.percentageTrue()
                holder.binding.tvVoteCountNeutral.text = surveyItem.percentageNeutral()
                holder.binding.tvVoteCountFalse.text = surveyItem.percentageFalse()
            }
        }

        //_______________________________________________________________________
        // SaveBtn - speicherung der Umfrage im Profil

        //To DO Speicherung im Profil
        holder.binding.ivSaveIcon.setOnClickListener {
            // Inflate the custom toast layout
            val toastLayout = LayoutInflater.from(holder.itemView.context).inflate(R.layout.custom_toast, null)
            val toastText = toastLayout.findViewById<TextView>(R.id.toast_text)
            val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)

            if (surveyItem.surveySaved) {
                surveyItem.surveySaved = false
                holder.binding.ivSaveIcon.setImageResource(R.drawable.baseline_bookmark_border_24)
                toastText.text = holder.itemView.context.getString(R.string.survey_removed)
                toastIcon.setImageResource(R.drawable.baseline_bookmark_border_24)
            } else {
                surveyItem.surveySaved = true
                holder.binding.ivSaveIcon.setImageResource(R.drawable.baseline_bookmark_24)
                toastText.text = holder.itemView.context.getString(R.string.survey_saved)
                toastIcon.setImageResource(R.drawable.baseline_bookmark_24)
            }

            val toast = Toast(holder.itemView.context).apply {
                duration = Toast.LENGTH_SHORT
                view = toastLayout
            }
            toast.show()
        }



        //ShareBtn
        holder.binding.ivShareIcon.setOnClickListener {
            // Text, der geteilt werden soll, erstellen
            val shareText = "Schauen Sie sich diese Umfrage an: \n" +
                    "Frage: ${surveyItem.surveyText} \n" +
                    "Abgestimmt: ${surveyItem.totalVotes} Stimmen \n" +
                    "Ergebnisse: Wahr - ${surveyItem.votesTrue}, Neutral - ${surveyItem.votesNeutral}, Falsch - ${surveyItem.votesFalse} \n" +
                    "Was ist deine Meinung dazu?"

            // Intent für das Teilen erstellen
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Umfrageergebnis")
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            /* *Erklärung des Intents*
            Ein Intent ist eine Beschreibung einer Operation, die ausgeführt werden soll.
            Hier wird ein Intent mit der Aktion ACTION_SEND erstellt, was Android mitteilt,
            dass Daten an eine andere App gesendet (geteilt) werden sollen. Der Intent wird als Typ text/plain festgelegt,
            was bedeutet, dass die zu teilenden Daten reiner Text sind. Mit putExtra werden zusätzliche Informationen übermittelt:
            Der Betreff (EXTRA_SUBJECT) für die Nachricht und der eigentliche Text (EXTRA_TEXT), der geteilt werden soll.



            Diese Zeile startet einen Dialog, der dem Benutzer erlaubt, eine App auszuwählen, über die er den Text teilen möchte.
            Intent.createChooser erzeugt einen Intent, der dem Benutzer eine Auswahl von Apps präsentiert, die den text/plain-Inhalt verarbeiten können.
            startActivity wird mit dem Context des aktuellen ViewHolders aufgerufen.
            Das bedeutet, es wird der Prozess in Gang gesetzt, der den Teilen-Dialog auf dem Bildschirm anzeigt.
             */

            // Teilen-Dialog starten
            holder.itemView.context.startActivity(Intent.createChooser(shareIntent, "Teilen über..."))
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

    private fun updatePercentageVisibility(holder: SurveyItemViewHolder, visible: Boolean) {
        val visibility = if (visible) View.VISIBLE else View.INVISIBLE
        holder.binding.tvVoteCountTrue.visibility = visibility
        holder.binding.tvVoteCountNeutral.visibility = visibility
        holder.binding.tvVoteCountFalse.visibility = visibility
    }


    fun updateData(newData: List<SurveyItem>){
        this.dataset = newData
        notifyDataSetChanged()
    }



}