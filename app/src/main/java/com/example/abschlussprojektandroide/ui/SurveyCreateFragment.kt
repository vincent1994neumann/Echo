package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.data.dataclass.model.SurveyItem
import com.example.abschlussprojektandroide.data.viewmodel.SharedViewModel
import com.example.abschlussprojektandroide.databinding.FragmentSurveyCreateBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp


class SurveyCreateFragment : Fragment() {
    private lateinit var binding: FragmentSurveyCreateBinding
    private val viewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSurveyCreateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            createSurveyItem()
            findNavController().popBackStack()
        }
        binding.addAnswerButton.setOnClickListener { addAnswerField() }
        binding.removeAnswerButton.setOnClickListener { removeAnswerField() }
    }

    private fun addAnswerField() {
        val answerFields = listOf(binding.tilAnswer3, binding.tilAnswer4) // Nur die zusätzlichen Felder
        val nextHiddenField = answerFields.firstOrNull { it.visibility == View.GONE }
        nextHiddenField?.visibility = View.VISIBLE
    }

    private fun removeAnswerField() {
        val answerFields = listOf(binding.tilAnswer3, binding.tilAnswer4) // Nur die zusätzlichen Felder
        val lastVisibleField = answerFields.lastOrNull { it.visibility == View.VISIBLE }
        lastVisibleField?.visibility = View.GONE
    }


    private fun createSurveyItem(){

        val header = binding.tiHeaderSurvey.text.toString()
        val surveyText = binding.tiTextSurvey.text.toString()
        val category = binding.spinnerCategory.selectedItem.toString()
        val answerOption1 = binding.etAnswer1.text.toString()
        val answerOption2 = binding.etAnswer2.text.toString()
        val answerOption3 = binding.etAnswer3.text.toString()
        val answerOption4 = binding.etAnswer4.text.toString()

        if (header.isBlank() || surveyText.isBlank() || answerOption1.isBlank() || answerOption2.isBlank()){
            Snackbar.make(binding.root, "Header, Survey Text, and at least two Answer Options are required.", Snackbar.LENGTH_LONG).show()
        }
        val surveyItem = viewModel.currentUser.value?.let {
            SurveyItem(
                surveyid ="",
                userId = it.uid ,
                timestamp = Timestamp.now(),
                publishedBy =it.uid, // ToDo - richtigen Namen vom User einpflegen (Datenbank user bearbeite)
                isPublished = false,
                header = header,
                category = category,
                surveyText= surveyText,
                answerOption1 = answerOption1,
                answerOption2 = answerOption2,
                answerOption3 = answerOption3,
                answerOption4 = answerOption4
            )
        }
        if (surveyItem != null) {
            viewModel.saveSurveyItem(surveyItem)
        }
    }
}
