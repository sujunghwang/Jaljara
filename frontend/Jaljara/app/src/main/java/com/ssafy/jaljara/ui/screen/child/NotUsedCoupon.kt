package com.ssafy.jaljara.ui.screen.child

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.NotUsedCoupon
import com.ssafy.jaljara.ui.vm.ChildViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotUsedCoupon(childViewModel: ChildViewModel, coupon: NotUsedCoupon, modifier: Modifier = Modifier) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 20.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        showDialog = true
                        Log.d("아이등록해제모달 요청","$showDialog")
                    },
                )
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = modifier
                    .size(64.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(50)),
                painter = painterResource(id = R.drawable.rabbit),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                NotUsedCouponContent(coupon.content, coupon.getTime)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "쿠폰 사용") },
            text = { Text(text = "쿠폰을 사용하시겠습니까?") },
            confirmButton = {
                Text(
                    text = "확인",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            childViewModel.setCouponUsed(coupon.rewardId)
                            showDialog=false
                        },
                    color = Color.Red,
                )
            },
            dismissButton = {
                Text(
                    text = "취소",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            showDialog = false
                        },
                )
            }
        )
    }
}

@Composable
fun NotUsedCouponContent(content: String, getTime: String?, modifier: Modifier = Modifier){
    Text(
        text = content,
        style = MaterialTheme.typography.titleMedium
    )
    Row(modifier = modifier.padding(top = 8.dp)) {
        Text(
            text = "발급일 : " + getTime?.split("T")?.get(0) ?:"null 값입니다.",
            style = MaterialTheme.typography.bodySmall
        )
    }

}
