package com.ssafy.jaljara.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes

import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.UserType
import com.ssafy.jaljara.ui.vm.LandingViewModel

private val TAG = "GoogleLogin"

class OneTapSignInState {
    var opened by mutableStateOf(false)
        private set

    fun open() {
        opened = true
    }

    internal fun close() {
        opened = false
    }
}

@Composable
private fun rememberOneTapSignInState(): OneTapSignInState {
    return remember { OneTapSignInState() }
}

@Composable
fun googleSignupHelper(context : Context,
                       viewModel: LandingViewModel,
                       onClose : () -> Unit,
                       userType: UserType = UserType.PARENTS,
                       parentCode: String?
) {
    val state = rememberOneTapSignInState()
    state.open();

    googleLoginWorker(context as Activity,
        state,
        stringResource(id = R.string.google_web_client_id),
        onTokenIdReceived = {token -> TokenHandler.getInstance().signupWithToken(token, TokenHandler.ProviderType.GOOGLE, viewModel, userType, parentCode); onClose() },
        onDialogDismissed = {
            Log.e(TAG, "Dialog dismissed")
            onClose()
        }
    );
}

@Composable
fun googleLoginHelper(context : Context, viewModel: LandingViewModel, onClose : () -> Unit) {
    val state = rememberOneTapSignInState()
    state.open();

    googleLoginWorker(context as Activity,
        state,
        stringResource(id = R.string.google_web_client_id),
        onTokenIdReceived = {token -> TokenHandler.getInstance().loginWithToken(token, TokenHandler.ProviderType.GOOGLE, viewModel) },
        onDialogDismissed = {
            Log.e(TAG, "Dialog dismissed")
            onClose()
        }
    );
}

@Composable
fun googleLoginWorker(
    activity: Activity,
    state: OneTapSignInState,
    clientId: String,
    nonce: String? = null,
    onTokenIdReceived: (String) -> Unit,
    onDialogDismissed: (String) -> Unit,
) {
    Log.e(TAG, "worker s")
    val activityLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        try {
            Log.e(TAG, "try")
            if (result.resultCode == Activity.RESULT_OK) {
                val oneTapClient = Identity.getSignInClient(activity)
                val credentials = oneTapClient.getSignInCredentialFromIntent(result.data)
                val tokenId = credentials.googleIdToken
                if (tokenId != null) {
                    onTokenIdReceived(tokenId)
                    state.close()
                }
            } else {
                onDialogDismissed("Dialog Closed.")
                state.close()
            }
        } catch (e: ApiException) {
            Log.e(TAG, "${e.message}")
            when (e.statusCode) {
                CommonStatusCodes.CANCELED -> {
                    onDialogDismissed("Dialog Canceled.")
                    state.close()
                }
                CommonStatusCodes.NETWORK_ERROR -> {
                    onDialogDismissed("Network Error.")
                    state.close()
                }
                else -> {
                    onDialogDismissed(e.message.toString())
                    state.close()
                }
            }
        }
    }

    LaunchedEffect(key1 = state.opened) {
        Log.e(TAG, "Launched Effect ${state.opened}")
        if (state.opened) {
            signIn(
                activity = activity,
                clientId = clientId,
                nonce = nonce,
                launchActivityResult = { intentSenderRequest ->
                    activityLauncher.launch(intentSenderRequest)
                },
                onError = {
                    onDialogDismissed(it)
                    state.close()
                }
            )
        }
    }
}

private fun signIn(
    activity: Activity,
    clientId: String,
    nonce: String?,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    onError: (String) -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setNonce(nonce)
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                onError(e.message.toString())
                Log.e(TAG, "${e.message}")
            }
        }
        .addOnFailureListener {
            signUp(
                activity = activity,
                clientId = clientId,
                nonce = nonce,
                launchActivityResult = launchActivityResult,
                onError = onError
            )
            Log.e(TAG, "${it.message}")
        }
}

private fun signUp(
    activity: Activity,
    clientId: String,
    nonce: String?,
    launchActivityResult: (IntentSenderRequest) -> Unit,
    onError: (String) -> Unit
) {
    val oneTapClient = Identity.getSignInClient(activity)
    val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setNonce(nonce)
                .setServerClientId(clientId)
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .build()

    oneTapClient.beginSignIn(signInRequest)
        .addOnSuccessListener { result ->
            try {
                launchActivityResult(
                    IntentSenderRequest.Builder(
                        result.pendingIntent.intentSender
                    ).build()
                )
            } catch (e: Exception) {
                onError(e.message.toString())
                Log.e(TAG, "${e.message}")
            }
        }
        .addOnFailureListener {
            onError("Google Account not Found.")
            Log.e(TAG, "${it.message}")
        }
}