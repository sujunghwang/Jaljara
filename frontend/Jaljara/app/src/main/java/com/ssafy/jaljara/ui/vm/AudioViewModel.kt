package com.ssafy.jaljara.ui.vm

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.launch
import java.io.IOException

class AudioViewModel : ViewModel(){

    var mediaPlayerUiState: UiState<MediaPlayer> by mutableStateOf(UiState.Loading)
        private set

    fun getMediaPlayer(url: String){
        viewModelScope.launch{

        }
    }
}