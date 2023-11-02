package com.example.abschlussprojektandroide.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.abschlussprojektandroide.R
import com.example.abschlussprojektandroide.databinding.FragmentDetailVoteBinding
import com.example.abschlussprojektandroide.databinding.FragmentHomeBinding


class DetailVoteFragment : Fragment() {
    private lateinit var binding : FragmentDetailVoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDetailVoteBinding.inflate(inflater,container,false)
        return binding.root
    }
}