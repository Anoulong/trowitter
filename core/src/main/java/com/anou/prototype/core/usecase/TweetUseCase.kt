package com.anou.prototype.core.usecase

import com.anou.prototype.core.db.tweet.TweetEntity

sealed class TweetUseCase {
    class SetData(val tweets : MutableList<TweetEntity>): TweetUseCase()
    class InitializeModule(val tweet : TweetEntity): TweetUseCase()
    class ShowError(val errorMessage : String): TweetUseCase()
    class ShowSuccess(val successMessage : String): TweetUseCase()
    class ShowEmpty(val emptyMessage : String): TweetUseCase()
    object ShowLoading: TweetUseCase()
    object HideLoading: TweetUseCase()

}