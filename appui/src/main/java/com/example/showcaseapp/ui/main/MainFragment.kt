package com.example.showcaseapp.ui.main

import android.content.Intent
import java.util.Timer

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import androidx.leanback.app.BackgroundManager
import androidx.leanback.app.BrowseSupportFragment
import androidx.core.content.ContextCompat
import android.util.DisplayMetrics
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.leanback.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.domainapp.models.Results
import com.example.showcaseapp.ui.details.DetailsActivity
import com.example.showcaseapp.R
import com.example.showcaseapp.viewmodel.ViewModel
import com.example.showcaseapp.presenters.CardPresenter
import com.example.showcaseapp.utils.Constants
import java.lang.Integer.MIN_VALUE

/**
 * Loads a grid of cards with movies to browse.
 */
class MainFragment : BrowseSupportFragment() {

    private val mHandler = Handler()
    private lateinit var mBackgroundManager: BackgroundManager
    private var mDefaultBackground: Drawable? = null
    private lateinit var mMetrics: DisplayMetrics
    private var mBackgroundTimer: Timer? = null
    private var mBackgroundUri: String? = null
    private lateinit var rowsAdapter:ArrayObjectAdapter
    private lateinit var mViewModel: ViewModel
    private var favouritePosition = MIN_VALUE

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        prepareBackgroundManager()
        setupUIElements()
        initViewModel()
        loadRows()
        setupEventListeners()
    }

    private fun initViewModel(){

        mViewModel = ViewModelProvider(this)[ViewModel::class.java]
        mViewModel.makeApiCall()
    }

    override fun onDestroy() {

        super.onDestroy()
        mBackgroundTimer?.cancel()
    }

    private fun prepareBackgroundManager() {

        mBackgroundManager = BackgroundManager.getInstance(activity)
        mBackgroundManager.attach(requireActivity().window)
        mDefaultBackground = ContextCompat.getDrawable(requireActivity(),
            R.drawable.default_background
        )
        mMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(mMetrics)
    }

    private fun setupUIElements() {
        title = "TMDb"
        // over title
        headersState = BrowseSupportFragment.HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true

        // set fastLane (or headers) background color
        brandColor = ContextCompat.getColor(requireActivity(), R.color.fastlane_background)
        // set search icon color
        searchAffordanceColor = ContextCompat.getColor(requireActivity(), R.color.search_opaque)
    }

    private fun loadRows() {

        rowsAdapter = ArrayObjectAdapter(ListRowPresenter())
        Handler().postDelayed({
            createRows()
            startEntranceTransition()
        }, 500)
    }

    private fun createRows() {

        mViewModel.getTopRatedMoviesMutableLiveData()?.observe(viewLifecycleOwner, Observer {
            updateMovieList(0, "Top Rated Movies", it as ArrayList<Results>)
        })

        mViewModel.getPopularMoviesMutableLiveData()?.observe(viewLifecycleOwner, Observer {
            updateMovieList(1, "Popular Movies", it as ArrayList<Results>)
        })

        mViewModel.getFavouriteMovies()?.observe(viewLifecycleOwner, Observer { it ->
            val newList = it.map {
                return@map it.result
            }
            updateMovieList(2, "Favourite Movies", newList as ArrayList<Results>)
        })
    }

    private fun updateMovieList(index:Int,category:String,list: ArrayList<Results>) {

        if(favouritePosition== MIN_VALUE && category == "Favourite Movies"){
            favouritePosition = rowsAdapter.size()
        }

        val cardPresenter = CardPresenter()
            val listRowAdapter = ArrayObjectAdapter(cardPresenter)
            for (j in 0 until list.size) {
                listRowAdapter.add(list?.get(j))
            }
            val header = HeaderItem(index.toLong() ,category)
            if(rowsAdapter.size() == 3){
                rowsAdapter.replace(favouritePosition,ListRow(header,listRowAdapter))
            }
        else
            rowsAdapter.add(ListRow(header, listRowAdapter))

        adapter = rowsAdapter
    }

    private fun setupEventListeners() {
        setOnSearchClickedListener {
            Toast.makeText(requireActivity(), "Implement your own in-app search", Toast.LENGTH_LONG)
                .show()
        }
        onItemViewClickedListener = ItemViewClickedListener()
        onItemViewSelectedListener = ItemViewSelectedListener()
    }

    private inner class ItemViewClickedListener : OnItemViewClickedListener {
        override fun onItemClicked(
            itemViewHolder: Presenter.ViewHolder,
            item: Any,
            rowViewHolder: RowPresenter.ViewHolder,
            row: Row
        ) {

            if (item is Results) {
                val intent = Intent(activity!!, DetailsActivity::class.java)
                intent.putExtra(DetailsActivity.MOVIE, item)
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    (itemViewHolder.view as ImageCardView).mainImageView,
                    DetailsActivity.SHARED_ELEMENT_NAME
                )
                    .toBundle()
                startActivity(intent, bundle)
            }
            else{
                return
            }
        }
    }

    private inner class ItemViewSelectedListener : OnItemViewSelectedListener {
        override fun onItemSelected(
            itemViewHolder: Presenter.ViewHolder?, item: Any?,
            rowViewHolder: RowPresenter.ViewHolder, row: Row
        ) {
            if (item is Results) {
                mBackgroundUri = Constants.API_IMAGE_URI_780 + item.posterPath
                Handler().postDelayed({
                    updateBackground(mBackgroundUri)
                }, 500)
            }
        }
    }

    private fun updateBackground(uri: String?) {

        val width = mMetrics.widthPixels
        val height = mMetrics.heightPixels
        Glide.with(requireActivity())
            .load(uri)
            .centerCrop()
            .error(mDefaultBackground)
            .into<SimpleTarget<Drawable>>(
                object : SimpleTarget<Drawable>(width, height) {
                    override fun onResourceReady(
                        drawable: Drawable,
                        transition: Transition<in Drawable>?
                    ) {
                        mBackgroundManager.drawable = drawable
                    }
                })
        mBackgroundTimer?.cancel()
    }
}