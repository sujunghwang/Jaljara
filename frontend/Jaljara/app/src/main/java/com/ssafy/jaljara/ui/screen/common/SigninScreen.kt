package com.ssafy.jaljara.ui.screen.common

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.ssafy.jaljara.R
import com.ssafy.jaljara.activity.ChildActivity
import com.ssafy.jaljara.data.UserType
import com.ssafy.jaljara.ui.screen.parent.ParentApp
import com.ssafy.jaljara.ui.vm.LandingViewModel
import com.ssafy.jaljara.utils.googleLoginHelper
import com.ssafy.jaljara.utils.kakaoLoginHelper

@SuppressLint("NewApi")
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    navigate: (String) -> Unit,
    viewModel: LandingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    var showOneTapDialog by remember { mutableStateOf(false) }
    val state by viewModel.uiState.collectAsState()

    when (state.isLoggedIn) {
        true -> {
            when (state.userType!!) {
                UserType.PARENTS -> ParentApp()
                UserType.CHILD -> {
                    context.startActivity(
                        Intent(context, ChildActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
            }
        }
        else -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .height(500.dp)
                    )

                    Button(
                        onClick = { kakaoLoginHelper(context, viewModel) },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                        )
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.kakao_login_large_wide),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                        )
                    }

                    Button(
                        onClick = { showOneTapDialog = true },
                        modifier = Modifier
                            .width(340.dp)
                            .height(50.dp)
                            .padding(start = 3.dp, end = 3.dp)
                            .clickable(
                                indication = null,
                                interactionSource = interactionSource
                            ) {},
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Gray,
                            contentColor = Color.Black
                        )
                    ) {
                        var drawable = AppCompatResources.getDrawable(
                            LocalContext.current,
                            com.google.android.gms.auth.api.R.drawable.common_google_signin_btn_icon_light_normal
                        )
                        Image(
                            painter = rememberDrawablePainter(drawable = drawable),
                            modifier = Modifier
                                .width(50.dp),
                            contentDescription = null,
                            alignment = Alignment.CenterStart,
                        )
                        Text(
                            text = "Sign With google", modifier = Modifier
                                .padding(6.dp)
                                .wrapContentWidth(Alignment.CenterHorizontally)
                        )
                    }

                    if (showOneTapDialog)
                        googleLoginHelper(
                            context = context,
                            onClose = { showOneTapDialog = !showOneTapDialog },
                            viewModel = viewModel
                        )
                }
            }
        }
    }
}