package com.example.movies.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.movies.databinding.FragmentDetailsBinding
import com.example.movies.ui.viewModels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDetails : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = MovieAdapter(this::onItemClick)
    private val viewModel by viewModels<DetailsViewModel>()
    private val args: FragmentDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        super.onCreate(savedInstanceState)
        _binding = FragmentDetailsBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.movieId
        binding.apply {
            reviewsRecycler.adapter = adapter
            reviewsRecycler.layoutManager = LinearLayoutManager(context)
            viewModel.getMovie(id)
            viewModel.movieData.observe(viewLifecycleOwner) { movie ->
                titleInDetails.text = movie.title
                imageInDetails.load(movie.backdropPath)
                titleInDetails.text = movie.title
                pointsText.text = movie.voteAverage.toString()
                yearTextInDetails.text = movie.releaseDate
                genresTextInDetails.text = movie.tagline
            }
            viewModel.getReviews(id)
            viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
                adapter.submitList(reviews)
            }
            viewModel.exceptions.observe(viewLifecycleOwner) {
                it.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onItemClick(id: Int) {
        TODO("Not yet implemented")
    }

}