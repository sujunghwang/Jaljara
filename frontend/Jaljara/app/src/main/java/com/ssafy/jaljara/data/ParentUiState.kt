package com.ssafy.jaljara.data

data class ParentUiState(
    // 자녀 리스트
    var children : List<ChildInfo>? = null,

    // 선택 된 자녀의 idx
    var selectedChildrenIdx : Long = 0,

    // 네비게이션 표시
    var showNavigation : Boolean = true,

)
