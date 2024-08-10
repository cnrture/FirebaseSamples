package com.canerture.firebasesamples.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.canerture.firebasesamples.data.repository.AuthRepository
import com.canerture.firebasesamples.ui.main.MainContract.UiAction
import com.canerture.firebasesamples.ui.main.MainContract.UiEffect
import com.canerture.firebasesamples.ui.main.MainContract.UiState
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
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEffect by lazy { Channel<UiEffect>() }
    val uiEffect: Flow<UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    fun onAction(uiAction: UiAction) {
        when (uiAction) {
            UiAction.SignOutClicked -> {
                signOut()
            }
        }
    }

    private fun signOut() = viewModelScope.launch {
        authRepository.signOut()
        emitUiEffect(UiEffect.GoToLoginScreen)
    }

    private fun updateUiState(block: UiState.() -> UiState) {
        _uiState.update(block)
    }

    private suspend fun emitUiEffect(uiEffect: UiEffect) {
        _uiEffect.send(uiEffect)
    }
}

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