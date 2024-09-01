package com.weiyou.attractions.ui.news

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.weiyou.attractions.R
import com.weiyou.attractions.databinding.FragmentNewsBinding
import com.weiyou.attractions.ui.MainActivity
import com.weiyou.attractions.utils.listener.UpperBarBackBottonListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize View Binding
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpperBar()

        val url = arguments?.getString("url") ?: "https://example.com"
        setupWebView(url)
    }

    private fun setupWebView(url: String) {
        binding.webview.apply {
            webViewClient = WebViewClient()  // 安全性考量，限制跳转至应用外部
            loadUrl(url)
            settings.javaScriptEnabled = true  // 如果需要，启用JavaScript支持
        }
    }

    private fun setUpperBar() {
        val title = getString(R.string.app_home_news) // 替换 your_string_id 为你的字符串资源ID

        val backListener = object : UpperBarBackBottonListener {
            override fun performAction() {
                if (isAdded) {
                    findNavController().popBackStack()
                }
            }
        }

        (activity as? MainActivity)?.setUpperBar(title, backListener, null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.webview.destroy()  // 清理 WebView 资源
        _binding = null
    }
}