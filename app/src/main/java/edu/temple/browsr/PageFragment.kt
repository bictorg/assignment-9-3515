package edu.temple.browsr

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider


class PageFragment : Fragment() {

    private val webView: WebView by lazy {
        view as WebView
    }

    // Fetch ViewModel instance scoped against parent fragment
    // This gives new instance for each tab. i.e. tabs are not sharing the currentURL LiveData,
    // each instance of TabFragment will have its own
    private val pageDataViewModel : PageDataViewModel by lazy {
        ViewModelProvider(requireParentFragment())[PageDataViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return  inflater.inflate(R.layout.fragment_page, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        with (webView) {
            settings.javaScriptEnabled = true

            // Allows knowledge of WebView's behavior
            webViewClient = object: WebViewClient() {
                // Be notified of the URL of a loaded page, regardless of why it was loaded
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    // Store current page URL in viewmodel
                    url?.run {
                        pageDataViewModel.setCurrentUrl(this)
                    }

                    // Add this to update the title
                    view?.title?.let { title ->
                        pageDataViewModel.setCurrentTitle(title)
                    }

                }
            }

            // Retrieve and restore WebView state, including current page and history
            savedInstanceState?.run{
                restoreState(this)
            }

        }
    }

    fun loadUrl(url: String) {
        if (url.isNotEmpty())
            webView.loadUrl(fixUrl(url))
    }

    fun goNext() {
        webView.goForward()
    }

    fun goBack() {
        webView.goBack()
    }

    // Process URL to fix specified malformed URLs or perform a search
    private fun fixUrl(url: String) : String {
        return if ((url.startsWith("https://")
                    || url.startsWith("http://"))
            && url.contains(Regex("[A-Za-z]+\\.[A-Za-z]+")))
            url
        else if (!(url.startsWith("https://")
                    || url.startsWith("http://"))
            && url.contains(Regex("[A-Za-z]+\\.[A-Za-z]+")))
            "https://$url"
        else
            searchUrl(url)
    }

    // Store WebView state, including current page and history
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        webView.saveState(outState)
    }

    private fun searchUrl (query: String) = "https://duckduckgo.com/?q=$query"

}