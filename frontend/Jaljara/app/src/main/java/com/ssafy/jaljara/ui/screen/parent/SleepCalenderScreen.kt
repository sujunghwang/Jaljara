package com.ssafy.jaljara.ui.screen.parent


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.himanshoe.kalendar.component.day.KalendarDay
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.himanshoe.kalendar.component.text.KalendarNormalText
import com.himanshoe.kalendar.component.text.KalendarSubTitle
import com.himanshoe.kalendar.component.text.config.KalendarTextColor
import com.himanshoe.kalendar.component.text.config.KalendarTextConfig
import com.himanshoe.kalendar.component.text.config.KalendarTextSize
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.toKalendarDay
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.component.LoadingComponent
import com.ssafy.jaljara.ui.vm.CalendarViewModel
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

val WeekDays = listOf("Mon", "Tue", "Wen", "Tur", "Fri", "Sat", "Sun")

@Composable
fun KalendarIconButton(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    onClick: () -> Unit = {}
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .wrapContentSize()
            .clip(CircleShape)
    ) {
        Icon(
            modifier = Modifier,
            tint = Color.White,
            imageVector = imageVector,
            contentDescription = contentDescription
        )
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun KalendarHeader(
    modifier: Modifier,
    month: Month,
    year: Int,
    onPreviousClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    arrowShown: Boolean = true,
    textColor: Color,
    textSize: KalendarTextSize = KalendarTextSize.SubTitle
) {
    val isNext = remember { mutableStateOf(true) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 8.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AnimatedContent(
            modifier = Modifier
                .wrapContentHeight()
                .wrapContentWidth()
                .align(Alignment.CenterVertically),
            targetState = getTitleText(month.name, year),
            transitionSpec = {
                addAnimation(isNext = isNext.value).using(
                    SizeTransform(clip = false)
                )
            }
        ) {
            KalendarSubTitle(
                text = it,
                modifier = Modifier,
                kalendarTextConfig = KalendarTextConfig(
                    kalendarTextColor = KalendarTextColor(textColor), kalendarTextSize = textSize
                )
            )
        }
        if (arrowShown) {
            Row(
                modifier = Modifier
                    .wrapContentWidth()
                    .align(Alignment.CenterVertically),
                horizontalArrangement = Arrangement.End,
            ) {
                KalendarIconButton(
                    modifier = Modifier.wrapContentSize(),
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Previous Week",
                    onClick = {
                        isNext.value = false
                        onPreviousClick()
                    }

                )
                KalendarIconButton(
                    modifier = Modifier.wrapContentSize(),
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Next Month",
                    onClick = {
                        isNext.value = true
                        onNextClick()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
internal fun addAnimation(duration: Int = 500, isNext: Boolean): ContentTransform {
    return slideInVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) height else -height } + fadeIn(
        animationSpec = tween(durationMillis = duration)
    ) with slideOutVertically(animationSpec = tween(durationMillis = duration)) { height -> if (isNext) -height else height } + fadeOut(
        animationSpec = tween(durationMillis = duration)
    )
}

internal fun getTitleText(monthName: String, year: Int): String {
    return monthName.lowercase().replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    } + " " + year
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JongSeokCalendar(
    modifier: Modifier = Modifier,
    kalendarEvents: List<KalendarEvent> = emptyList(),
    onClickDay: (LocalDate) -> Unit = { },
    takeMeToDate: LocalDate?,
    kalendarDayColors: KalendarDayColors,
    onChangeMonth: (year: Int, month: Int) -> Unit,
    sleepLogSimple: List<Int> = listOf()
) {
    val currentDay = takeMeToDate ?: Clock.System.todayIn(TimeZone.currentSystemDefault())

    var currentMonth by rememberSaveable{ mutableStateOf(currentDay.month)}
    var year by rememberSaveable{ mutableStateOf(currentDay.year)}

    val daysInMonth = currentMonth.minLength()

    val monthValue = if (currentMonth.value.toString().length == 1) "0" + currentMonth.value.toString() else currentMonth.value.toString()
    val startDayOfMonth = "${year}-$monthValue-01".toLocalDate()
    val firstDayOfMonth = startDayOfMonth.dayOfWeek
    val selectedKalendarDate = remember { mutableStateOf(currentDay) }

    Log.d("슬립로그 되냐?", sleepLogSimple.toString())

    Column(
        modifier = modifier
            .background(
                color = Color(0xFF323741),
                shape = RoundedCornerShape(8.dp)
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
            textColor = Color.White
        )
        val simpleLogSize = sleepLogSimple.size
        var idx = 0

        LazyVerticalGrid(
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(7),
            content = {
                items(WeekDays) {
                    KalendarNormalText(
                        modifier = Modifier.padding(bottom = 16.dp),
                        text = it,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }

                items((getInitialDayOfMonth(firstDayOfMonth)..daysInMonth).toList()) {
                    if (it > 0) {
                        val day = getGeneratedDay(it, currentMonth, year)
                        val isCurrentDay = day == currentDay

                        var isSleepLog = false

                        // 배열의 범위를 벗어나지 않았을 때
                        if(idx < simpleLogSize) {
                            if (sleepLogSimple[idx] < day.dayOfMonth) {
                                idx++
                            } else if (sleepLogSimple[idx] == day.dayOfMonth){
                                isSleepLog = true
                                Log.d("슬립로그임 ㅋㅋ", "$idx, ${sleepLogSimple[idx]}, ${day.dayOfMonth}")
                                idx++
                            }
                        }
                        Box(modifier = Modifier.fillMaxSize()) {
                            KalendarDay(
                                modifier = Modifier
                                    .background(
                                        color = Color(0xff3B414A)
                                    )
                                    .border(
                                        width = 2.dp,
                                        color = Color(0xFF323741),
                                        shape = RoundedCornerShape(4.dp)
                                    )
                                ,
                                kalendarDay = day.toKalendarDay(),
                                kalendarEvents = kalendarEvents,
                                isCurrentDay = isCurrentDay,
                                onCurrentDayClick = { kalendarDay, events ->
                                    if(isSleepLog) onClickDay(kalendarDay.localDate)
                                },
                                selectedKalendarDay = selectedKalendarDate.value,
                                kalendarDayColors = kalendarDayColors,
                                dotColor = Color.White,
                                dayBackgroundColor = Color.White
                            )

                            if(isSleepLog){
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(
                                            color = Color.Yellow,
                                            shape = CircleShape
                                        ).align(Alignment.BottomCenter)
                                )
                            }
                        }
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
    childId : Long = 1,
    onClickDay : (LocalDate) -> Unit
){

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    var tYear by rememberSaveable{ mutableStateOf(today.year)}
    var tMonth by rememberSaveable{ mutableStateOf(today.month.value)}


    val calendarViewModel : CalendarViewModel = viewModel()

    var simpleSleepLog by rememberSaveable{ mutableStateOf(listOf<Int>())}

    calendarViewModel.getSimpleSleepLog(childId, convert2yyyyMM(tYear, tMonth))

    LazyColumn(modifier = Modifier.fillMaxHeight()){
        item{
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.fillParentMaxHeight(0.1f))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.2f),
                    horizontalArrangement = Arrangement.Center,
                ){
                    Image(
                        painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                        contentDescription = null,
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                    Text(
                        text = "내 아이 수면 달력",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                LoadingComponent<List<Int>>(
                    uiState = calendarViewModel.calendarUiState,
                    onSuccessHandler = {
                        simpleSleepLog = it
                    }
                ){
                    JongSeokCalendar(
                        takeMeToDate = null,
                        kalendarDayColors = KalendarDayColors(Color.White, Color.Black),
                        onChangeMonth = { year, month ->
                            tYear = year
                            tMonth = month
                        },
                        onClickDay = onClickDay,
                        modifier = Modifier
                            .padding(16.dp)
                            .height(500.dp),
                        sleepLogSimple = simpleSleepLog
                    )
                }
            }
        }
    }

}

private fun convert2yyyyMM(year: Int, month: Int): String{
    val sb = StringBuilder()
    sb.append(year)
    if(month < 10) sb.append(0)
    sb.append(month)

    return sb.toString()
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true)
fun prevcal(){
    SleepCalenderScreen(1){
        day -> Log.d("클릭 날짜", day.toString())
    }
}