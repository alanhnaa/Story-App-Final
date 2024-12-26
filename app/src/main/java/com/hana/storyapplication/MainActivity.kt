package com.hana.storyapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hana.storyapplication.data.adapter.LoadingAdapter
import com.hana.storyapplication.data.adapter.StoryAdapter
import com.hana.storyapplication.databinding.ActivityMainBinding
import com.hana.storyapplication.view.ViewModelFactory
import com.hana.storyapplication.view.main.MainViewModel
import com.hana.storyapplication.view.maps.MapsActivity
import com.hana.storyapplication.view.uploadstory.UploadStoryActivity
import com.hana.storyapplication.view.welcome.WelcomeActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.addStory.setOnClickListener {
            val intent = Intent(this, UploadStoryActivity::class.java)
            startActivity(intent)
        }

        binding.maps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.logout()
            }
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        getSession()
    }

    private fun getSession() {
        val adapter = StoryAdapter()
        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                lifecycleScope.launch {
                    viewModel.getStory.observe(this@MainActivity) { result ->
                        adapter.submitData(lifecycle, result)
                        showLoading(false)
                    }
                }
            }
        }
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            this.adapter =
                adapter.withLoadStateHeaderAndFooter(
                    header = LoadingAdapter { adapter.retry() },
                    footer = LoadingAdapter { adapter.retry() }
                )
        }
    }


    private fun showLoading(state: Boolean) {
        if (state) binding.progressBarMain.visibility = View.VISIBLE
        else binding.progressBarMain.visibility = View.GONE
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}