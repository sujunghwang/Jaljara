package com.ssafy.jaljara.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.utils.UiState

/**
 * CalendarViewModel, SleepCalenderScreen을 참고해서
 * 공통 로딩 컴포저블을 구현해 보세요
 *
 * **/
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.size(200.dp),
            painter = painterResource(R.drawable.loading_img),
            contentDescription = "loading"
        )
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text("로딩 실패.", color = Color.White)
    }
}

@Composable
fun <T> LoadingComponent(modifier: Modifier = Modifier, uiState: UiState<T>,
                     loading: @Composable ()->Unit = {LoadingScreen(modifier)},
                     error: @Composable ()->Unit = {ErrorScreen(modifier)},
                     onSuccessHandler: (T) -> Unit = {},
                     content: @Composable ()->Unit = {},
){
    when (uiState) {
        is UiState.Loading -> loading()
        is UiState.Success -> {
            content()
            onSuccessHandler(uiState.data)
        }
        is UiState.Error -> error()
    }
}