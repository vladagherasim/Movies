package com.example.movies.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.R
import com.example.movies.databinding.FragmentFeedBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentFeed : Fragment() {

    private var _binding : FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val adapter = MovieAdapter(this::onItemClick)

    private fun onItemClick(id: Int) {
        findNavController().navigate(R.id.action_moviesFeed_to_movieDetails)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater)
        super.onCreate(savedInstanceState)
        _binding = FragmentFeedBinding.inflate(layoutInflater)

        return binding.root
}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            feedRecyclerView.adapter = adapter
            feedRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL,  false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}