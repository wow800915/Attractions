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
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels() // 使用 Hilt 注入 ViewModel

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
                syncLanguageFromDB(dbLanguage)
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

    private fun syncLanguageFromDB(dbLanguage: String) {
        // 獲取當前配置的完整語言代碼
        val currentLocale = resources.configuration.locales.get(0)

        var currentDeviceLanguage: String? = null
        if (currentLocale.language == "zh") {
            if (currentLocale.country == "CN") {
                currentDeviceLanguage = "zh-cn"
            } else {
                currentDeviceLanguage = "zh-tw"
            }
        } else if (resources.getStringArray(R.array.language_values)
                .contains(currentLocale.language)
        ) {
            currentDeviceLanguage = currentLocale.language
        } else {
            // 如果不是預設支援的語言，則設定為繁體中文
            currentDeviceLanguage = "zh-tw"
        }
        if (dbLanguage != currentDeviceLanguage) {
            setLocale(dbLanguage!!)
            recreate()  // 重啟 Activity 以應用新的語言設置
        }
    }

    private fun setLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}