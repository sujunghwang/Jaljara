package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

import com.kizitonwose.calendar.compose.HorizontalCalendar
import com.kizitonwose.calendar.compose.rememberCalendarState
import com.kizitonwose.calendar.core.*
import java.time.DayOfWeek
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Day(day: CalendarDay, onClickDay: (String) -> Unit) {
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clickable(
                enabled = day.position == DayPosition.MonthDate,
                onClick = {
                    val formatDate = day.date.format(DateTimeFormatter.ofPattern("yyyyMMdd"))
                    Log.d("데이 클릭", formatDate)
                    onClickDay(formatDate)
                }
            )
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.date.dayOfMonth.toString(),
            color = if (day.position == DayPosition.MonthDate) Color.Black else Color.Gray
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaysOfWeekTitle(calendarMonth: CalendarMonth, daysOfWeek: List<DayOfWeek>) {
    Column(modifier = Modifier
        .border(color = Color.Black, width = 1.dp)
    ) {
        Text(
            text = calendarMonth.yearMonth.toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp, top = 16.dp)
        ) {
            for (dayOfWeek in daysOfWeek) {
                Text(
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SleepCalenderScreen(
    // 아이의 pk
    childId : Int? = null,
    onClickDay : (String) -> Unit
){
    var currentMonth = remember { YearMonth.now() }
    val startMonth = remember { currentMonth.minusMonths(100) }
    val endMonth = remember { currentMonth.plusMonths(100) }
    val firstDayOfWeek = remember { firstDayOfWeekFromLocale() }


    val state = rememberCalendarState(
        startMonth = startMonth,
        endMonth = endMonth,
        firstVisibleMonth = currentMonth,
        firstDayOfWeek = firstDayOfWeek
    )

    val daysOfWeek = daysOfWeek()

    Column(){
        Text(text = "내 아이 수면 달력")
        HorizontalCalendar(
            state = state,
            dayContent =
            {
                Day(it, onClickDay = onClickDay)
            },
            monthHeader = {
                DaysOfWeekTitle(calendarMonth = it, daysOfWeek = daysOfWeek)
            },
            monthContainer = {
                    _, container ->
                val configuration = LocalConfiguration.current
                val screenWidth = configuration.screenWidthDp.dp
                Box(
                    modifier = Modifier
                        .width(screenWidth * 0.9f)
                        .padding(8.dp)
                        .clip(shape = RoundedCornerShape(8.dp))
                        .border(
                            color = Color.Black,
                            width = 1.dp,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    container()
                }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true)
fun prevcal(){
    SleepCalenderScreen(null){

    }
}