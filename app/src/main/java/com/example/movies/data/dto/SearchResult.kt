package com.example.movies.data.dto


import com.google.gson.annotations.SerializedName

data class SearchResult(
    @SerializedName("page")
    val page: Int,
    @SerializedName("results")
    val searchItems: List<SearchItem>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)