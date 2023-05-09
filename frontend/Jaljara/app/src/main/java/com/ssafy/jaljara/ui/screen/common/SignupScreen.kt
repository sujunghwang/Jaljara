package com.ssafy.jaljara.ui.screen.common

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.ui.vm.LandingViewModel
import com.ssafy.jaljara.utils.TokenHandler

@OptIn(ExperimentalComposeUiApi::class)
@Preview(showSystemUi = true)
@Composable
fun SignupScreen(navigate: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val nameFocusRequester by remember { mutableStateOf(FocusRequester()) }
    var isNameFocused by remember { mutableStateOf(false) }
    var userTypeButtonVisible by remember { mutableStateOf(false) }
    var parentCodeFieldVisible by remember { mutableStateOf(false) }
    var signupButtonVisible by remember { mutableStateOf(false) }
    var selectedValue by remember { mutableStateOf("") }
    var isNameValid by remember { mutableStateOf(false) }
    var isParentCodeValid by remember { mutableStateOf(false) }
    var userName by remember { mutableStateOf("") }
    var parentCode by remember { mutableStateOf("") }
    val items = listOf<String>("부모님", "아이")
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(color = Color.Transparent)
            .fillMaxSize()
            .padding(start = 40.dp, end = 40.dp)
            //.imePadding()
            .addFocusCleaner(focusManager)
    ) {
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = { Text("성함이 어떻게 되세요?") },
            singleLine = true,
            modifier = Modifier
                .background(color = Color.Gray.copy(alpha = 0.0f))
                .imePadding()
                .focusRequester(focusRequester = nameFocusRequester)
                .onFocusChanged {
                    isNameFocused = it.isFocused
                    when (userName.length) {
                        0 -> {
                            userTypeButtonVisible = false
                            parentCodeFieldVisible = false
                            signupButtonVisible = false
                        }
                        else -> {
                            userTypeButtonVisible = true
                            isNameValid = false
                        }
                    }
                },
            isError = isNameValid,
            colors = TextFieldDefaults.outlinedTextFieldColors(

                focusedBorderColor = Color.White.copy(alpha = 0.8f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                focusedLabelColor = Color.White.copy(alpha = 0.8f),
                unfocusedLabelColor = Color.White.copy(alpha = 0.4f),
                cursorColor = Color.White.copy(alpha = 0.8f),
                textColor = Color.White.copy(alpha = 0.8f),
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide(); focusManager.clearFocus() })
        )

        AnimatedVisibility(
            visible = userTypeButtonVisible,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 2500),
                initialAlpha = 0.01f
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)),

            modifier = Modifier
        ) {
            Row {
                items.forEach {
                    Button(
                        onClick = {
                            selectedValue = it
                            when (selectedValue) {
                                "부모님" -> {
                                    signupButtonVisible = true
                                    parentCodeFieldVisible = false
                                }
                                else -> {
                                    signupButtonVisible = false
                                    parentCodeFieldVisible = true
                                }
                            }
                            Log.e(
                                "SIGNUP",
                                "$signupButtonVisible $parentCodeFieldVisible"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =
                            if (selectedValue == it) Color.White
                            else Color.White.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.padding(start = 10.dp, end = 10.dp)
                    ) {
                        Text(text = it)
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = parentCodeFieldVisible,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 2500),
                initialAlpha = 0.01f
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 1000)),
            modifier = Modifier
        ) {
            OutlinedTextField(
                value = parentCode,
                onValueChange = { parentCode = it },
                label = { Text("인증번호를 입력해주세요") },
                singleLine = true,
                modifier = Modifier
                    .background(color = Color.Gray.copy(alpha = 0.0f))
                    .padding(0.dp)
                    .focusRequester(focusRequester = nameFocusRequester)
                    .onFocusChanged {
                        isNameFocused = it.isFocused
                        when (parentCode.length != 6) {
                            true -> {
                                signupButtonVisible = false
                                isParentCodeValid = false
                            }
                            false -> signupButtonVisible = true
                        }
                    },
                isError = isParentCodeValid,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                    unfocusedLabelColor = Color.White.copy(alpha = 0.4f),
                    cursorColor = Color.White.copy(alpha = 0.6f),
                    textColor = Color.White.copy(alpha = 0.6f)
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = { keyboardController?.hide(); focusManager.clearFocus() })
            )
        }

        AnimatedVisibility(
            visible = signupButtonVisible,
            enter = fadeIn(
                animationSpec = tween(durationMillis = 2500),
                initialAlpha = 0.01f
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 100)),

            modifier = Modifier
        ) {
            Button(
                onClick = {
                    isNameValid = IsNameValid(name = userName)
                    isParentCodeValid = IsParentCodeValid(code = parentCode)

                    when (isNameValid && isParentCodeValid) {
                        true -> TODO()
                        false -> TODO()
                    }
                },
                colors = ButtonDefaults.buttonColors(Color.White.copy(alpha = 1f)),
                shape = RoundedCornerShape(6.dp),
                modifier = Modifier.padding(start = 10.dp, end = 10.dp)
            ) {
                Text(text = "회원가입")
            }
        }
    }
}

fun IsNameValid(name: String): Boolean {
    if (name.length in 2..8)
        return true
    return false
}

fun IsParentCodeValid(code: String): Boolean {
    if (code.length == 6)
        return true
    return false
}

fun TryToSignup(name: String, parentCode: String?, viewModel: LandingViewModel): Boolean {
    //viewModel.signupWithExternalToken()
    return true;
}

fun Modifier.addFocusCleaner(focusManager: FocusManager, doOnClear: () -> Unit = {}): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(onTap = {
            doOnClear()
            focusManager.clearFocus()
        })
    }
}

@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}