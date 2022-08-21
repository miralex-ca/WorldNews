package com.muralex.worldnews.presentation.fragments.bookmarks

import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.presentation.utils.UiEffect
import com.muralex.worldnews.presentation.utils.UiEvent
import com.muralex.worldnews.presentation.utils.UiIntent
import com.muralex.worldnews.presentation.utils.UiState

class BookmarksContract {

    sealed class UserAction : UiEvent {
        object LaunchScreen : UserAction()
        data class ListItemSwiped(val article: Article) : UserAction()
        data class ListItemClicked(val article: Article) : UserAction()
    }

    companion object {
        fun intentToAction(intent: ViewIntent): ModelAction {
            return when (intent) {
                is ViewIntent.RemoveFavorite -> ModelAction.RemoveFavorite(intent.article)
                is ViewIntent.GetFavoriteNews -> ModelAction.GetFavoriteNews
            }
        }
    }

    sealed class ViewIntent : UiIntent {
        data class RemoveFavorite(val article: Article) : ViewIntent()
        object GetFavoriteNews : ViewIntent()
    }

    sealed class ModelAction {
        data class RemoveFavorite(val article: Article) : ModelAction()
        object GetFavoriteNews : ModelAction()
    }

    sealed class ViewState : UiState {
        object EmptyList : ViewState()
        data class ListLoaded(val data: Resource<List<Article>>) : ViewState()
        data class ListLoadFailure(val data: Resource<List<Article>>) : ViewState()
    }

}