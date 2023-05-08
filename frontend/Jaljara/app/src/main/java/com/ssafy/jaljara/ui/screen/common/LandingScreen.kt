package com.ssafy.jaljara.ui.screen.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.component.NightForestBackGround
import com.ssafy.jaljara.data.UserType
import com.ssafy.jaljara.ui.screen.child.ChildApp
import com.ssafy.jaljara.ui.screen.parent.ParentApp
import com.ssafy.jaljara.ui.vm.LandingViewModel
import com.ssafy.jaljara.utils.googleLoginHelper
import com.ssafy.jaljara.utils.kakaoLoginHelper

@SuppressLint("NewApi")
@Preview(showSystemUi = true)
@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    viewModel: LandingViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val interactionSource = remember { MutableInteractionSource() }
    var showOneTapDialog by remember { mutableStateOf(false) }
    //var state by remember { mutableStateOf(viewModel.state) }
    val state by viewModel.uiState.collectAsState()

    when (state.isLoggedIn) {
        true -> {
            when (state.userType!!) {
                UserType.PARENTS -> ParentApp()
                UserType.CHILD -> ChildApp()
            }
        }
        else -> {
            NightForestBackGround {
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
                        ) {
                            Text(text = "Top Padding")
                        }

                        Button(
                            onClick = { kakaoLoginHelper(context, viewModel) },
                            shape = RoundedCornerShape(6.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Transparent,
                            )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.kakao_login_medium_wide),
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
                            Row {
                                Image(
                                    painter = painterResource(id = R.drawable.google_login_normal),
                                    contentDescription = null,
                                    Modifier.wrapContentWidth(Alignment.Start)
                                )
                            }
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
}