package com.muralex.worldnews.presentation.fragments.bookmarks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.worldnews.app.data.Status
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.usecase.favorites.DeleteFavoriteUseCase
import com.muralex.worldnews.domain.usecase.favorites.GetFavoriteUseCase
import com.muralex.worldnews.presentation.fragments.bookmarks.BookmarksContract.ModelAction
import com.muralex.worldnews.presentation.fragments.bookmarks.BookmarksContract.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
    get() = _viewState

    fun setIntent(intent: BookmarksContract.ViewIntent) {
        val action = BookmarksContract.intentToAction(intent)
        handleModelAction(action)
    }

    private fun handleModelAction(modelAction: ModelAction) {
        when (modelAction) {
            is ModelAction.GetFavoriteNews -> getFavoriteNews()
            is ModelAction.RemoveFavorite -> removeFavorite(modelAction.article)
        }
    }

    private fun getFavoriteNews() = viewModelScope.launch(Dispatchers.IO) {
        getFavoriteUseCase().collect{ response ->
            when (response.status) {
                Status.LOADING -> {}
                Status.ERROR -> _viewState.postValue(ViewState.ListLoadFailure(response))
                Status.SUCCESS -> {
                    if (response.data?.isEmpty() == true)  _viewState.postValue(ViewState.EmptyList)
                    else _viewState.postValue(ViewState.ListLoaded(response))
                }
            }
        }
    }

    private fun removeFavorite(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        deleteFavoriteUseCase(article)
    }

}