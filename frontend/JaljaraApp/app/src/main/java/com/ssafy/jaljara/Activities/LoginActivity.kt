package com.ssafy.jaljara.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.SignInButton
import com.ssafy.jaljara.databinding.FragmentUserLoginBinding
import com.ssafy.jaljara.utils.Log

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: FragmentUserLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = FragmentUserLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}