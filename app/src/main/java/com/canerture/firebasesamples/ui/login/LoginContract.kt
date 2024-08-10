package com.canerture.firebasesamples.ui.login

object LoginContract {
    data class UiState(
        val isLoading: Boolean = false,
        val email: String = "",
        val password: String = "",
        val phoneNumber: String = "",
        val verifyCode: String = "",
    )

    sealed class UiAction {
        data object SignInClick : UiAction()
        data object SignUpClick : UiAction()
        data class ChangeEmail(val email: String) : UiAction()
        data class ChangePassword(val password: String) : UiAction()
        data class ChangePhoneNumber(val phoneNumber: String) : UiAction()
        data class ChangeVerifyCode(val verifyCode: String) : UiAction()
        data object SendCodeClick : UiAction()
        data object VerifyCodeClick : UiAction()
        data object AnonymousClick : UiAction()
    }

    sealed class UiEffect {
        data class ShowToast(val message: String) : UiEffect()
        data object GoToMainScreen : UiEffect()
    }
}