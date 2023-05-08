package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.enumType.getWeekBydayOfWeekNumber
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun SleepTimeCircleClock(modifier: Modifier = Modifier){
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.border(
            width = 1.dp,
            shape = CircleShape,
            color = Color.White
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.astronoutsleep),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
fun SettingTime(modifier: Modifier = Modifier, @DrawableRes draw: Int, description: String, time: String){
    Row(modifier = modifier){
        Image(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxSize()
                .padding(4.dp),
            painter = painterResource(draw),
            contentDescription = "settingImage"
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(0.7f)
                .padding(8.dp)
        ) {
            Text(text = description, style = MaterialTheme.typography.titleSmall)
            Text(text = time, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ArtBox(modifier: Modifier = Modifier, boxContent: @Composable (Modifier) -> Unit){
    Box(modifier = modifier
        .border(
            width = 4.dp,
            shape = RoundedCornerShape(2.dp),
            color = Color.White
        )
        .background(color = MaterialTheme.colorScheme.tertiary)
    )
    {
        boxContent(Modifier.align(Alignment.Center))
    }
}
@Composable
fun showButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            tint = Color.Black,
            contentDescription = null,
        )
    }
}

@Composable
fun MissionLogDeatail(modifier: Modifier = Modifier){
    Image(
        modifier = modifier,
        painter = painterResource(R.drawable.ic_launcher_foreground),
        contentDescription = "settingImage"
    )
}

@Composable
fun MissionLog(modifier: Modifier = Modifier,expanded: Boolean ,onClickButton: () -> Unit, detailHeight: Dp){
    Column(modifier = modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(16.dp))
        .animateContentSize(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = modifier
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            if(!expanded)
                Text(text = "미션기록", style = MaterialTheme.typography.titleSmall, modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp))
            else Spacer(modifier = Modifier.weight(1f))
            showButton(expanded = expanded,
                onClick = onClickButton,
            )
        }
        if(expanded)
            MissionLogDeatail(modifier = Modifier.height(detailHeight))
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SleepLogDetailScreen(formatDate : String =  "20230502"){

    // api 호출 코드 필요

    val date: LocalDate = LocalDate.parse(formatDate, DateTimeFormatter.ofPattern("yyyyMMdd"))

    val dayOfWeek: DayOfWeek = date.dayOfWeek

    val dayOfWeekNumber = dayOfWeek.value

    val fomatter = DateTimeFormatter.ofPattern("MM-dd")

    val displayDate = date.format(fomatter)

    var expanded by rememberSaveable{ mutableStateOf(false) }

    BoxWithConstraints {
        val pageSize = this.maxHeight
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "$displayDate ${getWeekBydayOfWeekNumber(dayOfWeekNumber).korean}",
                modifier = Modifier
                    .fillMaxHeight(0.1f)
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Row() {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp)
                ) {
                    Text(text = "수면시간", style = MaterialTheme.typography.titleSmall)
                    SleepTimeCircleClock(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .height(pageSize/4)
                    )
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp).height(pageSize/4)
                ) {
                    SettingTime(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        draw = R.drawable.baseline_king_bed_24,
                        description = "취침시간",
                        time = "22:10"
                    )
                    SettingTime(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        draw = R.drawable.baseline_alarm_24,
                        description = "기상시간",
                        time = "07:40"
                    )
                }
            }
            Row() {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(text = "수면달성도", style = MaterialTheme.typography.titleSmall)
                    ArtBox(
                        modifier = Modifier
                            .fillMaxSize().height(pageSize/4)
                    ) { modifier ->
                        Text(text = "93%", modifier = modifier)
                    }
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(16.dp)
                ) {
                    Text(text = "미션달성", style = MaterialTheme.typography.titleSmall)
                    ArtBox(
                        modifier = Modifier
                            .fillMaxSize().height(pageSize/4)
                    ) { modifier ->
                        Text(text = "COMPLETE!", modifier = modifier)
                    }
                }
            }
            MissionLog(
                expanded = expanded,
                onClickButton = {
                    expanded = !expanded
                },
                detailHeight = pageSize/2
            )
        }
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun sleepDetailPreview(){
    SleepLogDetailScreen("20230502")
}