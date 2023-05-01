package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*

import androidx.compose.runtime.*


import androidx.compose.ui.tooling.preview.Preview
import java.util.*

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.himanshoe.kalendar.color.KalendarThemeColor
import com.himanshoe.kalendar.component.KalendarHeader
import com.himanshoe.kalendar.component.day.KalendarDay
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.component.day.config.KalendarDayDefaultColors
import com.himanshoe.kalendar.component.text.KalendarNormalText
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.toKalendarDay
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone

internal object KalendarColors {
    fun defaultColors(): List<KalendarThemeColor> = buildList {
        repeat(12) { index ->
            add(
                KalendarThemeColor(
                    kalendarBackgroundColor[index],
                    backgroundColor[index],
                    headerColors[index]
                )
            )
        }
    }

    @Stable
    private val backgroundColor = listOf(
        Color(0xffF7CFD3),
        Color(0xffEFBDCF),
        Color(0xffDBBFE4),
        Color(0xffCFC4E5),
        Color(0xffC6CAE6),
        Color(0xffC1DEF9),
        Color(0xffBDE3F9),
        Color(0xffBEE8F1),
        Color(0xffBBDEDB),
        Color(0xffCEE5CB),
        Color(0xffDEEBCB),
        Color(0xffF1F4C8),
    )

    @Stable
    private val kalendarBackgroundColor = listOf(
        Color.White,
        Color(0xFFFCEFFE),
        Color(0xFFFDF2FE),
        Color(0xFFFEF7FE),
        Color(0xFFF9FDFE),
        Color(0xFFF1FEFF),
        Color(0xFFEBFEFF),
        Color(0xFFE9FEFF),
        Color(0xFFEBFEFF),
        Color(0xFFFCFFFC),
        Color(0xFFFFFFFB),
        Color(0xFFFFFFF7),
    )

    @Stable
    private val headerColors = listOf(
        Color(0xFFC39EA1),
        Color(0xFFBB8D9E),
        Color(0xFFAA8FB1),
        Color(0xFF9E94B4),
        Color(0xFF9599B4),
        Color(0xFF91ABC5),
        Color(0xFF8CB2C6),
        Color(0xFF8CB7BE),
        Color(0xFF8BACA9),
        Color(0xFF9DB39A),
        Color(0xFFADBA9A),
        Color(0xFFBEC196),
    )
}

data class KalendarThemeColor(
    val backgroundColor: Color,
    val dayBackgroundColor: Color,
    val headerTextColor: Color,
)



val WeekDays = listOf("M", "T", "W", "T", "F", "S", "S")

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JongSeokCalendar(
    modifier: Modifier = Modifier,
    kalendarEvents: List<KalendarEvent> = emptyList(),
    onClickDay: (LocalDate) -> Unit = { },
    takeMeToDate: LocalDate?,
    kalendarDayColors: KalendarDayColors,
    kalendarThemeColors: List<KalendarThemeColor>,
    onChangeMonth: (year: Int, month: Int) -> Unit
) {
    val currentDay = takeMeToDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault())

    var currentMonth by rememberSaveable{ mutableStateOf(currentDay.month)}
    var year by rememberSaveable{ mutableStateOf(currentDay.year)}

    val daysInMonth = currentMonth.minLength()

    val monthValue = if (currentMonth.value.toString().length == 1) "0" + currentMonth.value.toString() else currentMonth.value.toString()
    val startDayOfMonth = "${year}-$monthValue-01".toLocalDate()
    val firstDayOfMonth = startDayOfMonth.dayOfWeek
    val selectedKalendarDate = remember { mutableStateOf(currentDay) }

    Column(
        modifier = modifier
            .background(
                color = kalendarThemeColors[currentMonth.value.minus(1)].backgroundColor
            )
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        KalendarHeader(
            modifier = Modifier.padding(vertical = 12.dp),
            month = currentMonth,
            onPreviousClick = {
                if(currentMonth == Month.JANUARY){
                    year = year.minus(1)
                    currentMonth = Month.DECEMBER
                }else currentMonth = currentMonth.minus(1)
                onChangeMonth(year, currentMonth.value)
            },
            onNextClick = {
                if(currentMonth == Month.DECEMBER){
                    year = year.plus(1)
                    currentMonth = Month.JANUARY
                }else currentMonth = currentMonth.plus(1)
                onChangeMonth(year, currentMonth.value)
            },
            year = year,
            textColor = kalendarThemeColors[currentMonth.value.minus(1)].headerTextColor,
        )
        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(7),
            content = {
                items(WeekDays) {
                    KalendarNormalText(
                        text = it,
                        fontWeight = FontWeight.Normal,
                        color = kalendarDayColors.textColor,
                    )
                }
                items((getInitialDayOfMonth(firstDayOfMonth)..daysInMonth).toList()) {
                    if (it > 0) {
                        val day = getGeneratedDay(it, currentMonth, year)
                        val isCurrentDay = day == currentDay
                        KalendarDay(
                            kalendarDay = day.toKalendarDay(),
                            kalendarEvents = kalendarEvents,
                            isCurrentDay = isCurrentDay,
                            onCurrentDayClick = { kalendarDay, events ->
                                selectedKalendarDate.value = kalendarDay.localDate
                                onClickDay(kalendarDay.localDate)
                            },
                            selectedKalendarDay = selectedKalendarDate.value,
                            kalendarDayColors = kalendarDayColors,
                            dotColor = kalendarThemeColors[currentMonth.value.minus(1)].headerTextColor,
                            dayBackgroundColor = kalendarThemeColors[currentMonth.value.minus(1)].dayBackgroundColor,
                        )
                    }
                }
            }
        )
    }
}
@RequiresApi(Build.VERSION_CODES.O)
private fun getInitialDayOfMonth(firstDayOfMonth: DayOfWeek) = -(firstDayOfMonth.value).minus(2)

@RequiresApi(Build.VERSION_CODES.O)
private fun getGeneratedDay(day: Int, currentMonth: Month, currentYear: Int): LocalDate {
    val monthValue =
        if (currentMonth.value.toString().length == 1) "0${currentMonth.value}" else currentMonth.value.toString()
    val newDay = if (day.toString().length == 1) "0$day" else day
    return "$currentYear-$monthValue-$newDay".toLocalDate()
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SleepCalenderScreen(
    // 아이의 pk
    childId : Int? = null,
    onClickDay : (LocalDate) -> Unit
){

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    var tYear by rememberSaveable{ mutableStateOf(today.year)}
    var tMonth by rememberSaveable{ mutableStateOf(today.month.value)}

    Column() {

        JongSeokCalendar(
            takeMeToDate = null,
            kalendarDayColors = KalendarDayDefaultColors.defaultColors(),
            kalendarThemeColors = KalendarColors.defaultColors(),
            onChangeMonth = {
                year, month ->
                    tYear = year
                    tMonth = month
            },
            onClickDay = onClickDay
        )
        Text(text = "현재 날짜 $tYear 년 $tMonth 월")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true)
fun prevcal(){
    SleepCalenderScreen(null){
        day -> Log.d("클릭 날짜", day.toString())
    }
}