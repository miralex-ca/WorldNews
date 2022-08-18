package com.muralex.worldnews.presentation.fragments.home

import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.muralex.achiever.utilities.BaseUnitTest
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.domain.usecase.news.GetNewsUseCase
import com.muralex.worldnews.domain.usecase.news.UpdateNewsUseCase
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class HomeViewModelTest : BaseUnitTest() {

    private lateinit var SUT: HomeViewModel
    private val articlesList = mock<List<Article>>()
    private val expected = Resource.success(articlesList)
    private val getNewsUseCase = mockk<GetNewsUseCase>()
    private val updateNewsUseCase: UpdateNewsUseCase = mockk()
    private val expectedError = Resource.error("msg", articlesList)
    private val observer =
        mockk<Observer<HomeContract.ViewState>> { every { onChanged(any()) } just Runs }

    @Test
    fun getNews_getUseCaseInvoke() {
        mockSuccessfulCase()
        SUT.setIntent(HomeContract.ViewIntent.GetNews(COUNTRY_CODE))
        coVerify { getNewsUseCase.invoke() }
    }

    @Test
    fun update_updateUseCaseInvoke() {
        mockSuccessfulCase()
        SUT.setIntent(HomeContract.ViewIntent.UpdateNews)
        coVerify { updateNewsUseCase() }
    }

    @Test
    fun startRefresh_onStart_isFalse() {
        mockSuccessfulCase()
        assertThat(SUT.startRefresh).isTrue()
    }

    @Test
    fun startRefresh_afterGetNews_isFalse() {
        mockSuccessfulCase()
        SUT.setIntent(HomeContract.ViewIntent.GetNews(COUNTRY_CODE))
        assertThat(SUT.startRefresh).isFalse()
    }

    @Test
    fun startRefresh_afterUpdateNews_isFalse() {
        mockSuccessfulCase()
        SUT.setIntent(HomeContract.ViewIntent.UpdateNews)
        assertThat(SUT.startRefresh).isFalse()
    }

    @Test
    fun differentCountry_updateUseCaseInvoke() {
        mockSuccessfulCase()
        SUT.setNewsCountry("test")
        SUT.setIntent(HomeContract.ViewIntent.GetNews(COUNTRY_CODE))
        coVerify { updateNewsUseCase() }
    }

    @Test
    fun getNews_success_ViewStateListLoaded() {
        mockSuccessfulCase()
        SUT.viewState.observeForever(observer)
        SUT.setIntent(HomeContract.ViewIntent.GetNews(COUNTRY_CODE))

        verifySequence {
            observer.onChanged(HomeContract.ViewState.Loading)
            observer.onChanged(HomeContract.ViewState.ListLoaded(expected))
        }
    }

    @Test
    fun updateNews_success_ViewStateListRefreshed() {
        mockSuccessfulCase()
        SUT.viewState.observeForever(observer)
        SUT.setIntent(HomeContract.ViewIntent.UpdateNews)

        verifySequence {
            observer.onChanged(HomeContract.ViewState.Loading)
            observer.onChanged(HomeContract.ViewState.ListRefreshed(expected))
        }
    }

    @Test
    fun getNews_error_ViewStateListLoadFailure() {
        mockErrorCase()
        SUT.viewState.observeForever(observer)
        SUT.setIntent(HomeContract.ViewIntent.GetNews(COUNTRY_CODE))

        verifySequence {
            observer.onChanged(HomeContract.ViewState.Loading)
            observer.onChanged(HomeContract.ViewState.ListLoadFailure(expectedError))
        }
    }

    @Test
    fun updateNews_error_ViewStateListLoadFailure() {
        mockErrorCase()
        SUT.viewState.observeForever(observer)
        SUT.setIntent(HomeContract.ViewIntent.UpdateNews)

        verifySequence {
            observer.onChanged(HomeContract.ViewState.Loading)
            observer.onChanged(HomeContract.ViewState.ListLoadFailure(expectedError))
        }
    }

    private fun mockSuccessfulCase() {
        coEvery { getNewsUseCase.invoke() } returns expected
        coEvery { updateNewsUseCase() } returns expected
        SUT = HomeViewModel(getNewsUseCase, updateNewsUseCase)
    }

    private fun mockErrorCase() {
        coEvery { getNewsUseCase() } returns expectedError
        coEvery { updateNewsUseCase() } returns expectedError
        SUT = HomeViewModel(getNewsUseCase, updateNewsUseCase)
    }

    companion object {
        const val COUNTRY_CODE = "ca"
    }

}