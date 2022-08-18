package com.muralex.worldnews.presentation.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.app.data.Status
import com.muralex.worldnews.domain.usecase.news.GetNewsUseCase
import com.muralex.worldnews.domain.usecase.news.UpdateNewsUseCase
import com.muralex.worldnews.presentation.fragments.home.HomeContract.ModelAction
import com.muralex.worldnews.presentation.fragments.home.HomeContract.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
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

    private val _viewState = MutableLiveData<ViewState>()
    val viewState: LiveData<ViewState>
        get() = _viewState

    private val _viewEffect : Channel<HomeContract.ViewEffect> = Channel()
    val viewEffect = _viewEffect.receiveAsFlow()

    fun setIntent(intent: HomeContract.ViewIntent) {
        val action = HomeContract.intentToAction(intent)
        handleModelAction(action)
    }

    private fun handleModelAction(modelAction: ModelAction) {
        when (modelAction) {
            is ModelAction.GetNews -> {
                val selectedCountry = modelAction.country
                val fetchDataType = if (isDifferentCountry(selectedCountry)) UPDATE_TYPE
                else GET_TYPE
                setNewsCountry(selectedCountry)
                getData(fetchDataType)
            }
            ModelAction.UpdateNews -> {
                getData(UPDATE_TYPE)
            }
        }
    }

    private fun getData(type: Int) {
        _startRefresh = false
        viewModelScope.launch(Dispatchers.IO) {
            _viewState.postValue(ViewState.Loading)

            try {
                val response = getDataFromUseCase(type)
                when (response.status) {
                    Status.LOADING -> _viewState.postValue(ViewState.Loading)
                    Status.ERROR -> {
                        _viewState.postValue(ViewState.ListLoadFailure(response))
                        setEffect(HomeContract.ViewEffect.ShowErrorSnackBar(response.message.toString()))
                    }
                    Status.SUCCESS -> {
                        if (type == GET_TYPE) _viewState.postValue(ViewState.ListLoaded(response))
                        else _viewState.postValue(ViewState.ListRefreshed(response))
                    }
                }
            } catch (e: Exception) {
                val resource = Resource.error(e.message.toString(), null)
                _viewState.postValue(ViewState.ListLoadFailure(resource))
                setEffect(HomeContract.ViewEffect.ShowErrorSnackBar(resource.message.toString()))
            }
        }
    }

    private suspend fun setEffect(effect: HomeContract.ViewEffect) {
        _viewEffect.send(effect)
    }

    fun setNewsCountry(country: String) {
        this.country = country
    }

    private suspend fun getDataFromUseCase(type: Int): Resource<List<Article>> {
        return if (type == UPDATE_TYPE) updateNewsUseCase()
        else getNewsUseCase()
    }

    private fun isDifferentCountry(country: String): Boolean {
        return if (this.country == COUNTRY_START) false
        else country != this.country
    }

    private companion object {
        const val UPDATE_TYPE = 1
        const val GET_TYPE = 2
        const val COUNTRY_START = "unknown"
    }

}

