package com.example.movies.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import coil.load
import com.example.movies.databinding.ItemMovieFeedBinding

class MovieAdapter(
    private val itemClickListener: (Int) -> Unit
) : ListAdapter<Item, MovieViewHolder>(ItemDiffCallbacks()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val binding = ItemMovieFeedBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MovieViewHolder(binding, itemClickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }


    override fun onBindViewHolder(
        holder: MovieViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {

        val item = getItem(position) as? ItemMovie ?: return
        val myPayload = payloads.firstOrNull() as List<*>?
        if (myPayload.isNullOrEmpty()) {
            holder.bind(item)
        } else myPayload.forEach {
            when (it) {
                is MoviePayloads.TitleChanged -> holder.setMovieTitle(it.newTitle)
                is MoviePayloads.ReleaseYearChanged -> holder.setMovieYear(it.newYear)
                is MoviePayloads.RatingChanged -> holder.setMovieRating(it.newRating)
                is MoviePayloads.ImageChanged -> holder.setMovieImage(it.newImage)
                is MoviePayloads.DescriptionChanged -> holder.setMovieGenres(it.newDescription)
            }
        }
        holder.setOnClickListener(item)
    }
}

class MovieViewHolder(
    private val binding: ItemMovieFeedBinding,
    private val itemClickListener: (Int) -> Unit
) : ViewHolder(binding.root) {

    fun bind(item: ItemMovie) {
        setMovieGenres(item.description)
        setMovieImage(item.image)
        setMovieRating(item.rating)
        setMovieTitle(item.title)
        setMovieYear(item.releaseYear)
    }

    fun setMovieImage(image: String) {
        binding.moviePoster.load("https://image.tmdb.org/t/p/w500$image")
    }

    fun setMovieTitle(title: String) {
        binding.titleText.text = title
    }

    fun setMovieGenres(genres: String) {
        binding.genresText.text = genres
    }

    fun setMovieYear(year: String) {
        binding.yearText.text = year
    }

    fun setMovieRating(rating: Double) {
        binding.ratingText.text = rating.toString()
    }

    fun setOnClickListener(item: ItemMovie) {
        binding.movieInFeedContainer.setOnClickListener {
            itemClickListener(item.id)
        }
    }
}


data class ItemMovie(
    val id: Int,
    val title: String,
    val rating: Double,
    val image: String,
    val releaseYear: String,
    val description: String
) : Item {

    override fun areItemsTheSame(other: Any): Boolean {
        return other is ItemMovie && other.id == this.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return other is ItemMovie
                && other.title == this.title
                && other.rating == this.rating
                && other.releaseYear == this.releaseYear
                && other.image == this.image
                && other.description == this.description
    }

    override fun getChangePayload(other: Any): MutableList<Payloads> {
        return mutableListOf<Payloads>().apply {
            if (other is ItemMovie) {
                if (other.title != title) {
                    add(MoviePayloads.TitleChanged(other.title))
                }
                if (other.description != description) {
                    add(MoviePayloads.DescriptionChanged(other.description))
                }
                if (other.rating != rating) {
                    add(MoviePayloads.RatingChanged(other.rating))
                }
                if (other.releaseYear != releaseYear) {
                    add(MoviePayloads.ReleaseYearChanged(other.releaseYear))
                }
                if (other.image != image) {
                    add(MoviePayloads.ImageChanged(other.image))
                }
            }
        }
    }
}

interface Payloads

sealed class MoviePayloads : Payloads {
    data class TitleChanged(val newTitle: String) : MoviePayloads()
    data class DescriptionChanged(val newDescription: String) : MoviePayloads()
    data class RatingChanged(val newRating: Double) : MoviePayloads()
    data class ImageChanged(val newImage: String) : MoviePayloads()
    data class ReleaseYearChanged(val newYear: String) : MoviePayloads()
}

