package com.ssafy.jaljara.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ssafy.jaljara.R

@Composable
fun StarLink(){

    var getBtnValid = true
    var rewardBtnValid = true

    var streakCnt = remember {
        mutableStateOf(0)
    }
//    if(streakCnt == 7)
//        getBtnValid = true

    var currentReward = "놀이동산 같이 가기"
    if(currentReward == "")
        rewardBtnValid = false

    var openDialog = remember {
        mutableStateOf(false)
    }

    Box(modifier = Modifier.fillMaxSize()){
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(painter = painterResource(id = R.drawable.astronoutsleep), contentDescription = "icon")
                Text(
                    text = "바른 수면 별자리",
                    color = Color.White,
                    fontSize = 40.sp
                )
            }
            var starLinkImg = R.drawable.star_link
            when (streakCnt.value) {
                1 -> starLinkImg = R.drawable.star_link_1
                2 -> starLinkImg = R.drawable.star_link_2
                3 -> starLinkImg = R.drawable.star_link_3
                4 -> starLinkImg = R.drawable.star_link_4
                5 -> starLinkImg = R.drawable.star_link_5
                6 -> starLinkImg = R.drawable.star_link_6
                7 -> starLinkImg = R.drawable.star_link_7
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(starLinkImg),
                    contentDescription = "star_link",
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .fillMaxHeight(0.8f)
                )
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(color = Color(0x40BAD6F0), shape = RoundedCornerShape(16.dp))
                ){
                    Text(
                        text = "${streakCnt.value} / 7",
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
            Button(
                onClick = {
                    openDialog.value = true
                },
                contentPadding = PaddingValues(12.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFF674AB3)),
                modifier = Modifier
                    .wrapContentSize()
                    .padding(10.dp),
                enabled = rewardBtnValid
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_card_giftcard_24),
                    contentDescription = "present drawable",
                    modifier = Modifier.size(36.dp)
                )
            }
            Button(
                onClick = {
                    streakCnt.value = (streakCnt.value + 1) % 8
                },
                contentPadding = PaddingValues(12.dp),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = Color.Gray,
                    containerColor = Color.Blue,
                    contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                enabled = getBtnValid
            ) {
                Text(text = "보상 획득")
            }
        }

        if(openDialog.value){
            RewardDialog(currentReward) { openDialog.value = false }
        }
    }
}

@Composable
fun RewardDialog(reward: String, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = {
            onDismiss()
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(color = Color(0xFF674AB3)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 16.dp),
                    painter = painterResource(id = R.drawable.baseline_card_giftcard_24),
                    contentDescription = "gift image"
                )
                Spacer(modifier = Modifier.size(16.dp))
                Text(
                    modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                    text = reward,
                    textAlign = TextAlign.Justify,
                    color = Color.White,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun preview(){
    StarLink()
}