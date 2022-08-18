package com.muralex.worldnews.presentation.fragments.detail

import com.muralex.worldnews.data.model.app.Article
import com.muralex.worldnews.presentation.utils.UiEvent

class DetailContract {

    sealed class UserAction: UiEvent {
        data class LaunchScreen(val article: Article): UserAction()
        data class BookmarkClicked(val article: Article): UserAction()
        data class ShareMenuMenuItemSelected(val article: Article): UserAction()
        data class OpenSourceMenuItemSelected(val article: Article): UserAction()
        data class OpenSourceButtonClicked(val article: Article): UserAction()
    }

}