package com.ssafy.jaljara.data

import com.ssafy.jaljara.utils.ProviderType

enum class UserType {
    PARENTS,
    CHILD
}

data class UserInfo(
    val userId: String,
    val profileImageUrl: String,
    val userType: UserType
)

data class UserLoginRequestDto(
    val provider: ProviderType,
    val token: String
)

data class UserLoginResponseDto(
    val userInfo: UserInfo,
    val accessToken: String,
    val refreshToken: String
)

data class UserSignupResponseDto(
    val userId: String,
    val profileImageUrl: String,
    val userType: UserType
)

data class UserInfoWithTokens(
    val userInfo: UserInfo,
    val accessToken: String,
    val refreshToken: String
)