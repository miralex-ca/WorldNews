package com.muralex.worldnews.data.model.utils

interface EntityMapper<SRC, DST> {
    fun mapFromEntity(data: SRC): DST
}