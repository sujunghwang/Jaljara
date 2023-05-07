package com.ssafy.jaljara.data

data class LandingUiState (
    var isLoggedIn : Boolean = false,
    var userType: UserType? = null,
    var isTokenAvailable : Boolean = false
)