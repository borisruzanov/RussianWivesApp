package com.borisruzanov.russianwives.mvp.ui.search.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.util.Log


abstract class FeedScrollListener: RecyclerView.OnScrollListener {
    // The minimum amount of items to have below your current scroll position before isLoading more.
    /**@important make sure that ITEMS_PER_PAGE is enough to have 3 rows below
     * otherwise it will never load the second page*/
    private var visibleThreshold = 3
    // The current offset index of data you have loaded
    private var currentPage = 0
    // The total number of items in the dataset after the last load
    private var previousTotalItemCount = 0
    private var isLoading = true
    //!important! when using FeedScrollListener we need manually tell it about the end of the list
    var isStoppedLoading = false

    private var mLayoutManager: RecyclerView.LayoutManager? = null

    constructor(layoutManager: LinearLayoutManager) {
        this.mLayoutManager = layoutManager
    }

    constructor(layoutManager: GridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    constructor(layoutManager: StaggeredGridLayoutManager) {
        this.mLayoutManager = layoutManager
        visibleThreshold *= layoutManager.spanCount
    }

    fun setStopLoading(isStopLoading: Boolean) {
        isStoppedLoading = isStopLoading
    }


    override fun onScrolled(view: RecyclerView, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0
        val totalItemCount = mLayoutManager!!.itemCount

        when (mLayoutManager) {
            is GridLayoutManager -> lastVisibleItemPosition = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        }

        // If it’s still isLoading, we check to see if the dataset count has
        // changed, if so we conclude it has finished isLoading and update the current page
        // number and total item count.

        // If it isn’t currently isLoading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        // threshold should reflect how many total columns there are too

        // If it’s still isLoading, we check to see if the dataset count has
        // changed, if so we conclude it has finished isLoading and update the current page
        // number and total item count.
        if (isLoading && totalItemCount > previousTotalItemCount) {
            isLoading = false
            previousTotalItemCount = totalItemCount
        }

        // If it isn’t currently isLoading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        if (!isLoading && lastVisibleItemPosition + visibleThreshold > totalItemCount
                && view.adapter!!.itemCount > visibleThreshold
                && !isStoppedLoading) {// This condition will useful when recyclerview has less than visibleThreshold items
            currentPage++
            onLoadMore(currentPage, totalItemCount, view)
            isLoading = true
        }
    }

    // Call whenever performing new searches
    fun resetState() {
        this.previousTotalItemCount = 0
        this.isLoading = true
        isStoppedLoading = false
    }

    // Defines the process for actually isLoading more data based on page
    abstract fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView)

}