package com.example.movies.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.movies.R
import com.example.movies.databinding.FragmentDetailsBinding
import com.example.movies.ui.viewModels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDetails : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapter = ReviewAdapter()
    private val viewModel by viewModels<DetailsViewModel>()
    private val args: FragmentDetailsArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.movieId
        binding.apply {
            reviewsRecycler.adapter = adapter
            reviewsRecycler.layoutManager = LinearLayoutManager(context)
            viewModel.getMovie(id)
            viewModel.movieData.observe(viewLifecycleOwner) { movie ->
                titleInDetails.text = movie.title
                val image = movie.backdropPath
                imageInDetails.load("https://image.tmdb.org/t/p/original$image")
                titleInDetails.text = movie.title
                pointsText.text = movie.voteAverage.toString()
                yearTextInDetails.text = movie.releaseDate.take(4)
                genresTextInDetails.text = movie.tagline
                descriptionText.text = movie.overview
            }
            viewModel.getReviews(id)
            viewModel.getReviewsNumber(id)
            viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
                adapter.submitList(reviews)
            }
            viewModel.totalReviews.observe(viewLifecycleOwner) { totalReviews ->
                reviewsOption.text = getString(R.string.reviews, totalReviews.toString())
            }
            viewModel.exceptions.observe(viewLifecycleOwner) {
                it.printStackTrace()
            }
            reviewsOption.isSelected = true
            descriptionOption.isSelected = false
            reviewsRecycler.isVisible = true
            descriptionText.isVisible = false
            reviewsOption.setOnClickListener {
                reviewsOption.apply {
                    isSelected = !isSelected
                    reviewsRecycler.isVisible = isSelected
                    descriptionOption.isSelected = !isSelected
                    descriptionText.isVisible = !isSelected
                }
            }
            descriptionOption.setOnClickListener {
                descriptionOption.apply {
                    isSelected = !isSelected
                    descriptionText.isVisible = isSelected
                    reviewsOption.isSelected = !isSelected
                    reviewsRecycler.isVisible = !isSelected
                }
            }
            writeReviewButton.setOnClickListener {
                val directions = FragmentDetailsDirections.actionMovieDetailsToReviewFragment(id)
                findNavController().navigate(directions)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}