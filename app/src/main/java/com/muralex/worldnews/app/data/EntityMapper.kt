package com.muralex.worldnews.app.data

interface EntityMapper<SRC, DST> {
    fun mapFromEntity(data: SRC): DST
}