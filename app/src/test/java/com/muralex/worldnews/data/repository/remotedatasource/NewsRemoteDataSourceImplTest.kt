package com.muralex.worldnews.data.repository.remotedatasource

import com.google.common.truth.Truth.assertThat
import com.muralex.achiever.utilities.BaseUnitTest
import com.muralex.worldnews.app.utils.NetworkHelper
import com.muralex.worldnews.app.utils.SettingsHelper
import com.muralex.worldnews.data.api.NewsApiService
import com.muralex.worldnews.data.model.api.NewsApiResponse
import com.muralex.worldnews.data.model.api.NewsMapper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class NewsRemoteDataSourceImplTest : BaseUnitTest() {

    private lateinit var SUT: NewsRemoteDataSourceImpl
    private val api: NewsApiService = mockk()
    private val mapper: NewsMapper = mockk()
    private val settingsHelper: SettingsHelper = mockk()
    private val networkHelper: NetworkHelper = mockk()


    @Test
    fun getNews_invoke_settingsHelper_GetSelectedCountry() = runTest {
        mockSuccess()
        SUT.getNewsDataFromApi()
        coVerify { settingsHelper.getSelectedCountry() }
    }

    @Test
    fun getNews_invoke_networkHelper_isNetworkConnected() = runTest {
        mockSuccess()
        SUT.getNewsDataFromApi()
        coVerify { networkHelper.isNetworkConnected() }
    }

    @Test
    fun getNews_invoke_apiService_getHeadlines() = runTest {
        mockSuccess()
        SUT.getNewsDataFromApi()
        coVerify { api.getHeadlines(any()) }
    }

    @Test
    fun getNews_successResponse_mapperMapFromEntityList() = runTest {
        mockSuccess()
        SUT.getNewsDataFromApi()
        coVerify { mapper.mapFromEntityList(any()) }
    }

    @Test
    fun getNews_successResponse_getResourceSuccess() = runTest {
        mockSuccess()
        val result = SUT.getNewsDataFromApi()
        assertThat(result.isSuccess()).isTrue()
    }

    @Test
    fun getNews_responseError_isError() = runTest {
        mockError()
        val result = SUT.getNewsDataFromApi()
        assertThat(result.isError()).isTrue()
    }

    @Test
    fun getNews_responseException_isError() = runTest {
        mockException()
        val result = SUT.getNewsDataFromApi()
        assertThat(result.isError()).isTrue()
    }

    @Test
    fun getNews_noConnection_isError() = runTest {
        mockNoConnection()
        val result = SUT.getNewsDataFromApi()
        assertThat(result.isError()).isTrue()
    }

    private fun mockNoConnection() = runTest {
        coEvery { networkHelper.isNetworkConnected() } returns false
        coEvery { settingsHelper.getSelectedCountry() } returns testCountry
        SUT = NewsRemoteDataSourceImpl(api, mapper, settingsHelper, networkHelper)
    }

    private fun mockSettingsAndNetworkHelpers() = runTest {
        coEvery { networkHelper.isNetworkConnected() } returns true
        coEvery { settingsHelper.getSelectedCountry() } returns testCountry
    }

    private fun mockSuccess() = runTest {
        mockSettingsAndNetworkHelpers()
        coEvery { api.getHeadlines(testCountry) } returns testResponse
        coEvery { mapper.mapFromEntityList(any()) } returns emptyList()
        SUT = NewsRemoteDataSourceImpl(api, mapper, settingsHelper, networkHelper)
    }

    private fun mockException() = runTest {
        mockSettingsAndNetworkHelpers()
        coEvery { api.getHeadlines(testCountry) } throws Exception()
        SUT = NewsRemoteDataSourceImpl(api, mapper, settingsHelper, networkHelper)
    }

    private fun mockError() = runTest {
        mockSettingsAndNetworkHelpers()
        coEvery { api.getHeadlines(testCountry) } returns testErrorResponse
        SUT = NewsRemoteDataSourceImpl(api, mapper, settingsHelper, networkHelper)
    }

    companion object {
        private const val testCountry = "ca"
        private val testApiResponse = NewsApiResponse(emptyList(), "", 0)
        private val testResponse: Response<NewsApiResponse> = Response.success(testApiResponse)
        private val testErrorResponse: Response<NewsApiResponse> = mockk()
    }


}