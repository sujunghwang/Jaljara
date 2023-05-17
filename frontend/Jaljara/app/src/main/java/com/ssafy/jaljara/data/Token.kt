package com.ssafy.jaljara.data

class Token {
    data class TokenRefreshRequestDto (
        val refreshToken: String
        )

    data class TokenRefreshResponseDto (
        val accessToken: String,
        val refreshToken: String
        )
}