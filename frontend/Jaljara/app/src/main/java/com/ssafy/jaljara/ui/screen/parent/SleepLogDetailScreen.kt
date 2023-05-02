package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.ui.enumType.getWeekBydayOfWeekNumber
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter

import com.ssafy.jaljara.R
import com.ssafy.jaljara.component.NightForestBackGround

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
fun ArtBox(modifier: Modifier = Modifier, boxContent: @Composable (Modifier) -> Unit){
    Box(modifier = modifier
        .border(
            width = 4.dp,
            shape = RoundedCornerShape(2.dp),
            color = Color.White
        ).background(color = Color.LightGray)
    )
    {
        boxContent(Modifier.align(Alignment.Center))
    }
}

@Composable
private fun showButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            tint = MaterialTheme.colorScheme.secondary,
            contentDescription = null,
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SleepLogDetailScreen(formatDate : String =  "20230502"){

    // api 호출 코드 필요

    val date: LocalDate = LocalDate.parse(formatDate, DateTimeFormatter.ofPattern("yyyyMMdd"))

    val dayOfWeek: DayOfWeek = date.dayOfWeek

    val dayOfWeekNumber = dayOfWeek.value

    val fomatter = DateTimeFormatter.ofPattern("MM/dd")

    val displayDate = date.format(fomatter)

    var expanded by rememberSaveable{ mutableStateOf(false) }
    
    val commonColor = Color.White

    NightForestBackGround {
        Column() {
            Text(
                text = "$displayDate ${getWeekBydayOfWeekNumber(dayOfWeekNumber).korean}",
                color = commonColor
            )
            Row(){
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "수면시간", color = commonColor)
                    SleepTimeCircleClock()
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "취침시간",
                        color = commonColor
                    )
                    Text(
                        text = "기상시간",
                        color = commonColor
                    )
                }
            }
            Row(){
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "수면달성도", color = commonColor)
                    ArtBox(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ){
                            modifier ->
                        Text(text = "93%", modifier = modifier, color = commonColor)
                    }
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = "미션달성", color = commonColor)
                    ArtBox(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ){
                            modifier ->
                        Text(text = "COMPLETE!", modifier = modifier, color = commonColor)
                    }
                }
            }
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            ){
                Text(text = "미션기록", modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.weight(3f))
                showButton(expanded = expanded,
                    onClick = { expanded = !expanded },
                    modifier = Modifier.weight(1f)
                )
            }


        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true, showBackground = true)
fun sleepDetailPreview(){
    SleepLogDetailScreen("20230502")
}