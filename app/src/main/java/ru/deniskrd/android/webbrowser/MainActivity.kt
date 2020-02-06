package ru.deniskrd.android.webbrowser

import android.graphics.Bitmap
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.webkit.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import ru.deniskrd.android.webbrowser.db.AppConstants.HOME_PAGE

class MainActivity : AppCompatActivity() {

    private var propertiesHelper: PropertiesHelper? = null

    private var webView: WebView? = null

    private var isError = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        propertiesHelper = PropertiesHelper(applicationContext)
        propertiesHelper!!.loadProperties()

        initWebBrowser()
        initToolbarNavigation()

        addLongClickHandlers()

        initSearch()
    }

    override fun onDestroy() {
        super.onDestroy()
        propertiesHelper!!.saveProperties()
    }

    fun search(view: View) {
        val searchView : CardView = findViewById(R.id.search_view)
        searchView.visibility = View.VISIBLE

        val searchEditText : EditText = findViewById(R.id.search_text_input)
        searchEditText.requestFocus()
    }

    fun searchNext(view: View) {
        webView!!.findNext(true)
    }

    fun searchPrevious(view: View) {
        webView!!.findNext(false)
    }

    fun cancelSearch(view: View) {
        val searchView : CardView = findViewById(R.id.search_view)
        searchView.visibility = View.GONE
    }

    fun openUrl(view: View) {
        val webSiteInput : EditText = findViewById(R.id.website_address_input)

        val url = webSiteInput.text.toString()
        if (!URLUtil.isNetworkUrl(url)) {
            webSiteInput.error = resources.getString(R.string.invalid_url_error_message)
        } else {
            webView!!.loadUrl(url)
        }
    }

    fun onClickToolbarButton(view: View) {
        val toggleButton = view as ToggleButton

        when (view.getId()) {
            R.id.home_button -> {
                val homePageUrl = propertiesHelper!!.userSettings.getProperty(HOME_PAGE.name)
                if (homePageUrl != null) {
                    webView!!.loadUrl(homePageUrl)
                    toggleButton.isChecked = true
                } else
                    toggleButton.isChecked = false
            }
            R.id.back_button -> {
                if (!toggleButton.isChecked) {
                    webView!!.goBack()
                }
                toggleButton.isChecked = webView!!.canGoBack()
            }
            R.id.next_button -> {
                if (!toggleButton.isChecked) {
                    webView!!.goForward()
                }
                toggleButton.isChecked = webView!!.canGoForward()
            }
            R.id.refresh_button -> {
                if (!isError && !toggleButton.isChecked) {
                    webView!!.reload()
                }
                toggleButton.isChecked = !isError
            }
            R.id.favourite_button -> if (!isError) {
                if (!toggleButton.isChecked) {
                    propertiesHelper!!.favourites.remove(webView!!.title)
                } else {
                    propertiesHelper!!.favourites.setProperty(webView!!.title, webView!!.url)
                }
            } else
                toggleButton.isChecked = false
        }
    }

    private fun initSearch() {
        val searchEditText : EditText = findViewById(R.id.search_text_input)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(searchString: Editable) {
                val searchText = searchString.toString()

                if (searchText.isNotEmpty()) {
                    webView!!.findAllAsync(searchText)
                }
            }
        })
    }

    private fun initToolbarNavigation() {
        val toolbar : androidx.appcompat.widget.Toolbar = findViewById(R.id.navigation_toolbar)
        toolbar.title = ""
        setSupportActionBar(toolbar)
    }

    private fun initWebBrowser() {
        webView = findViewById(R.id.webView)

        val webSettings = webView!!.settings
        webSettings!!.javaScriptEnabled = true

        webView!!.webViewClient = SimpleWebViewClient()
        webView!!.loadUrl(propertiesHelper!!.userSettings.getProperty(HOME_PAGE.name))
    }

    private fun addLongClickHandlers() {
        var button : Button = findViewById(R.id.home_button)
        button.setOnLongClickListener { view ->
            val toggleButton = view as ToggleButton
            if (webView!!.url != null && !toggleButton.isChecked) {
                propertiesHelper!!.userSettings.setProperty(HOME_PAGE.name, webView!!.url)
                toggleButton.isChecked = true
            }
            true
        }

        button = findViewById(R.id.favourite_button)
        button.setOnLongClickListener {
            showFavouritesList()
            true
        }
    }

    private fun showFavouritesList() {
        val favouritesView : CardView = findViewById(R.id.favourites_view)

        val data = ArrayList(propertiesHelper!!.favourites.stringPropertyNames())

        val favouritesList : ListView = findViewById(R.id.favourites_list)
        favouritesList.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data.toTypedArray())

        favouritesList.setOnItemClickListener{ parent, view, position, id ->
            val textView = view as TextView
            webView!!.loadUrl(propertiesHelper!!.favourites.getProperty(textView.text.toString()))

            favouritesView.visibility = View.GONE
        }

        favouritesView.visibility = View.VISIBLE
    }

    private inner class SimpleWebViewClient : WebViewClient() {
        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap) {
            super.onPageStarted(view, url, favicon)

            var toggleButton : ToggleButton = findViewById(R.id.home_button)
            toggleButton.isChecked = (url == propertiesHelper!!.userSettings.getProperty(HOME_PAGE.name))

            toggleButton = findViewById(R.id.back_button)
            toggleButton.isChecked = webView!!.canGoBack()

            toggleButton = findViewById(R.id.next_button)
            toggleButton.isChecked = webView!!.canGoForward()

            toggleButton = findViewById(R.id.favourite_button)
            toggleButton.isChecked = propertiesHelper!!.favourites.values.contains(url)

            isError = false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)

            isError = isError || !URLUtil.isNetworkUrl(url)

            val toggleButton : ToggleButton = findViewById(R.id.refresh_button)
            toggleButton.isChecked = !isError

            if (!isError) {
                val webSiteInput : EditText = findViewById(R.id.website_address_input)
                webSiteInput.setText(webView!!.url)
            }
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            super.onReceivedError(view, request, error)

            isError = true
        }
    }
}
