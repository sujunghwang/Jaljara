package com.ssafy.jaljara.ui.enumType

enum class Week(val korean: String) {
    Monday("월요일"),
    Tuesday("화요일"),
    Wednesday("수요일"),
    Thursday("목요일"),
    Friday("금요일"),
    Saturday("토요일"),
    Sunday("일요일");
}

// 1 ~ 7 사이의 수로 Week enum을 가져오는 함수
fun getWeekBydayOfWeekNumber(dayOfWeekNumber: Int): Week{
    if(dayOfWeekNumber < 1 || dayOfWeekNumber > 7)
        throw java.lang.IllegalArgumentException("1이상 7이하의 수가 아닙니다.")
    return Week.values()[dayOfWeekNumber - 1]
}