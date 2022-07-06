package com.example.movies.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies.databinding.ItemReviewRecyclerBinding

class ReviewAdapter : ListAdapter<Item, ReviewViewHolder>(ItemDiffCallbacks()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewRecyclerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        onBindViewHolder(holder, position, mutableListOf())
    }

    override fun onBindViewHolder(
        holder: ReviewViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        val item = getItem(position) as? ItemReview ?: return
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
sealed class ReviewPayloads : Payloads {
    data class TitleChanged(val newTitle: String) : ReviewPayloads()
    data class DateChanged(val newDate: String) : ReviewPayloads()
    data class RatingChanged(val newRating: String) : ReviewPayloads()
    data class ContentChanged(val newContent: String) : ReviewPayloads()
    data class AuthorChanged(val newAuthor: String) : ReviewPayloads()
}

class ReviewViewHolder(
    private val binding: ItemReviewRecyclerBinding,
) : RecyclerView.ViewHolder(binding.root) {

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