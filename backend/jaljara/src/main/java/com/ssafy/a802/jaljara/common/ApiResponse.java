package com.ssafy.a802.jaljara.common;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) //EnumView Adoc 작성 위해 필요
public class ApiResponse<T> {

	private T data;

}
