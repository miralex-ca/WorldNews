package com.muralex.worldnews.presentation.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.databinding.ArticleListitemBinding
import com.muralex.worldnews.presentation.utils.Constants.Action
import com.muralex.worldnews.presentation.utils.setListItemImage

class ArticlesListAdapter : ListAdapter<Article, ArticlesListAdapter.ViewHolder>(ListDiffCallBack()) {
    private var onItemClickListener : ( (Action, Article) -> Unit )?= null

    fun setOnItemClickListener (listener:  (Action, Article) -> Unit ) {
        onItemClickListener = listener
    }

    class ViewHolder (private val binding: ArticleListitemBinding) : RecyclerView.ViewHolder (binding.root) {
        fun bind(item: Article, onItemClickListener: ((Action, Article) -> Unit)?) {

            if (item.image.isEmpty()) binding.ivListImage.visibility = View.GONE

            binding.apply {

                if (item.description.isEmpty()) tvDesc.text = item.author
                tvTitle.text = item.title
                tvDesc.text = item.description
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
                val binding = ArticleListitemBinding.inflate(layoutInflater, parent, false)
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
        return false
    }
}
