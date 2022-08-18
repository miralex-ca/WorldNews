package com.muralex.worldnews.presentation.fragments.home

import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.presentation.utils.UiEffect
import com.muralex.worldnews.presentation.utils.UiEvent
import com.muralex.worldnews.presentation.utils.UiIntent
import com.muralex.worldnews.presentation.utils.UiState

class HomeContract {

    sealed class UserAction : UiEvent {
        object LaunchScreen : UserAction()
        object RefreshFromMenu : UserAction()
        object OpenCountryDialogFromMenu : UserAction()
        object CountrySelectedFromDialog : UserAction()
        object SwipeRefreshPulled : UserAction()
        data class ListItemClick(val article: Article) : UserAction()
    }

    companion object {
        fun intentToAction(intent: ViewIntent): ModelAction {
            return when (intent) {
                is ViewIntent.GetNews -> ModelAction.GetNews(intent.country)
                is ViewIntent.UpdateNews -> ModelAction.UpdateNews
            }
        }
    }

    sealed class ViewIntent : UiIntent {
        object UpdateNews : ViewIntent()
        data class GetNews(val country: String) : ViewIntent()
    }

    sealed class ModelAction {
        object UpdateNews : ModelAction()
        data class GetNews(val country: String) : ModelAction()
    }

    sealed class ViewState : UiState {
        object Loading : ViewState()
        object EmptyList : ViewState()
        data class ListLoaded(val data: Resource<List<Article>>) : ViewState()
        data class ListRefreshed(val data: Resource<List<Article>>) : ViewState()
        data class ListLoadFailure(val data: Resource<List<Article>>) : ViewState()
    }

    sealed class ViewEffect : UiEffect {
        data class ShowErrorSnackBar(val message: String) : ViewEffect()
        object ShowLoading : ViewEffect()
    }

}