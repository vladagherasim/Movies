package com.example.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.movies.databinding.FragmentReviewBinding
import com.example.movies.ui.viewModels.ReviewViewModel
import com.example.movies.utils.hideKeyboard
import com.example.movies.utils.setOnFocused
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

        binding.apply {

            submitButton.isEnabled = false

            fun hideSubmit() {
                appBarReview.setExpanded(false)
                submitButton.isVisible = false
                submitButton.isEnabled = true
            }
            reviewTitle.setOnFocused { hideSubmit() }
            reviewText.setOnFocused { hideSubmit() }

            reviewText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submitButton.isVisible = true
                    reviewText.hideKeyboard()
                    true
                } else false
            }

            reviewText.setOnClickListener {
                hideSubmit()
            }
            reviewTitle.setOnClickListener {
                hideSubmit()
            }

            submitButton.setOnClickListener {
                reviewText.setText("")
                reviewTitle.setText("")
                submitButton.isEnabled = false
                appBarReview.setExpanded(true)
            }
        }

    }

    private fun starClickListeners() {
        binding.apply {
            oneStar.setOnClickListener {
                oneStar.isSelected = true
                twoStar.isSelected = false
                threeStar.isSelected = false
                fourStar.isSelected = false
                fiveStar.isSelected = false
            }
            twoStar.setOnClickListener {
                oneStar.isSelected = true
                twoStar.isSelected = true
                threeStar.isSelected = false
                fourStar.isSelected = false
                fiveStar.isSelected = false
            }
            threeStar.setOnClickListener {
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