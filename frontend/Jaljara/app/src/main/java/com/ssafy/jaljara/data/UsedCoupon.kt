package com.ssafy.jaljara.data

import java.time.LocalDate

data class UsedCoupon(
    val rewardId: Int,
    val userId: Int,
    val content: String,
    val getTime: LocalDate,
    val usedTime: LocalDate,
)

val usedCoupons = listOf(
    UsedCoupon(1,1,"다 쓴 쿠폰1", LocalDate.of(2023,4,20), LocalDate.of(2023,4,26)),
    UsedCoupon(2,1,"다 쓴 쿠폰2", LocalDate.of(2023,4,20), LocalDate.of(2023,4,26)),
)