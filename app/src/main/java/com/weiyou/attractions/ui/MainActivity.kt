package com.weiyou.attractions.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.weiyou.attractions.R
import com.weiyou.attractions.databinding.ActivityMainBinding
import com.weiyou.attractions.utils.listener.UpperBarBackBottonListener
import com.weiyou.attractions.utils.listener.UpperBarRightBottonListener
import com.weiyou.attractions.utils.LanguageUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // 觀察語言設置並更新配置，僅在語言變更時重啟 Activity
        mainViewModel.language.observe(this) { dbLanguage ->
            if (dbLanguage != null) {
                LanguageUtil.syncLanguage(this, dbLanguage)
            }
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