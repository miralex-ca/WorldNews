package com.muralex.worldnews.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.data.model.utils.Status
import com.muralex.worldnews.domain.usecase.news.GetNewsUseCase
import com.muralex.worldnews.domain.usecase.news.UpdateNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val updateNewsUseCase: UpdateNewsUseCase,
) : ViewModel() {

    private var country = COUNTRY_START

    private var _startRefresh = true
    val startRefresh: Boolean
        get() = _startRefresh

    val viewState = MutableLiveData<ViewState>()

    fun getNews(country: String) {
        val fetchDataType = if (isDifferentCountry(country)) UPDATE_TYPE
        else GET_TYPE
        setNewsCountry(country)
        getData(fetchDataType)
    }

    private fun isDifferentCountry(country: String): Boolean {
        return if (this.country == COUNTRY_START) false
        else country != this.country
    }

    fun updateNews() = getData(UPDATE_TYPE)

    private fun getData(type: Int) = viewModelScope.launch(Dispatchers.IO) {
        _startRefresh = false
        viewState.postValue(ViewState.Loading)
        try {
            val response = getDataFromUseCase(type)

            when (response.status) {
                Status.LOADING -> viewState.postValue(ViewState.Loading)
                Status.ERROR -> viewState.postValue(ViewState.ListLoadFailure(response))
                Status.SUCCESS -> {
                    if (type == GET_TYPE) viewState.postValue(ViewState.ListLoaded(response))
                    else viewState.postValue(ViewState.ListRefreshed(response))
                }
            }
        } catch (e: Exception) {
            val resource = Resource.error(e.message.toString(), null)
            viewState.postValue(ViewState.ListLoadFailure(resource))
        }
    }

    fun setNewsCountry(country: String) {
        this.country = country
    }

    private suspend fun getDataFromUseCase(type: Int): Resource<List<Article>> {
        return if (type == UPDATE_TYPE) updateNewsUseCase()
        else getNewsUseCase()
    }

    private companion object {
        const val UPDATE_TYPE = 1
        const val GET_TYPE = 2
        const val COUNTRY_START = "unknown"
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class ListLoaded(val data: Resource<List<Article>>) : ViewState()
        data class ListRefreshed(val data: Resource<List<Article>>) : ViewState()
        data class ListLoadFailure(val data: Resource<List<Article>>) : ViewState()
    }

}