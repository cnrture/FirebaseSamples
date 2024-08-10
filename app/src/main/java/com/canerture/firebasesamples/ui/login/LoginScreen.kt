package com.canerture.firebasesamples.ui.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.canerture.firebasesamples.ui.login.LoginContract.UiAction
import com.canerture.firebasesamples.ui.login.LoginContract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun LoginScreen(
    uiState: UiState,
    uiEffect: Flow<LoginContract.UiEffect>,
    onAction: (UiAction) -> Unit,
    onNavigateMainScreen: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(uiEffect, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            uiEffect.collect { effect ->
                when (effect) {
                    is LoginContract.UiEffect.ShowToast -> {
                        Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                    }

                    is LoginContract.UiEffect.GoToMainScreen -> {
                        onNavigateMainScreen()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        EmailAndPasswordContent(
            email = uiState.email,
            password = uiState.password,
            onEmailChange = { onAction(UiAction.ChangeEmail(it)) },
            onPasswordChange = { onAction(UiAction.ChangePassword(it)) },
            onSignInClick = { onAction(UiAction.SignInClick) },
            onSignUpClick = { onAction(UiAction.SignUpClick) },
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 64.dp), thickness = 2.dp)

        PhoneNumberContent(
            phoneNumber = uiState.phoneNumber,
            verifyCode = uiState.verifyCode,
            onPhoneNumberChange = { onAction(UiAction.ChangePhoneNumber(it)) },
            onVerifyCodeChange = { onAction(UiAction.ChangeVerifyCode(it)) },
            onSendCodeClick = { onAction(UiAction.SendCodeClick) },
            onVerifyCodeClick = { onAction(UiAction.VerifyCodeClick) },
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 64.dp), thickness = 2.dp)

        AnonymouslyContent(
            onSignInAnonymouslyClick = { onAction(UiAction.AnonymousClick) },
        )
    }
}

@Composable
fun EmailAndPasswordContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
) {
    Text(
        text = "Email / Password",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = email,
        maxLines = 1,
        placeholder = { Text("Email") },
        onValueChange = onEmailChange,
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = password,
        maxLines = 1,
        placeholder = { Text("Password") },
        onValueChange = onPasswordChange,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = { onSignInClick() },
        ) {
            Text("Sign In")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = { onSignUpClick() },
        ) {
            Text("Sign Up")
        }
    }
}

@Composable
fun PhoneNumberContent(
    phoneNumber: String,
    verifyCode: String,
    onPhoneNumberChange: (String) -> Unit,
    onVerifyCodeChange: (String) -> Unit,
    onSendCodeClick: () -> Unit,
    onVerifyCodeClick: () -> Unit,
) {
    Text(
        text = "Phone Number",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Row(
        horizontalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp, end = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = phoneNumber,
                maxLines = 1,
                placeholder = { Text("Phone Number") },
                onValueChange = onPhoneNumberChange,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onSendCodeClick() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Send Code")
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = verifyCode,
                maxLines = 1,
                placeholder = { Text("Verify Code") },
                onValueChange = onVerifyCodeChange,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { onVerifyCodeClick() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Verify Code")
            }
        }
    }
}

@Composable
fun AnonymouslyContent(
    onSignInAnonymouslyClick: () -> Unit,
) {
    Text(
        text = "Anonymously",
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    )

    Spacer(modifier = Modifier.height(16.dp))

    Button(onClick = { onSignInAnonymouslyClick() }) {
        Text("Sign In Anonymously")
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        uiState = UiState(),
        uiEffect = emptyFlow(),
        onAction = {},
        onNavigateMainScreen = {},
    )
}