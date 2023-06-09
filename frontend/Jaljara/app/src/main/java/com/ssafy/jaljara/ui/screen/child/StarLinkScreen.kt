package com.ssafy.jaljara.ui.screen

import android.app.Application
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun StarLink(
    childViewModel: ChildViewModel,
    childId : Long = 1L
){
    var getBtnValid = false
    childViewModel.getChildSleepInfo(childId)
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
                    .padding(start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillParentMaxHeight(0.18f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.astronoutsleep), contentDescription = "icon")
                    Text(
                        text = "바른 수면 별자리",
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
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.reward),
                            contentDescription = null,
                            modifier = Modifier
                                .size(70.dp)
                                .padding(start = 10.dp, end = 5.dp)
                                .offset(x = -16.dp)
                        )
                        Text(
                            text = "${childSleepInfo.streakCount} / 7",
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                        )
                    }
                    Icon(Icons.Filled.OpenInNew, null, Modifier.align(Alignment.TopEnd).size(20.dp).padding(4.dp))
                }

                Button(
                    onClick = {
                        if(childSleepInfo.currentReward == "")
                            failToast.show()
                        else{
                            childViewModel.getReward(childId)
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
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 24.sp
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
                .fillMaxWidth()
                .fillMaxHeight(0.45f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.primary)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Image(
                    modifier = Modifier
                        .size(64.dp)
                        .padding(8.dp)
                        .clip(RoundedCornerShape(50)),
                    painter = painterResource(id = R.drawable.rabbit),
                    contentDescription = null
                )
                Text(
                    modifier = Modifier.padding(8.dp).fillMaxWidth(),
                    text = reward,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun preview(){
    StarLink(childViewModel = ChildViewModel(LocalContext.current as Application))
}