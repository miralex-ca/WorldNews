package com.muralex.worldnews.app.utils

object Constants {
    const val ARTICLE_URL = "article_url"
    const val ARTICLE_EXTRA = "selected_article"
    const val CONTACT_EMAIL = "study.languages.online@gmail.com"
    const val NEWS_LIST_SIZE = 50

    const val DATA_FETCH_ERROR = "unknown_error"

    enum class Action {Click, LongClick}

    enum class DataErrors {CONNECTION, SERVER, REQUEST, GENERIC}

}