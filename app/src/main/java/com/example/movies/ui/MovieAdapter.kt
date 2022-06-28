package com.example.movies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.movies.R
import com.example.movies.databinding.ItemMovieFeedBinding
import com.example.movies.databinding.ItemReviewRecyclerBinding

class MovieAdapter(
    private val itemClickListener: (Int) -> Unit
) : ListAdapter<Item, ViewHolder>(ItemDiffCallbacks()) {

    private val itemMovie: Int = 1
    private val itemReview: Int = 2
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        when (viewType) {
            itemMovie -> {
                val binding = ItemMovieFeedBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return MovieViewHolder(binding, itemClickListener)
            }
            itemReview -> {
                val binding = ItemReviewRecyclerBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                return ReviewViewHolder(binding)
            }
            else -> throw IllegalArgumentException("No such type")
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position) is ItemMovie -> itemMovie
            getItem(position) is ItemReview -> itemReview
            else -> throw IllegalArgumentException("No such type")
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        when (holder) {
            is MovieViewHolder -> {
                val item = getItem(position) as ItemMovie? ?: return
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
            is ReviewViewHolder -> {
                val item = getItem(position) as ItemReview? ?: return
                val myPayload = payloads.firstOrNull() as List<*>?
                if (myPayload.isNullOrEmpty()) {
                    holder.bind(item)
                } else myPayload.forEach {
                    when (it) {
                        is ReviewPayloads.TitleChanged -> holder.setReviewTitle(it.newTitle)
                        is ReviewPayloads.ContentChanged -> holder.setReviewContent(it.newContent)
                        is ReviewPayloads.AuthorChanged -> holder.setReviewAuthor(it.newAuthor)
                        is ReviewPayloads.DateChanged -> holder.setReviewDate(it.newDate)
                        is ReviewPayloads.RatingChanged -> holder.setReviewRating(it.newRating)
                    }
                }
            }
        }

    }

}

class MovieViewHolder (
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

    //TODO figure a correct way to get the image url
    fun setMovieImage(image: String) {
        //binding.moviePoster.load(image)
        binding.moviePoster.setImageResource(R.drawable.ic_star_big)
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

class ReviewViewHolder(
    private val binding: ItemReviewRecyclerBinding,
) : ViewHolder(binding.root) {

    fun bind(item: ItemReview) {
        setReviewAuthor(item.author)
        setReviewContent(item.content)
        setReviewDate(item.date)
        setReviewRating(item.rating)
        setReviewTitle(item.title)
    }

    fun setReviewTitle(title: String) {
        binding.titleReview.text = title
    }

    fun setReviewDate(date: String) {
        binding.reviewDate.text = date
    }

    fun setReviewRating(rating: String) {
        binding.rating.text = rating
    }

    fun setReviewAuthor(author: String) {
        binding.author.text = author
    }

    fun setReviewContent(content: String) {
        binding.reviewContent.text = content
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

sealed class ReviewPayloads : Payloads {
    data class TitleChanged(val newTitle: String) : ReviewPayloads()
    data class DateChanged(val newDate: String) : ReviewPayloads()
    data class RatingChanged(val newRating: String) : ReviewPayloads()
    data class ContentChanged(val newContent: String) : ReviewPayloads()
    data class AuthorChanged(val newAuthor: String) : ReviewPayloads()
}

data class ItemReview(
    val id: String,
    val author: String,
    val rating: String,
    val title: String,
    val date: String,
    val content: String
) : Item {

    override fun areItemsTheSame(other: Any): Boolean {
        return (other is ItemReview) && (other.id == this.id)
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return (other is ItemReview)
                && (other.author == this.author)
                && (other.content == this.content)
                && (other.date == this.date)
                && (other.rating == this.rating)
                && (other.title == this.title)
    }

    override fun getChangePayload(other: Any): MutableList<Payloads> {
        return mutableListOf<Payloads>().apply {
            if (other is ItemReview) {
                if (other.title != title) {
                    add(ReviewPayloads.TitleChanged(other.title))
                }
                if (other.author != author) {
                    add(ReviewPayloads.AuthorChanged(other.author))
                }
                if (other.content != content) {
                    add(ReviewPayloads.ContentChanged(other.content))
                }
                if (other.date != date) {
                    add(ReviewPayloads.DateChanged(other.date))
                }
                if (other.rating != rating) {
                    add(ReviewPayloads.RatingChanged(other.rating))
                }
            }
        }
    }

}