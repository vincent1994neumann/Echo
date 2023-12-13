package com.example.abschlussprojektandroide.adapter

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.util.VoteType
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.databinding.ListItemSurveyBinding
import com.example.abschlussprojektandroide.util.VoteTypeQuestion
import com.google.android.material.snackbar.Snackbar

class SurveyAdapter(
    private var dataset: List<SurveyItem>, // Liste von Umfrage-Elementen
    var currentUserId: String, // ID des aktuellen Benutzers
    var updateSurveyItem: (SurveyItem) -> Unit, // Lambda-Funktion zum Aktualisieren eines Umfrageelements
    var onSaveClicked: (String,Boolean) -> Unit, // Lambda-Funktion für Klick auf Speichern-Button
    var savedSurveyItems: MutableList<String> // Liste der gespeicherten Umfrageelemente

) : RecyclerView.Adapter<SurveyAdapter.SurveyItemViewHolder>() { // Erweiterung des RecyclerView.Adapter


    class SurveyItemViewHolder(
        val binding: ListItemSurveyBinding
    ) : RecyclerView.ViewHolder(binding.root)

    // Erzeugt neuen ViewHolder für jedes Umfrageelement
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

        //Ausklappen der Voting Funktion
        val btnToggle :ImageButton = holder.binding.btnToggleDown
        val voteSection: ViewGroup = holder.binding.voteSection

        btnToggle.setOnClickListener {
            val isVisible = voteSection.visibility == View.VISIBLE
            voteSection.visibility = if (isVisible) View.GONE else View.VISIBLE
            if (isVisible){
                btnToggle.setImageResource(R.drawable.ic_dropdown_arrow)
            }else{
                btnToggle.setImageResource(R.drawable.ic_dropup_arrow_24)
            }

        }



        // Hier beginnt die Zuweisung der Daten an die Views (Textfelder, Buttons usw.)
        holder.binding.tvHeaderCv.text = surveyItem.header
        holder.binding.tvCategorieCv.text = surveyItem.category
        holder.binding.tvSurveyText.text = surveyItem.surveyText
        holder.binding.tvTimestamp.text = surveyItem.getFormattedTime()
        holder.binding.tvPublishedUsernameInput.text = surveyItem.publishedBy
        holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()
        holder.binding.tvTotalVoteCount.text = surveyItem.totalVotes.toString()


        //_______________________________________________________________________

        //UP & Down Vote der Frage Logik
        updateUiAfterVoteQuestion2(surveyItem, holder) // Aktualisiert die Benutzeroberfläche nach einer Abstimmung.

        // Überprüft, ob der aktuelle Benutzer bereits abgestimmt hat.
        if (!surveyItem.votedQuestionUser.contains(currentUserId)){
        // Aktiviert die Up- und Down-Vote-Buttons.
            enableUpDownVotingButtons(holder)
            var UpDownButtonList = listOf(
                holder.binding.btnVoteUp,
                holder.binding.btnVoteDown
            )
            UpDownButtonList.forEach { it.isEnabled = true  }

            /* Wenn der Up-Vote-Button gedrückt wird, werden folgende Aktionen ausgeführt:
                  1. Der aktuelle Benutzer wird zur Liste der Abstimmenden hinzugefügt.
                  2. Die Gesamtzahl der Up- und Down-Votes wird aktualisiert.
                  3. Der aktualisierte Wert wird in der Benutzeroberfläche angezeigt.
                  4. Das SurveyItem-Objekt wird mit den neuen Daten aktualisiert.
                  5. Der Up-Vote-Button wird visuell als ausgewählt markiert.
                  6. Eine Like-Animation wird ausgelöst.
                  7. Die Abstimmungsbuttons werden deaktiviert.
                  8. Die Benutzeroberfläche wird erneut aktualisiert, um die Änderungen widerzuspiegeln. */
            holder.binding.btnVoteUp.setOnClickListener {
                surveyItem.addUserToQuestionUpDownVote(currentUserId,VoteTypeQuestion.OPTIONUP)
                surveyItem.totalUpDownVotes()
                holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()
                updateSurveyItem(surveyItem)
                holder.binding.btnVoteUp.setImageResource(R.drawable.ic_thumbsup_filled24)
                surveyItem.hasVotedQuestion = true
                animationLike(holder)
                disableVotingUpDownBtn(holder)
                updateUiAfterVoteQuestion2(surveyItem,holder)
            }
            /* Wenn der Down-Vote-Button gedrückt wird, werden folgende Aktionen ausgeführt:
      1. Der aktuelle Benutzer wird zur Liste der Abstimmenden hinzugefügt.
      2. Die Gesamtzahl der Up- und Down-Votes wird aktualisiert.
      3. Der aktualisierte Wert wird in der Benutzeroberfläche angezeigt.
      4. Das SurveyItem-Objekt wird mit den neuen Daten aktualisiert.
      5. Der Down-Vote-Button wird visuell als ausgewählt markiert.
      6. Eine Like-Animation wird ausgelöst.
      7. Die Abstimmungsbuttons werden deaktiviert.
      8. Die Benutzeroberfläche wird erneut aktualisiert, um die Änderungen widerzuspiegeln. */
            holder.binding.btnVoteDown.setOnClickListener {
                surveyItem.addUserToQuestionUpDownVote(currentUserId,VoteTypeQuestion.OPTIONDOWN)
                surveyItem.totalUpDownVotes()
                holder.binding.tvVoteCounter.text = surveyItem.totalUpDownVotes()
                updateSurveyItem(surveyItem)
                holder.binding.btnVoteDown.setImageResource(R.drawable.ic_thumbsdown_filled24)
                surveyItem.hasVotedQuestion = true
                animationDisLike(holder)
                disableVotingUpDownBtn(holder)
                updateUiAfterVoteQuestion2(surveyItem,holder)
            }
        }

        //_______________________________________________________________________

        // Logik zur Verwaltung der Sichtbarkeit und Interaktion der Abstimmungsoptionen
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


        // Definiert eine Variable für die Sichtbarkeit der Prozentanzeige basierend auf dem `showPercentage`-Attribut des `surveyItem`.
        val percentageVisibility = if (surveyItem.showPercentage) View.VISIBLE else View.INVISIBLE
        /* Setzt die Sichtbarkeit und den Text für die Anzeige der Prozentwerte der ersten Option.
        1. `tvVoteCountOption1.visibility` - Legt fest, ob die Prozentanzeige sichtbar oder unsichtbar sein soll.
        2. `tvVoteCountOption1.text` - Zeigt den prozentualen Anteil der ersten Option an, basierend auf der Methode `percentageOption1` des `surveyItem`. */
        holder.binding.tvVoteCountOption1.visibility = percentageVisibility
        holder.binding.tvVoteCountOption1.text = surveyItem.percentageOption1()
        holder.binding.tvVoteCountOption2.visibility = percentageVisibility
        holder.binding.tvVoteCountOption2.text = surveyItem.percentageOption2()

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
        //Abstimmungslogik der Frage

        //Update der UI da sonst die RV die ListItems nicht richtig recyclet
        updateUiAfterVote(surveyItem,holder)
        if (!surveyItem.votedUser.contains(currentUserId)) {

            //Um die Auswahl der radiobtn zu reseten
            //ListItem wird geupdatet auf den tatsächlichen Stand
            enableVotingButtons(holder)
            var radioButtonList = listOf(
                holder.binding.rbOption1,
                holder.binding.rbOption2,
                holder.binding.rbOption3,
                holder.binding.rbOption4
            )
            radioButtonList.forEach { it.isChecked = false }

            /* Für jeden Radio-Button wird ein OnClickListener gesetzt, der:
             1. Die Stimme des Benutzers zur Umfrage hinzufügt.
             2. Die Abstimmungsbuttons deaktiviert.
             3. Die Anzeige der Prozentsätze aktiviert und aktualisiert.
             4. Die Gesamtstimmenzahl in der UI aktualisiert.
             5. Die UI nach der Stimmabgabe aktualisiert.
             6. Das SurveyItem-Objekt mit den neuen Daten aktualisiert.
             Dieser Vorgang wird für alle vier Optionen wiederholt. */

            holder.binding.rbOption1.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION1)
                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                holder.binding.tvTotalVoteCount.text = surveyItem.totalVotes.toString() // aktualisiert den totalVote Count, sobald abgestimmt wurde
                updateUiAfterVote(surveyItem,holder)
                updateSurveyItem(surveyItem)
            }
            holder.binding.rbOption2.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION2)

                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                holder.binding.tvTotalVoteCount.text = surveyItem.totalVotes.toString()
                updateUiAfterVote(surveyItem,holder)
                updateSurveyItem(surveyItem)
            }
            holder.binding.rbOption3.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION3)

                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                holder.binding.tvTotalVoteCount.text = surveyItem.totalVotes.toString()
                updateUiAfterVote(surveyItem,holder)
                updateSurveyItem(surveyItem)
            }
            holder.binding.rbOption4.setOnClickListener {
                surveyItem.addVote(currentUserId, VoteType.OPTION4)

                disableVotingButtons(holder)
                surveyItem.showPercentage = true
                updatePercentageVisibility(holder, surveyItem)
                updatePercentage(holder,surveyItem)
                holder.binding.tvTotalVoteCount.text = surveyItem.totalVotes.toString()
                updateUiAfterVote(surveyItem,holder)
                updateSurveyItem(surveyItem)
            }
        }
        else{
            /* Wenn der Benutzer bereits abgestimmt hat, werden die Abstimmungsbuttons deaktiviert und
             die Prozentsätze der einzelnen Optionen in der UI aktualisiert. */
            disableVotingButtons(holder)
            holder.binding.tvVoteCountOption1.text = surveyItem.percentageOption1()
            holder.binding.tvVoteCountOption2.text = surveyItem.percentageOption2()
            holder.binding.tvVoteCountOption3.text = surveyItem.percentageOption3()
            holder.binding.tvVoteCountOption4.text = surveyItem.percentageOption4()
        }

        //_______________________________________________________________________
        // SaveBtn - Speicherung der Umfrage im Profil

        // Überprüft, ob das aktuelle Umfrage-Item bereits gespeichert ist, und setzt das entsprechende Icon.
        if (savedSurveyItems.contains(surveyItem.surveyid)) {
            holder.binding.ivSaveIcon.setImageResource(R.drawable.baseline_bookmark_24)
        } else {
            holder.binding.ivSaveIcon.setImageResource(R.drawable.baseline_bookmark_border_24)
        }
        holder.binding.ivSaveIcon.setOnClickListener {
            val isCurrentlySaved = savedSurveyItems.contains(surveyItem.surveyid)

            /* Ruft die Callback-Methode `onSaveClicked` auf und aktualisiert die lokale Liste sofort.
        Die Callback-Methode wird verwendet, um Aktionen auszuführen, die notwendig sind, wenn ein Item gespeichert oder entfernt wird. */
            onSaveClicked(surveyItem.surveyid, !isCurrentlySaved)
            if (isCurrentlySaved) {
                savedSurveyItems.remove(surveyItem.surveyid)
                holder.binding.ivSaveIcon.setImageResource(R.drawable.baseline_bookmark_border_24)
                showToast(holder, R.string.survey_removed, R.drawable.baseline_bookmark_border_24)
            } else {
                savedSurveyItems.add(surveyItem.surveyid)
                holder.binding.ivSaveIcon.setImageResource(R.drawable.baseline_bookmark_24)
                showToast(holder, R.string.survey_saved, R.drawable.baseline_bookmark_24)
            }

            // Benachrichtigen Sie den Adapter über die Änderung des spezifischen Items
            notifyItemChanged(position)
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



    private fun animationLike(holder: SurveyItemViewHolder){

        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(holder.binding.btnVoteUp,scaleX,scaleY)
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.interpolator = BounceInterpolator()
        animator.start()
    }
    private fun animationDisLike(holder: SurveyItemViewHolder){
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X,2f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y,2f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(holder.binding.btnVoteDown,scaleX,scaleY)
        animator.duration = 500
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.interpolator = BounceInterpolator()
        animator.start()
    }
    private fun disableVotingUpDownBtn(holder: SurveyItemViewHolder) {
        holder.binding.btnVoteUp.isEnabled = false
        holder.binding.btnVoteDown.isEnabled = false
    }

    private fun enableUpDownVotingButtons(holder: SurveyItemViewHolder){
        holder.binding.btnVoteUp.isEnabled = true
        holder.binding.btnVoteDown.isEnabled = true
    }


    private fun updateUiAfterVoteQuestion2(surveyItem: SurveyItem, holder: SurveyAdapter.SurveyItemViewHolder) {
        if (surveyItem.votedQuestionUser.contains(currentUserId)) {
            disableVotingUpDownBtn(holder)
            holder.binding.btnVoteUp.setImageResource(R.drawable.ic_thumbsup_filled24)
            holder.binding.btnVoteDown.setImageResource(R.drawable.ic_thumbsdown_filled24)
        }else{
            holder.binding.btnVoteUp.setImageResource(R.drawable.ic_thumbs24)
            holder.binding.btnVoteDown.setImageResource(R.drawable.thumbs_down_24)
            holder.binding.btnVoteDown.isVisible = true
            holder.binding.btnVoteDown.isVisible = true
            holder.binding.upDownCheck.isVisible = false

        }
    }


    private fun disableVotingButtons(holder: SurveyItemViewHolder) {
        holder.binding.rbOption1.isEnabled = false
        holder.binding.rbOption2.isEnabled = false
        holder.binding.rbOption3.isEnabled = false
        holder.binding.rbOption4.isEnabled = false
    }
    private fun enableVotingButtons(holder: SurveyItemViewHolder) {
        holder.binding.rbOption1.isEnabled = true
        holder.binding.rbOption2.isEnabled = true
        holder.binding.rbOption3.isEnabled = true
        holder.binding.rbOption4.isEnabled = true
    }



    private fun updateUiAfterVote(surveyItem: SurveyItem, holder: SurveyItemViewHolder){
        if (surveyItem.votedUser.contains(currentUserId)){
            val votedColor = ContextCompat.getColor(holder.itemView.context, R.color.grey)
            holder.binding.cvSurvey.setCardBackgroundColor(votedColor)
            holder.binding.upDownCheck.isVisible = true
        } else {
            val notVotedColor = ContextCompat.getColor(holder.itemView.context, R.color.white)
            holder.binding.cvSurvey.setCardBackgroundColor(notVotedColor)
            holder.binding.upDownCheck.isVisible = false
        }
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


    private fun showToast(holder: SurveyAdapter.SurveyItemViewHolder, messageResId: Int, iconResId: Int) {
        // Erstellen und Anzeigen des Toasts
        val toastLayout = LayoutInflater.from(holder.itemView.context).inflate(R.layout.custom_toast, null)
        val toastText = toastLayout.findViewById<TextView>(R.id.toast_text)
        val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)

        toastText.text = holder.itemView.context.getString(messageResId)
        toastIcon.setImageResource(iconResId)

        Toast(holder.itemView.context).apply {
            duration = Toast.LENGTH_SHORT
            view = toastLayout
        }.show()
    }

}