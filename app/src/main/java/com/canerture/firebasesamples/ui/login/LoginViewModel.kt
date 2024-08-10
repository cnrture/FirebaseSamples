package com.canerture.firebasesamples.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.firebasesamples.common.Resource
import com.canerture.firebasesamples.data.repository.AuthRepository
import com.canerture.firebasesamples.ui.login.LoginContract.UiAction
import com.canerture.firebasesamples.ui.login.LoginContract.UiEffect
import com.canerture.firebasesamples.ui.login.LoginContract.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    private var verificationId: String = ""

    init {
        isUserLoggedIn()
    }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            is UiAction.SignInClick -> signIn()
            is UiAction.SignUpClick -> signUp()
            is UiAction.ChangeEmail -> updateUiState { copy(email = uiAction.email) }
            is UiAction.ChangePassword -> updateUiState { copy(password = uiAction.password) }
            is UiAction.ChangePhoneNumber -> updateUiState { copy(phoneNumber = uiAction.phoneNumber) }
            is UiAction.ChangeVerifyCode -> updateUiState { copy(verifyCode = uiAction.verifyCode) }
            is UiAction.SendCodeClick -> sendCode()
            is UiAction.VerifyCodeClick -> verifyCode()
            is UiAction.AnonymousClick -> anonymousSignIn()
        }
    }

    private fun isUserLoggedIn() = viewModelScope.launch {
        if (authRepository.isUserLoggedIn()) {
            emitUiEffect(UiEffect.GoToMainScreen)
        }
    }

    private fun signUp() = viewModelScope.launch {
        when (val result = authRepository.signUp(uiState.value.email, uiState.value.password)) {
            is Resource.Success -> {
                emitUiEffect(UiEffect.ShowToast(result.data))
                emitUiEffect(UiEffect.GoToMainScreen)
            }

            is Resource.Error -> {
                emitUiEffect(UiEffect.ShowToast(result.exception.message.orEmpty()))
            }
        }
    }

    private fun signIn() = viewModelScope.launch {
        when (val result = authRepository.signIn(uiState.value.email, uiState.value.password)) {
            is Resource.Success -> {
                emitUiEffect(UiEffect.ShowToast(result.data))
                emitUiEffect(UiEffect.GoToMainScreen)
            }

            is Resource.Error -> {
                emitUiEffect(UiEffect.ShowToast(result.exception.message.orEmpty()))
            }
        }
    }

    private fun sendCode() = viewModelScope.launch {
        authRepository.sendVerificationCode(uiState.value.phoneNumber).collect { result ->
            when (result) {
                is Resource.Success -> {
                    if (result.data.isEmpty()) {
                        emitUiEffect(UiEffect.ShowToast("Success"))
                    } else {
                        verificationId = result.data
                        emitUiEffect(UiEffect.ShowToast("Code sent"))
                    }
                }

                is Resource.Error -> {
                    emitUiEffect(UiEffect.ShowToast(result.exception.message.orEmpty()))
                }
            }
        }
    }

    private fun verifyCode() = viewModelScope.launch {
        when (val result = authRepository.verifyCode(verificationId, uiState.value.verifyCode)) {
            is Resource.Success -> {
                emitUiEffect(UiEffect.ShowToast(result.data))
                emitUiEffect(UiEffect.GoToMainScreen)
            }

            is Resource.Error -> {
                emitUiEffect(UiEffect.ShowToast(result.exception.message.orEmpty()))
            }
        }
    }

    private fun anonymousSignIn() = viewModelScope.launch {
        when (val result = authRepository.signInAnonymously()) {
            is Resource.Success -> {
                emitUiEffect(UiEffect.ShowToast(result.data))
                emitUiEffect(UiEffect.GoToMainScreen)
            }

            is Resource.Error -> {
                emitUiEffect(UiEffect.ShowToast(result.exception.message.orEmpty()))
            }
        }
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}

