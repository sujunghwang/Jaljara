package com.ssafy.jaljara.ui.login

import android.content.Intent
import android.util.Log
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.SignInButton
import com.ssafy.jaljara.Activities.GoogleLoginActivity
import com.ssafy.jaljara.databinding.FragmentUserLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentUserLoginBinding? = null
    private var kakaoLoginBtn: ImageButton? = null
    private var googleLoginBtn: SignInButton? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val loginViewModel =
            ViewModelProvider(this).get(LoginViewModel::class.java)

        _binding = FragmentUserLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val loginTextView: TextView = binding.loginText
        loginViewModel.sampleText.observe(viewLifecycleOwner) {
            loginTextView.text = it
        }

        val context = requireContext()
        kakaoLoginBtn = binding.kakaoLoginButton
        kakaoLoginBtn!!.setOnClickListener(KakaoLoginWorker.kakaoLoginBtnListener)

        googleLoginBtn = binding.googleLoginButton
        googleLoginBtn!!.setOnClickListener{
            Log.e("jaljara", "Google Login Clicked")
            //inflater.inflate(R.layout.activity_google_login, container, false);
            val intent = Intent(activity, GoogleLoginActivity::class.java)
            activity?.startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}