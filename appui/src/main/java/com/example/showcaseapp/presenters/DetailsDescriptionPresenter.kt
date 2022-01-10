package com.example.showcaseapp.presenters


import androidx.leanback.widget.AbstractDetailsDescriptionPresenter
import com.example.domainapp.models.Results

class DetailsDescriptionPresenter : AbstractDetailsDescriptionPresenter() {

    override fun onBindDescription(
        viewHolder: AbstractDetailsDescriptionPresenter.ViewHolder,
        item: Any
    ) {
        val movie = item as Results

        viewHolder.title.text = movie.title
        viewHolder.subtitle.text = movie.posterPath
        viewHolder.body.text = movie.posterPath
    }
}