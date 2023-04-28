package com.ssafy.jaljara.data

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate

data class NotUsedCoupon(
    val rewardId: Int,
    val userId: Int,
    val content: String,
    val getTime: LocalDate
)

@RequiresApi(Build.VERSION_CODES.O)
val notUsedCoupons = listOf(
    NotUsedCoupon(4, 1, "놀이동산 가기", LocalDate.of(2023, 4, 26)),
    NotUsedCoupon(6, 1, "용돈 주기", LocalDate.of(2023, 4, 26)),
    NotUsedCoupon(7, 1, "놀이동산 가기", LocalDate.of(2023, 4, 26)),
    NotUsedCoupon(8,1,"용돈 주기", LocalDate.of(2023,4,26)),
    NotUsedCoupon(9,1,"놀이동산 가기", LocalDate.of(2023,4,26)),
    NotUsedCoupon(10,1,"용돈 주기", LocalDate.of(2023,4,26)),
    NotUsedCoupon(11,1,"놀이동산 가기", LocalDate.of(2023,4,26)),
    NotUsedCoupon(12,1,"용돈 주기", LocalDate.of(2023,4,26)),
)