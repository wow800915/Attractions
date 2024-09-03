package com.weiyou.attractions.ui.webview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.weiyou.attractions.R
import com.weiyou.attractions.databinding.FragmentWebviewBinding
import com.weiyou.attractions.ui.MainActivity
import com.weiyou.attractions.utils.listener.UpperBarBackBottonListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WebViewFragment : Fragment() {

    private var _binding: FragmentWebviewBinding? = null
    private val binding get() = _binding!!

    private val args: WebViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpperBar(args.title)
        binding.progressBar.visibility = View.VISIBLE
        setupWebView()
    }

    private fun setupWebView() {
        binding.webview.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(
                    view: WebView?,
                    url: String?,
                    favicon: android.graphics.Bitmap?
                ) {
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                }
            }
            loadUrl(args.url)
            settings.javaScriptEnabled = true  // 如果需要，启用JavaScript支持
        }
    }

    private fun setUpperBar(title: String) {
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
        binding.webview.destroy()  // 清理 WebView 資源
        _binding = null
    }
}