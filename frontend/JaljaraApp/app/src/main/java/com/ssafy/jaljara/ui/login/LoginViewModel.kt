package com.ssafy.jaljara.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    private val _sampleText = MutableLiveData<String>().apply{
        value = "This is Login View"
    }
    val sampleText: LiveData<String> = _sampleText
}