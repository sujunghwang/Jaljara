package com.ssafy.jaljara.ui.screen.child

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.NotUsedCoupon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotUsedCoupon(coupon: NotUsedCoupon, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.padding(8.dp),
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
}

@Composable
fun NotUsedCouponContent(content: String, getTime: String?, modifier: Modifier = Modifier){
    Text(
        text = content,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
    Row(modifier = modifier.padding(top = 8.dp)) {
        Text(
            text = "발급일 : "
        )
        Text(
            text = getTime?.split("T")?.get(0) ?:"null 값입니다."
        )
    }

}
