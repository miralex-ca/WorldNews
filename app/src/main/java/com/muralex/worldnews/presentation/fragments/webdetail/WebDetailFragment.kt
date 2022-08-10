package com.muralex.worldnews.presentation.fragments.webdetail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.muralex.worldnews.R
import com.muralex.worldnews.databinding.FragmentWebDetailBinding
import com.muralex.worldnews.presentation.activity.TempToolbarTitleListener
import com.muralex.worldnews.presentation.fragments.detail.DetailFragmentArgs
import com.muralex.worldnews.presentation.utils.ContactActions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class WebDetailFragment : Fragment() {
    private var _binding: FragmentWebDetailBinding? = null
    private val binding get() = _binding!!

    private val args : DetailFragmentArgs by navArgs()

    private var webView: WebView? = null
    @Inject
    lateinit var contactActions: ContactActions
    private var title = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentWebDetailBinding.inflate(inflater, container, false)

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val article = args.selectedArticle
        webView = binding.webview
        title = article.author

        setToolbarTitle()
        setMenu(article.url)
        initWebView(article.url)
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle()
    }

    private fun setToolbarTitle() {
        (requireActivity() as TempToolbarTitleListener).updateTitle(title)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(url: String) {

        webView?.webViewClient = object : WebViewClient() {
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


    private fun setMenu(url: String) {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_webdetail, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_share -> {
                        contactActions.shareURL(url)
                        true
                    }
                    R.id.action_refresh -> {
                        refreshPage(url)
                        true
                    }
                    R.id.action_open -> {
                        contactActions.openUrl(url)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner )
    }

    private fun refreshPage(url: String) {
        binding.loaderProgress.visibility = View.VISIBLE
        binding.webview.loadUrl(url)
    }

    private fun destroyWebView() {
        webView?.let {
            it.removeAllViews()
            it.clearHistory()
            it.clearCache(true)
            it.loadUrl("about:blank")
            it.onPause()
            it.removeAllViews()
            it.pauseTimers()
            it.destroy()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
       // destroyWebView()
        _binding = null
    }



    override fun onDestroy() {
        super.onDestroy()
        destroyWebView()
    }


}