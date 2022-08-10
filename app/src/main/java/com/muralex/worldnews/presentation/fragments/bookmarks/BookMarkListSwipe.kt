package com.muralex.worldnews.presentation.fragments.bookmarks

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.data.model.app.Article

class BookMarkListSwipe  {
    companion object {

        fun setup(
            listAdapter: BookmarksListAdapter,
            callback: (Article) -> Unit
        ) =  object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder,
                ): Boolean {
                    return true
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val layoutPosition = viewHolder.layoutPosition
                    val article = listAdapter.currentList[layoutPosition]
                    callback(article)
                }

                override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                    if (viewHolder != null) {
                        val foregroundView: View =
                            (viewHolder as BookmarksListAdapter.ViewHolder).viewForeground
                        getDefaultUIUtil().onSelected(foregroundView)
                    }
                }

                override fun onChildDrawOver(
                    c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder?,
                    dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean,
                ) {
                    val foregroundView: View =
                        (viewHolder as BookmarksListAdapter.ViewHolder).viewForeground
                    getDefaultUIUtil().onDrawOver(
                        c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive
                    )
                }

                override fun clearView(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                ) {
                    val foregroundView: View =
                        (viewHolder as BookmarksListAdapter.ViewHolder).viewForeground
                    getDefaultUIUtil().clearView(foregroundView)
                }

                override fun onChildDraw(
                    c: Canvas,
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    dX: Float,
                    dY: Float,
                    actionState: Int,
                    isCurrentlyActive: Boolean,
                ) {

                    val infoSwipeLeft: View =
                        (viewHolder as BookmarksListAdapter.ViewHolder).infoSwipeLeft
                    val infoSwipeRight: View = viewHolder.infoSwipeRight

                    if (dX < 0) {
                        infoSwipeLeft.visibility = View.GONE
                        infoSwipeRight.visibility = View.VISIBLE
                    } else {
                        infoSwipeLeft.visibility = View.VISIBLE
                        infoSwipeRight.visibility = View.GONE
                    }

                    val foregroundView: View = viewHolder.viewForeground

                    getDefaultUIUtil().onDraw(
                        c, recyclerView, foregroundView, dX, dY,
                        actionState, isCurrentlyActive
                    )
                }
            }
    }
}