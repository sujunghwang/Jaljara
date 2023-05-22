package com.ssafy.jaljara.data

import com.google.gson.annotations.SerializedName
import com.ssafy.jaljara.utils.TokenHandler.ProviderType

enum class UserType {
    PARENTS,
    CHILD
}

data class UserInfo(
    @SerializedName("userId")
    val userId: String,
    @SerializedName("profileImageUrl")
    val profileImageUrl: String,
    @SerializedName("userType")
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
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("userInfo")
    val userInfo: UserInfo
)