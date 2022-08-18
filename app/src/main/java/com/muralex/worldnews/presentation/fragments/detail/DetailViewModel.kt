package com.muralex.worldnews.presentation.fragments.detail

import androidx.lifecycle.*
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.usecase.favorites.AddFavoriteUseCase
import com.muralex.worldnews.domain.usecase.favorites.CheckFavoriteUseCase
import com.muralex.worldnews.domain.usecase.favorites.DeleteFavoriteUseCase
import com.muralex.worldnews.presentation.fragments.home.HomeContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase,
    private val checkFavoriteUseCase: CheckFavoriteUseCase,
) : ViewModel() {

    private val _favorite = MutableLiveData(false)
    val favorite: LiveData<Boolean>
        get() = _favorite

    fun checkFavorite(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        checkFavoriteUseCase(article).collect {
            _favorite.postValue(it)
        }
    }

    fun addToFavorite(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        addFavoriteUseCase(article)
    }

    fun removeFromFavorite(article: Article) = viewModelScope.launch(Dispatchers.IO) {
        deleteFavoriteUseCase(article)
    }

}