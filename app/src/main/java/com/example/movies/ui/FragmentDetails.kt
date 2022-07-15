package com.example.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.example.movies.R
import com.example.movies.databinding.FragmentDescriptionOptionBinding
import com.example.movies.databinding.FragmentDetailsBinding
import com.example.movies.databinding.FragmentReviewOptionBinding
import com.example.movies.ui.viewModels.DetailsViewModel
import com.example.movies.ui.viewModels.ReviewOptionViewModel
import com.example.movies.utils.getNavOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentDetails : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailsViewModel>()

    private val args: FragmentDetailsArgs by navArgs()
    val fragments by lazy {
        mutableListOf(
            FragmentReviewOption.newInstance(FragmentReviewOptionArgs(args.movieId)),
            FragmentDescriptionOption()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.movieId
        viewModel.getMovie(id)

        binding.apply {
            viewPager.adapter = ViewPagerAdapter(this@FragmentDetails)
            reviewsOption.isSelected = true
            descriptionOption.isSelected = false

            viewModel.movieData.observe(viewLifecycleOwner) { movie ->
                titleInDetails.text = movie.title
                val image = movie.backdropPath
                imageInDetails.load("https://image.tmdb.org/t/p/original$image") {
                    crossfade(600)
                }
                titleInDetails.text = movie.title
                pointsText.text = movie.voteAverage.toString()
                yearTextInDetails.text = movie.releaseDate.take(4)
                genresTextInDetails.text = movie.tagline
                (fragments[1] as FragmentDescriptionOption).onDescriptionRetrieved(movie.overview)
            }
            viewModel.getReviewsNumber(id)
            viewModel.totalReviews.observe(viewLifecycleOwner) { totalReviews ->
                if (totalReviews > 0) {
                    reviewsOption.text = getString(R.string.reviews, totalReviews.toString())
                    viewPager.setCurrentItem(0, true)
                } else {
                    reviewsOption.isVisible = false
                    descriptionOption.isSelected = true
                    viewPager.setCurrentItem(1, true)
                    viewPager.isUserInputEnabled = false
                }
            }
            viewModel.exceptions.observe(viewLifecycleOwner) {
                it.printStackTrace()
            }

            reviewsOption.setOnClickListener {
                viewPager.setCurrentItem(0, true)
            }
            descriptionOption.setOnClickListener {
                viewPager.setCurrentItem(1, true)
            }
            writeReviewButton.setOnClickListener {
                val directions = FragmentDetailsDirections.actionMovieDetailsToReviewFragment(id)
                viewPager.setCurrentItem(0, true)
                findNavController().navigate(directions, getNavOptions())
            }

            viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    binding.reviewsOption.isSelected = position == 0
                    binding.descriptionOption.isSelected = position == 1
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        fragments.clear()
    }

}

class ViewPagerAdapter(val fragment: FragmentDetails) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return fragment.fragments[position]
    }
}

@AndroidEntryPoint
class FragmentReviewOption : Fragment() {
    private var _binding: FragmentReviewOptionBinding? = null
    private val binding get() = _binding!!
    private val adapter = ReviewAdapter()
    private val viewModel by viewModels<ReviewOptionViewModel>()
    private val args by lazy {
        arguments?.let {
            FragmentReviewOptionArgs.fromBundle(it)
        } ?: throw Exception("Invalid navigation")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getReviews(args.id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReviewOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        fun newInstance(args: FragmentReviewOptionArgs): FragmentReviewOption {
            val fragment = FragmentReviewOption()
            fragment.arguments = args.toBundle()
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.reviewsRecycler.layoutManager = LinearLayoutManager(context)
        binding.reviewsRecycler.adapter = adapter
        viewModel.reviews.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.exceptions.observe(viewLifecycleOwner) {
            it.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

@AndroidEntryPoint
class FragmentDescriptionOption : Fragment() {
    private var _binding: FragmentDescriptionOptionBinding? = null
    private val binding get() = _binding!!

    private var desc = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDescriptionOptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    fun onDescriptionRetrieved(desc: String) {
        this.desc = desc
        if (_binding != null) {
            binding.descriptionText.text = desc
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.descriptionText.text = desc
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
