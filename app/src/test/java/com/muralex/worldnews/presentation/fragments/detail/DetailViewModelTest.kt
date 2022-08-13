package com.muralex.worldnews.presentation.fragments.detail

import androidx.lifecycle.Observer
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.domain.usecase.favorites.AddFavoriteUseCase
import com.muralex.worldnews.domain.usecase.favorites.CheckFavoriteUseCase
import com.muralex.worldnews.domain.usecase.favorites.DeleteFavoriteUseCase
import com.muralex.worldnews.domain.usecase.news.GetNewsUseCase
import com.muralex.worldnews.domain.usecase.news.UpdateNewsUseCase
import com.muralex.worldnews.presentation.fragments.home.HomeViewModel
import com.muralex.worldnews.presentation.fragments.home.HomeViewModelTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.*
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class DetailViewModelTest  {

    private lateinit var SUT: DetailViewModel

    private val addFavoriteUseCase: AddFavoriteUseCase = mockk()
    private val deleteFavoriteUseCase: DeleteFavoriteUseCase = mockk()
    private val checkFavoriteUseCase: CheckFavoriteUseCase = mockk()
    private val testArticle: Article = mockk()
    private val expected = true

    private fun mockSuccessfulCase() {
        coEvery { checkFavoriteUseCase.invoke(any()) } returns flowOf(expected)
        coEvery { deleteFavoriteUseCase.invoke(any()) } returns Unit
        coEvery { addFavoriteUseCase.invoke(any()) } returns Unit
        SUT = DetailViewModel(addFavoriteUseCase, deleteFavoriteUseCase, checkFavoriteUseCase)
    }

    @Test
    fun checkFavorite_invoke_checkFavoriteUseCase() {
        mockSuccessfulCase()
        SUT.checkFavorite(testArticle)
        coVerify { checkFavoriteUseCase.invoke(testArticle) }
    }

    @Test
    fun addToFavorite_invoke_addFavoriteUseCase() {
        mockSuccessfulCase()
        SUT.addToFavorite(testArticle)
        coVerify { addFavoriteUseCase.invoke(testArticle) }
    }

    @Test
    fun removeFromFavorite_invoke_deleteFavoriteUseCase() {
        mockSuccessfulCase()
        SUT.removeFromFavorite(testArticle)
        coVerify { deleteFavoriteUseCase.invoke(testArticle) }
    }


}

