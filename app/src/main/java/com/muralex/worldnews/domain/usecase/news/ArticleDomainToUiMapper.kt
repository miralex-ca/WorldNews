package com.muralex.worldnews.domain.usecase.news

import com.muralex.worldnews.app.data.EntityMapper
import com.muralex.worldnews.app.data.Resource
import com.muralex.worldnews.app.data.ResourceInfo
import com.muralex.worldnews.app.utils.Constants.DataErrors
import com.muralex.worldnews.app.utils.Constants.DATA_FETCH_ERROR
import com.muralex.worldnews.data.model.app.Article
import javax.inject.Inject

class ArticleDomainToUiMapper @Inject constructor(): EntityMapper<Resource<List<Article>>, Resource<List<Article>>> {

    override fun mapFromEntity(data: Resource<List<Article>>): Resource<List<Article>> {
       return if (data.isError()) setErrorMessage(data)
       else data
    }

    private fun setErrorMessage(data: Resource<List<Article>>) : Resource<List<Article>> {
        val errorType = when (data.getInfo()) {
            is ResourceInfo.ConnectionError -> DataErrors.CONNECTION
            is ResourceInfo.RequestError -> DataErrors.REQUEST
            is ResourceInfo.ErrorException -> DataErrors.SERVER
            else -> DataErrors.GENERIC
        }

        return Resource.error(DATA_FETCH_ERROR, data = data.data).apply {
            setInfo(ResourceInfo.ErrorType(errorType))
        }
    }
}