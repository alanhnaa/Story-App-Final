package com.hana.storyapplication.view.signup

    import android.content.Intent
    import android.os.Build
    import android.os.Bundle
    import android.view.WindowInsets
    import android.view.WindowManager
    import android.widget.Toast
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.lifecycleScope
    import com.hana.storyapplication.R
    import com.hana.storyapplication.data.Result
    import com.hana.storyapplication.data.response.RegisterResponse
    import com.hana.storyapplication.databinding.ActivitySignupBinding
    import com.hana.storyapplication.view.ViewModelFactory
    import com.hana.storyapplication.view.login.LoginActivity
    import com.google.gson.Gson
    import kotlinx.coroutines.launch
    import retrofit2.HttpException

    class SignupActivity : AppCompatActivity() {


        private val viewModel by viewModels<SignupViewModel> {
            ViewModelFactory.getInstance(this)
        }
        private var _binding: ActivitySignupBinding? = null
        private val binding get() = _binding!!

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            _binding = ActivitySignupBinding.inflate(layoutInflater)
            setContentView(binding.root)
            showLoading(false)

            setupView()
            setupAction()
        }

        private fun setupView() {
            @Suppress("DEPRECATION")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                window.insetsController?.hide(WindowInsets.Type.statusBars())
            } else {
                window.setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
                )
            }
            supportActionBar?.hide()
        }

        private fun setupAction() {
            binding.signupButton.setOnClickListener {
                showLoading(true)
                val name = binding.nameEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()

                if (name.isEmpty()) {
                    binding.nameEditText.error = getString(R.string.fill_username)
                } else if (email.isEmpty()) {
                    binding.emailEditText.error = getString(R.string.fill_email)
                } else if (password.isEmpty()) {
                    binding.passwordEditText.error = getString(R.string.fill_email)
                }

                lifecycleScope.launch {
                    try {
                        viewModel.register(name, email, password)
                            .observe(this@SignupActivity) { result ->
                                if (result != null) {
                                    when (result) {
                                        is Result.Loading -> {
                                            showLoading(true)
                                        }

                                        is Result.Success -> {
                                            showLoading(false)
                                            showToast(getString(R.string.login))
                                            val intent =
                                                Intent(this@SignupActivity, LoginActivity::class.java)
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                            startActivity(intent)
                                            finish()
                                        }

                                        is Result.Error -> {
                                            showLoading(false)
                                            showToast(result.error)
                                        }
                                    }
                                }
                            }
                    } catch (e: HttpException) {
                        showLoading(false)
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, RegisterResponse::class.java)
                        showToast(errorResponse.message)
                    }
                }


            }
        }

        private fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }

        private fun showLoading(isLoading: Boolean) {
            binding.progressBarSignup.visibility =
                if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
            binding.signupButton.isEnabled = !isLoading
        }
}
