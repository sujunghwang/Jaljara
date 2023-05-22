package com.ssafy.jaljara.ui.screen.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.Screen.*
import com.ssafy.jaljara.ui.vm.LandingViewModel

@SuppressLint("NewApi")
@Preview(showSystemUi = true)
@Composable
fun LandingScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    viewModel: LandingViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    navigate: (String) -> Unit,

    ) {
    val TEXT_START_PAD = 50.dp
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        visible = true
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    //.background(color = Color.Blue)
                    .height(250.dp)
            ) {

                Column {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp)
                    )

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(durationMillis = 2500),
                            initialAlpha = 0.01f
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 2000)),

                        modifier = Modifier
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = TEXT_START_PAD),
                            textAlign = TextAlign.Left,
                            fontSize = 50.sp,
                            fontWeight = FontWeight.ExtraBold,
                            text = "잘자라", color = Color.White
                        )
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(durationMillis = 3500),
                            initialAlpha = 0.01f
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 2000)),
                        modifier = Modifier
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = TEXT_START_PAD, top = 0.dp),
                            textAlign = TextAlign.Left,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = "Sleep Well, Grow Well",
                            color = Color.White
                        )
                    }
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(durationMillis = 4500),
                            initialAlpha = 0.01f
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 2000))
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = TEXT_START_PAD, top = 20.dp),
                            textAlign = TextAlign.Left,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = "부모님과 함께 기르는",
                            color = Color.White
                        )
                    }

                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(durationMillis = 4500),
                            initialAlpha = 0.01f
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 2000))
                    ) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = TEXT_START_PAD, top = 5.dp),
                            textAlign = TextAlign.Left,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            text = "우리아이 수면 습관",
                            color = Color.White
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 3000),
                    initialAlpha = 0.01f
                ),
                exit = fadeOut(animationSpec = tween(durationMillis = 2000)),
                modifier = Modifier
            ) {
                Image(
                    painter = painterResource(id = R.drawable.landing_cabin),
                    contentDescription = "background",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        //.background(color = Color.Magenta)
                        .padding(start = 40.dp, end = 40.dp)
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(20.dp))
                )
            }

            Box(
                modifier = Modifier
                    //.background(color = Color.Green)
                    .fillMaxWidth()
                    .padding(top = 0.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AnimatedVisibility(
                        visible = visible,
                        enter = fadeIn(
                            animationSpec = tween(durationMillis = 4000),
                            initialAlpha = 0.01f
                        ),
                        exit = fadeOut(animationSpec = tween(durationMillis = 2000)),
                        modifier = Modifier
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(30.dp)) {

                            Button(
                                modifier = Modifier
                                    .padding(start = 40.dp, end = 40.dp)
                                    .fillMaxWidth(),
                                onClick = { navigate(LandingScreens.Signup.url) },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Gray.copy(alpha = .3f),
                                )
                            ) {
                                Text(
                                    text = "회원가입", modifier = Modifier
                                        .padding(6.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 16.sp
                                )
                            }

                            Button(
                                modifier = Modifier
                                    .padding(start = 40.dp, end = 40.dp)
                                    .fillMaxWidth(),

                                onClick = { navigate(LandingScreens.Login.url) },
                                shape = RoundedCornerShape(6.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Gray.copy(alpha = .3f),
                                )
                            ) {
                                Text(
                                    text = "로그인", modifier = Modifier
                                        .padding(6.dp)
                                        .wrapContentWidth(Alignment.CenterHorizontally),
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}