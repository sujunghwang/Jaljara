package com.ssafy.jaljara.ui.screen.parent

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.ssafy.jaljara.ui.vm.ParentViewModel

@Composable
fun EmptyChildrenListScreen(viewModel: ParentViewModel) {
    var userId = viewModel.test!!.userInfo.userId.toLong()
    viewModel.getParentCode(userId)
    val parentCode = viewModel.parentCode

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Box() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.background(color = Color.Transparent)
            ) {
                Text(text = "아직 자녀로 지정된 사용자가 없어요 T.T",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
                Text(text = "아래의 코드를 이용해 자녀를 추가해주세요",
                    textAlign = TextAlign.Center,
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp)
                )
                // 아이 없다는 메세지
            }
        }
        Box(
            modifier = Modifier
                .background(color = Color.Gray.copy(alpha = 0.4f),
                shape = RoundedCornerShape(10.dp))
                .padding(start = 24.dp,
                    end = 24.dp,
                    top = 10.dp,
                    bottom = 10.dp),
        ) {
            // 부모 코드 표시
            Text(text = "${parentCode.parentCode}",
                color = Color.White
            )
        }
        Button(
            onClick = { viewModel.getChildList(parentId = userId) },
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Gray.copy(alpha = 0.4f),
            )
        ){
            Text(text = "자녀를 등록했나요?",
                style = MaterialTheme.typography.bodyLarge,
                fontSize = 24.sp
            )
        }
    }
}