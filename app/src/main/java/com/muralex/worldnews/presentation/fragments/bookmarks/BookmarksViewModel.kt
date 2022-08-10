package com.muralex.worldnews.presentation.fragments.bookmarks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.app.data.Status
import com.muralex.worldnews.domain.usecase.favorites.DeleteFavoriteUseCase
import com.muralex.worldnews.domain.usecase.favorites.GetFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val getFavoriteUseCase: GetFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase
) : ViewModel() {

    val viewState = MutableLiveData<ViewState>()

    fun removeFavorite(article: Article)  = viewModelScope.launch(Dispatchers.IO) {
        deleteFavoriteUseCase(article)
    }

    fun getNews() = viewModelScope.launch(Dispatchers.IO) {
        viewState.postValue(ViewState.Loading)
        getFavoriteUseCase().collect{ response ->
            when (response.status) {
                Status.LOADING -> viewState.postValue(ViewState.Loading)
                Status.ERROR -> viewState.postValue(ViewState.ListLoadFailure(response))
                Status.SUCCESS -> viewState.postValue(ViewState.ListLoaded(response))
            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class ListLoaded(val data: Resource<List<Article>>) : ViewState()
        data class ListLoadFailure(val data: Resource<List<Article>>) : ViewState()
    }

}