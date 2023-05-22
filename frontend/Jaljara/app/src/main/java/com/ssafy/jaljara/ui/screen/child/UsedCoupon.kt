package com.ssafy.jaljara.ui.screen.child

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.UsedCoupon


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsedCoupon(coupon: UsedCoupon, modifier: Modifier = Modifier){
    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = 20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(Color.LightGray),
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
                    .clip(RoundedCornerShape(50))
                    .alpha(0.5f),
                painter = painterResource(id = R.drawable.rabbit),
                contentDescription = null
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                NotUsedCouponContent(coupon.content, coupon.getTime)
                Row(modifier = modifier.padding(top = 8.dp)) {
                    Text(
                        text = "사용일 : " + coupon.usedTime?.split("T")?.get(0) ?:"",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Black
                    )
                }

            }
        }
    }
}

