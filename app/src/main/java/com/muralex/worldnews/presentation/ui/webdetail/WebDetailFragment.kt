package com.muralex.worldnews.presentation.ui.webdetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.muralex.worldnews.databinding.FragmentWebDetailBinding
import com.muralex.worldnews.presentation.utils.Constants.ARTICLE_URL
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class WebDetailFragment : Fragment() {
     private var _binding: FragmentWebDetailBinding? = null
     private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWebDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val url = requireArguments().getString(ARTICLE_URL).toString()
        binding.webview.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                lifecycleScope.launch {
                    delay(400)
                    _binding?.loaderProgress?.visibility = View.INVISIBLE
                }
            }

            override fun onPageFinished(view: WebView, url: String) {
                _binding?.loaderProgress?.visibility = View.INVISIBLE
            }
        }

        binding.webview.settings.javaScriptEnabled = true
        binding.webview.loadUrl(url)
    }

    private fun destroyWebView() {
        binding.webview.apply {
            removeAllViews()
            clearHistory()
            clearCache(true)
            loadUrl("about:blank")
            onPause()
            removeAllViews()
            pauseTimers()
            destroy()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        destroyWebView()
        _binding = null
    }

}