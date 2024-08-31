package com.weiyou.attractions.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.weiyou.attractions.R
import com.weiyou.attractions.databinding.ActivityMainBinding
import com.weiyou.attractions.utils.listener.UpperBarBackBottonListener
import com.weiyou.attractions.utils.listener.UpperBarRightBottonListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels() // 使用 Hilt 注入 ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 觀察 ViewModel 中的數據變化並更新 UI
        mainViewModel.text.observe(this) { text ->
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        }
    }

    fun setUpperBar(
        title: String,
        upperBarBackBottonListener: UpperBarBackBottonListener?,
        upperBarRightBottonListener: UpperBarRightBottonListener?
    ) {
        binding.tvTitle.text = title
        if (upperBarBackBottonListener == null) {
            binding.ivBack.visibility = View.GONE
        } else {
            binding.ivBack.visibility = View.VISIBLE
            binding.ivBack.setOnClickListener {
                upperBarBackBottonListener.performAction()
            }
        }

        if (upperBarRightBottonListener == null) {
            binding.ivLang.visibility = View.GONE
        } else {
            binding.ivLang.visibility = View.VISIBLE
            binding.ivLang.setOnClickListener {
                upperBarRightBottonListener.performAction()
            }
        }

    }

}