package com.muralex.worldnews.app.data

import com.muralex.worldnews.app.utils.Constants

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message: String?
    ) {
    private var info: ResourceInfo? = null

    fun setInfo(info: ResourceInfo?) {
        this.info = info
    }

    fun getErrorType() : Constants.DataErrors {
        return when (info) {
            is ResourceInfo.ErrorType -> {
                (info as ResourceInfo.ErrorType).errorType
            }
            else -> Constants.DataErrors.GENERIC
        }
    }

    fun getInfo() = this.info

    fun isSuccess() = this.status == Status.SUCCESS

    fun isError() = this.status == Status.ERROR

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }


}

sealed class ResourceInfo {
    class ErrorType(val errorType: Constants.DataErrors) : ResourceInfo()
    object GenericError: ResourceInfo()
    object ConnectionError : ResourceInfo()
    object RequestError : ResourceInfo()
    class ErrorException (val info: Exception?) : ResourceInfo()
}



enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}