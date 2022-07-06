package com.example.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies.databinding.FragmentFeedBinding
import com.example.movies.ui.viewModels.FeedViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FragmentFeed : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    private val adapter = MovieAdapter(this::onItemClick)
    private val viewModel by viewModels<FeedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            feedRecyclerView.adapter = adapter
            feedRecyclerView.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            viewModel.getMovies()
            viewModel.movies.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            viewModel.exceptions.observe(viewLifecycleOwner) {
                it.printStackTrace()
            }

            viewModel.searchResult.observe(viewLifecycleOwner) {
                adapter.submitList(it)
            }

            var job: Job? = null
            searchBar.doOnTextChanged { text, _, _, _ ->
                job?.cancel()
                job = lifecycleScope.launch {
                    delay(500)
                    val searchText = text.toString()
                    if (searchText.isEmpty()) {
                        viewModel.getMovies()
                    } else {
                        viewModel.getSearchResults(searchText)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onItemClick(id: Int) {
        val directions = FragmentFeedDirections.actionMoviesFeedToMovieDetails(id)
        findNavController().navigate(directions)
    }
}