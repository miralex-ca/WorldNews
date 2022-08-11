package com.muralex.worldnews.presentation.fragments.bookmarks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.ListItemFavoriteBinding

import com.muralex.worldnews.app.utils.Constants.Action
import com.muralex.worldnews.presentation.utils.setListItemImage

class BookmarksListAdapter : ListAdapter<Article, BookmarksListAdapter.ViewHolder>(ListDiffCallBack()) {
    private var onItemClickListener : ( (Action, Article) -> Unit )?= null

    fun setOnItemClickListener (listener:  (Action, Article) -> Unit ) {
        onItemClickListener = listener
    }

    class ViewHolder (private val binding: ListItemFavoriteBinding) : RecyclerView.ViewHolder (binding.root) {

        val viewForeground = binding.viewForeground
        val infoSwipeLeft = binding.bgArchiveLeft
        val infoSwipeRight = binding.bgArchiveRight

        fun bind(item: Article, onItemClickListener: ((Action, Article) -> Unit)?) {

            binding.apply {

                ivListImage.setListItemImage(item.image)
                tvTitle.text = item.title
                tvDesc.text = item.description
                tvPublished.text = item.publishedAt
                tvPublishedInfo.text = item.source

                onItemClickListener?.let { clicker ->
                    cardWrap.setOnClickListener {
                        clicker(Action.Click, item)
                    }
                }
            }
        }

        companion object {
            fun from (parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemFavoriteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClickListener )
    }

    class ListDiffCallBack : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }
    }


}


