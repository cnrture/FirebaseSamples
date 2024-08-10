package com.canerture.firebasesamples.ui.main

object MainContract {
    data class UiState(
        val isLoading: Boolean = false,
    )

    sealed class UiAction {
        data object SignOutClicked : UiAction()
    }

    sealed class UiEffect {
        data object GoToLoginScreen : UiEffect()
    }
}