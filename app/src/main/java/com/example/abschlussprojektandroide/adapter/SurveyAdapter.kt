package com.example.abschlussprojektandroide.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.VoteType
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.dataclass.model.User
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.ListItemSurveyBinding
import com.google.android.gms.common.data.DataHolder
import com.google.firebase.auth.FirebaseUser

//aktuell kann jede Survey nur einmal abgestimmt werden
//Hier fehlt noch die Logik das jeder User  abstimmen kann


class SurveyAdapter(
    private var dataset: List<SurveyItem>,
    var currentUserId: String,
    var updateSurveyItem: (SurveyItem) -> Unit //Lambda Syntas - var  die sich verhält wie eine funktion


) : RecyclerView.Adapter<SurveyAdapter.SurveyItemViewHolder>() {

    class SurveyItemViewHolder(
        val binding: ListItemSurveyBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyItemViewHolder {
        val binding =
            ListItemSurveyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        holder.binding.tvPublishedUsernameInput.text = surveyItem.userId
        holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()

        // Abstimmung
        // Radio Btn's 1+2
        holder.binding.rbOption1.setText(surveyItem.answerOption1)
        holder.binding.rbOption2.setText(surveyItem.answerOption2)
        // Setze Text und Sichtbarkeit für RadioButton 3
        if (surveyItem.answerOption3.isNullOrEmpty()) {
            holder.binding.rbOption3.visibility = View.GONE
            holder.binding.tvVoteCountOption3.visibility = View.GONE
        } else {
            holder.binding.rbOption3.visibility = View.VISIBLE
            holder.binding.rbOption3.text = surveyItem.answerOption3
        }
        // Setze Text und Sichtbarkeit für RadioButton 4
        if (surveyItem.answerOption4.isNullOrEmpty()) {
            holder.binding.rbOption4.visibility = View.GONE
            holder.binding.tvVoteCountOption4.visibility = View.GONE
        } else {
            holder.binding.rbOption4.visibility = View.VISIBLE
            holder.binding.rbOption4.text = surveyItem.answerOption4
        }


        // die Sichtbarkeit der Prozentangaben basierend auf der showPercentages-Eigenschaft - erst nach Abstimmung sichtbar
        val percentageVisibility = if (surveyItem.showPercentage) View.VISIBLE else View.INVISIBLE
        holder.binding.tvVoteCountOption1.visibility = percentageVisibility
        holder.binding.tvVoteCountOption2.visibility = percentageVisibility
        // Setze Text und Sichtbarkeit für RadioButton 3
        if (surveyItem.answerOption3.isEmpty()) {
            holder.binding.rbOption3.visibility = View.GONE
            holder.binding.tvVoteCountOption3.visibility = View.GONE
        } else {
            holder.binding.rbOption3.visibility = View.VISIBLE
            holder.binding.rbOption3.text = surveyItem.answerOption3
            // Stellen Sie sicher, dass die Prozentanzeige aktualisiert wird, wenn die Antwortoption nicht leer ist
            if (surveyItem.showPercentage) {
                holder.binding.tvVoteCountOption3.text = surveyItem.percentageOption3()
                holder.binding.tvVoteCountOption3.visibility = View.VISIBLE
            } else {
                holder.binding.tvVoteCountOption3.visibility = View.INVISIBLE
            }
        }

        if (surveyItem.answerOption4.isEmpty()) {
            holder.binding.rbOption4.visibility = View.GONE
            holder.binding.tvVoteCountOption4.visibility = View.GONE
        } else {
            holder.binding.rbOption4.visibility = View.VISIBLE
            holder.binding.rbOption4.text = surveyItem.answerOption4
            // Auch hier die Prozentanzeige aktualisieren
            if (surveyItem.showPercentage) {
                holder.binding.tvVoteCountOption4.text = surveyItem.percentageOption4()
                holder.binding.tvVoteCountOption4.visibility = View.VISIBLE
            } else {
                holder.binding.tvVoteCountOption4.visibility = View.INVISIBLE
            }
        }


        //_______________________________________________________________________

        //UP & Down Vote der Frage Logik
        holder.binding.btnVoteUp.setOnClickListener {
            surveyItem.questionUpVotes++
            surveyItem.totalUpDownVotes()
            disableVotingUpDownBtn(holder)
            holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()
        }
        holder.binding.btnVoteDown.setOnClickListener {
            surveyItem.questionDownVotes++
            disableVotingUpDownBtn(holder)
            holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()
        }

        //_______________________________________________________________________

        //Abstimmungslogik der Frage
        if (!surveyItem.votedUser.contains(currentUserId)) {
            holder.binding.rbOption1.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION1)
                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                updateSurveyItem(surveyItem)
            }
            holder.binding.rbOption2.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION2)
                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                updateSurveyItem(surveyItem)
            }
            holder.binding.rbOption3.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION3)
                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                updateSurveyItem(surveyItem)
            }
            holder.binding.rbOption4.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION4)
                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                updateSurveyItem(surveyItem)
            }

        }
        else{
            disableVotingButtons(holder)
            holder.binding.tvVoteCountOption1.text = surveyItem.percentageOption1()
            holder.binding.tvVoteCountOption2.text = surveyItem.percentageOption2()
            holder.binding.tvVoteCountOption3.text = surveyItem.percentageOption3()
            holder.binding.tvVoteCountOption4.text = surveyItem.percentageOption4()
        }




        //_______________________________________________________________________

        // SaveBtn - speicherung der Umfrage im Profil
        //Todo : Speicherung im Profil
        holder.binding.ivSaveIcon.setOnClickListener {

            // Die Survey-ID, die gespeichter wird möchten
            val surveyIdToSave = surveyItem.surveyid

            // Überprüfen, ob die Umfrage bereits gespeichert ist
            val isSaved = surveyItem.surveySaved

            // Inflate the custom toast layout
            val toastLayout =
                LayoutInflater.from(holder.itemView.context).inflate(R.layout.custom_toast, null)
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
                    "Ergebnisse: Wahr - ${surveyItem.votesOption1}, Neutral - ${surveyItem.votesOption2}, Falsch - ${surveyItem.votesOption3} \n" +
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
            holder.itemView.context.startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Teilen über..."
                )
            )
        }
    }


    private fun disableVotingButtons(holder: SurveyItemViewHolder) {
        holder.binding.rbOption1.isEnabled = false
        holder.binding.rbOption2.isEnabled = false
        holder.binding.rbOption3.isEnabled = false
        holder.binding.rbOption4.isEnabled = false
    }

    private fun disableVotingUpDownBtn(holder: SurveyItemViewHolder) {
        holder.binding.btnVoteUp.isEnabled = false
        holder.binding.btnVoteDown.isEnabled = false
    }

    private fun updatePercentageVisibility(holder: SurveyItemViewHolder, surveyItem: SurveyItem) {
        // Sichtbarkeit für die Prozentsatzanzeigen der Antwortoptionen 1 und 2
        val percentageVisibility = if (surveyItem.showPercentage) View.VISIBLE else View.INVISIBLE
        holder.binding.tvVoteCountOption1.visibility = percentageVisibility
        holder.binding.tvVoteCountOption2.visibility = percentageVisibility

        // Sichtbarkeit für die Prozentsatzanzeigen der Antwortoptionen 3 und 4
        holder.binding.tvVoteCountOption3.visibility =
            if (surveyItem.showPercentage && !surveyItem.answerOption3.isNullOrEmpty()) View.VISIBLE else View.GONE
        holder.binding.tvVoteCountOption4.visibility =
            if (surveyItem.showPercentage && !surveyItem.answerOption4.isNullOrEmpty()) View.VISIBLE else View.GONE
    }


    fun updateData(newData: List<SurveyItem>) {
        dataset = newData
        notifyDataSetChanged() // Benachrichtigen Sie den Adapter, dass die Daten aktualisiert wurden
    }
    fun updatePercentage(holder: SurveyItemViewHolder, surveyItem: SurveyItem){
        holder.binding.tvVoteCountOption1.text = surveyItem.percentageOption1()
        holder.binding.tvVoteCountOption2.text = surveyItem.percentageOption2()
        holder.binding.tvVoteCountOption3.text = surveyItem.percentageOption3()
        holder.binding.tvVoteCountOption4.text = surveyItem.percentageOption4()
    }


}