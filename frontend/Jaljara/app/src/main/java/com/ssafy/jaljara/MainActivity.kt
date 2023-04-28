package com.ssafy.jaljara

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.ssafy.jaljara.ui.screen.JaljaraApp
import com.ssafy.jaljara.ui.screen.parent.ParentApp
import com.ssafy.jaljara.ui.theme.JaljaraTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JaljaraTheme {
                ParentApp()
            }
        }
    }

    @Composable
    private fun getGoogleLoginAuth(): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestIdToken(stringResource(R.string.google_web_client_id))
            .requestId()
            .requestProfile()
            .build()
        return GoogleSignIn.getClient(this, gso)
    }
}
