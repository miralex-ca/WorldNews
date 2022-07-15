package com.muralex.worldnews.presentation.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.data.model.utils.Resource
import com.muralex.worldnews.data.model.utils.Status
import com.muralex.worldnews.domain.usecase.GetNewsUseCase
import com.muralex.worldnews.domain.usecase.UpdateNewsUseCase
import com.muralex.worldnews.presentation.utils.Constants.DEFAULT_COUNTRY
import com.muralex.worldnews.presentation.utils.NetworkHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getNewsUseCase: GetNewsUseCase,
    private val updateNewsUseCase: UpdateNewsUseCase,
    private val networkHelper: NetworkHelper
        ): ViewModel() {

    private var country = DEFAULT_COUNTRY
    val viewState = MutableLiveData<ViewState>()

    fun getNews(country: String) {
        var type = GET_TYPE
        if (country != this.country) {
            type = UPDATE_TYPE
            setNewsCountry(country)
        }
        getData(type)

    }

    fun updateNews() = getData( UPDATE_TYPE)

    private fun getData(type: Int) = viewModelScope.launch(Dispatchers.IO) {
        viewState.postValue(ViewState.Loading)
        try{
            if (networkHelper.isNetworkConnected()) {
                val response = getDataFromUseCase(type)
                when (response.status) {
                    Status.SUCCESS -> viewState.postValue(ViewState.ListLoaded(response))
                    Status.ERROR -> {
                        val message =  response.message ?: "Error"
                        viewState.postValue(ViewState.ListLoadFailure(message) )
                    }
                    else -> {
                        viewState.postValue(ViewState.Initial)
                    }
                }
            }else{
                viewState.postValue(ViewState.ListLoadFailure("No internet connection") )
            }
        }catch (e: Exception){
            viewState.postValue(ViewState.ListLoadFailure(e.message.toString()) )
        }
    }

    fun setNewsCountry(country: String) {
        this.country = country
    }


    private suspend fun getDataFromUseCase(type: Int): Resource<List<Article>> {
        return  if (type == UPDATE_TYPE) updateNewsUseCase()
        else getNewsUseCase()
    }

    private companion object {
        const val UPDATE_TYPE = 1
        const val GET_TYPE = 2
    }

    sealed class ViewState {
        object Initial: ViewState()
        object Loading: ViewState()
        data class ListLoaded(val data: Resource<List<Article>>): ViewState()
        data class ListLoadFailure(val errorMessage: String): ViewState()
    }
}