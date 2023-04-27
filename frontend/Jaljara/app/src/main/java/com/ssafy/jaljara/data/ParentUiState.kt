package com.ssafy.jaljara.data

data class ParentUiState(

    // 자녀 리스트
    val children : List<ChildInfo>? = null,

    // 선택 된 자녀의 idx
    val selectedChildrenIdx : Int? = null,

    // 선택 된 네비게이션 bar idx
    val selectedNavIdx : Int = 0
)
