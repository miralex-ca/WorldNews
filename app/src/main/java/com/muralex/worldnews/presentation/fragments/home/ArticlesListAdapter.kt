package com.muralex.worldnews.presentation.fragments.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.utils.Constants.Action
import com.muralex.worldnews.databinding.ListItemArticleBinding
import com.muralex.worldnews.presentation.utils.setListItemImage

class ArticlesListAdapter : ListAdapter<Article, ArticlesListAdapter.ViewHolder>(ListDiffCallBack()) {
    private var onItemClickListener : ( (Action, Article) -> Unit )?= null

    fun setOnItemClickListener (listener:  (Action, Article) -> Unit ) {
        onItemClickListener = listener
    }

    class ViewHolder (val binding: ListItemArticleBinding) : RecyclerView.ViewHolder (binding.root) {
        fun bind(item: Article, onItemClickListener: ((Action, Article) -> Unit)?) {

            binding.apply {
                tvTitle.text = item.title
                tvPublished.text = item.publishedAt
                tvPublishedInfo.text = item.source
                ivListImage.setListItemImage(item.image)

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
                val binding = ListItemArticleBinding.inflate(layoutInflater, parent, false)
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
}

class ListDiffCallBack : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return false
    }
    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }
}
