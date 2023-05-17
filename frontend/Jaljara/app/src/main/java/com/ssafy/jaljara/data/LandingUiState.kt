package com.ssafy.jaljara.data

enum class Screen {
    LANDING,
    SIGNUP,
    SIGNIN
}

data class LandingUiState (
    var screenState: Screen = Screen.LANDING,
    var isLoggedIn : Boolean = false,
    var userType: UserType? = null,
    var isTokenAvailable : Boolean = false
)