package com.ssafy.jaljara.data

data class ParentUiState(

    // 자녀 리스트
    val children : List<ChildInfo>? = null,

    // 선택 된 자녀의 idx
    val selectedChildrenIdx : Int? = null,

    // 네비게이션 표시
    val showNavigation : Boolean = true,

)
