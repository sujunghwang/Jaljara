package com.ssafy.jaljara.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.ssafy.jaljara.ui.vm.ChildViewModel

@Composable
fun StarLink(childViewModel: ChildViewModel){
    var getBtnValid = false
    childViewModel.getChildSleepInfo(1)
    var childSleepInfo = childViewModel.childSleepResponse

    if(childSleepInfo.streakCount >= 7)
        getBtnValid = true

    var openDialog = rememberSaveable {
        mutableStateOf(false)
    }

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
                style = MaterialTheme.typography.titleLarge
            )
        }
        var starLinkImg = R.drawable.star_link_7
        when (childSleepInfo.streakCount) {
            1 -> starLinkImg = R.drawable.star_link_1
            2 -> starLinkImg = R.drawable.star_link_2
            3 -> starLinkImg = R.drawable.star_link_3
            4 -> starLinkImg = R.drawable.star_link_4
            5 -> starLinkImg = R.drawable.star_link_5
            6 -> starLinkImg = R.drawable.star_link_6
            0 -> starLinkImg = R.drawable.star_link
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
                    .background(color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(16.dp))
            ){
                Text(
                    text = "${childSleepInfo.streakCount} / 7",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp)
                )
            }
        }
        Button(
            onClick = {
                openDialog.value = true
            },
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary),
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_card_giftcard_24),
                contentDescription = "present drawable",
                modifier = Modifier.size(36.dp)
            )
        }
        Button(
            onClick = {
                childViewModel.getReward(1)
            },
            contentPadding = PaddingValues(12.dp),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color.LightGray,
                containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
//            enabled = getBtnValid
        ) {
            Text(
                text = "보상 획득",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }

    if(openDialog.value){
        if(childSleepInfo.currentReward == "")
            RewardDialog("등록된 보상이 없습니다") { openDialog.value = false }
        else
            RewardDialog(childSleepInfo.currentReward) { openDialog.value = false }
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
    StarLink(childViewModel = ChildViewModel())
}