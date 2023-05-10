package com.ssafy.jaljara.ui.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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

    val successToast = Toast.makeText(LocalContext.current, "보상 획득 성공", Toast.LENGTH_SHORT)
    val failToast = Toast.makeText(LocalContext.current, "등록된 보상이 없습니다", Toast.LENGTH_SHORT)

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

    LazyColumn(modifier = Modifier.fillMaxHeight()){
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillParentMaxHeight(0.2f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.astronoutsleep), contentDescription = "icon")
                    Text(
                        text = "수면 별자리",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                Image(
                    painter = painterResource(starLinkImg),
                    contentDescription = "star_link",
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.5f)
                )

                Spacer(modifier = Modifier.fillParentMaxHeight(0.02f))

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .fillParentMaxHeight(0.12f)
                        .background(
                            color = MaterialTheme.colorScheme.tertiary,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            openDialog.value = true
                        },
                    contentAlignment = Alignment.Center
                ){
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.reward),
                            contentDescription = null,
                            modifier = Modifier.size(56.dp).padding(end = 8.dp)
                        )
                        Text(
                            text = "${childSleepInfo.streakCount} / 7",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp
                        )
                    }
                }

                Button(
                    onClick = {
                        if(childSleepInfo.currentReward == "")
                            failToast.show()
                        else{
                            childViewModel.getReward(1)
                            successToast.show()
                        }
                    },
                    contentPadding = PaddingValues(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.LightGray,
                        disabledContentColor = Color.Gray,
                        containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    enabled = getBtnValid
                ) {
                    Text(
                        text = "보상 획득",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
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
                    style = MaterialTheme.typography.titleSmall
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