package com.example.movies.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.movies.databinding.FragmentReviewBinding
import com.example.movies.ui.viewModels.ReviewViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentReview : Fragment() {

    private var _binding: FragmentReviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ReviewViewModel>()
    private val args: FragmentReviewArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = args.movieIdReviews
        viewModel.getMovie(id)
        viewModel.movieData.observe(viewLifecycleOwner) {
            val image = it.backdropPath
            binding.movieReviewImage.load("https://image.tmdb.org/t/p/w500$image")
            binding.movieTitle.text = it.title
        }
        viewModel.exceptions.observe(viewLifecycleOwner) {
            it.printStackTrace()
        }
        starClickListeners()

    }

    private fun starClickListeners() {
        binding.apply {
            oneStar.setOnClickListener{
                oneStar.isSelected = true
                twoStar.isSelected = false
                threeStar.isSelected = false
                fourStar.isSelected = false
                fiveStar.isSelected = false
            }
            twoStar.setOnClickListener{
                oneStar.isSelected = true
                twoStar.isSelected = true
                threeStar.isSelected = false
                fourStar.isSelected = false
                fiveStar.isSelected = false
            }
            threeStar.setOnClickListener{
                oneStar.isSelected = true
                twoStar.isSelected = true
                threeStar.isSelected = true
                fourStar.isSelected = false
                fiveStar.isSelected = false
            }
            fourStar.setOnClickListener {
                oneStar.isSelected = true
                twoStar.isSelected = true
                threeStar.isSelected = true
                fourStar.isSelected = true
                fiveStar.isSelected = false
            }
            fiveStar.setOnClickListener {
                oneStar.isSelected = true
                twoStar.isSelected = true
                threeStar.isSelected = true
                fourStar.isSelected = true
                fiveStar.isSelected = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}