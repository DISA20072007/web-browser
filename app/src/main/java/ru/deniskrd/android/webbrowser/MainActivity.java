package ru.deniskrd.android.webbrowser;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

import ru.deniskrd.android.webbrowser.db.entities.UserSetting.Settings;

public class MainActivity extends AppCompatActivity {

    private PropertiesHelper propertiesHelper;

    private WebView webView;

    private boolean isError = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        propertiesHelper = new PropertiesHelper(getApplicationContext());

        propertiesHelper.loadProperties();

        initWebBrowser();
        initToolbarNavigation();

        addLongClickHandlers();

        initSearch();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        propertiesHelper.saveProperties();
    }

    public void search(View view) {
        CardView searchView = findViewById(R.id.search_view);
        searchView.setVisibility(View.VISIBLE);

        EditText searchEditText = findViewById(R.id.search_text_input);
        searchEditText.requestFocus();
    }

    public void searchNext(View view) {
        webView.findNext(true);
    }

    public void searchPrevious(View view) {
        webView.findNext(false);
    }

    public void cancelSearch(View view) {
        CardView searchView = findViewById(R.id.search_view);
        searchView.setVisibility(View.GONE);
    }

    public void openUrl(View view) {
        TextView webSiteInput = findViewById(R.id.website_address_input);

        String url = webSiteInput.getText().toString();
        if (!URLUtil.isNetworkUrl(url)) {
            webSiteInput.setError("");
        }
        webView.loadUrl(webSiteInput.getText().toString());
    }

    public void onClickToolbarButton(View view) {
        ToggleButton toggleButton = (ToggleButton) view;

        switch (view.getId()) {
            case R.id.home_button:
                String homePageUrl = propertiesHelper.getUserSettings().getProperty(String.valueOf(Settings.HOME_PAGE));
                if (homePageUrl != null) {
                    webView.loadUrl(homePageUrl);
                    toggleButton.setChecked(true);
                } else toggleButton.setChecked(false);

                break;
            case R.id.back_button:
                if (!toggleButton.isChecked()) {
                    webView.goBack();
                }
                toggleButton.setChecked(webView.canGoBack());
                break;
            case R.id.next_button:
                if (!toggleButton.isChecked()) {
                    webView.goForward();
                }
                toggleButton.setChecked(webView.canGoForward());
                break;
            case R.id.refresh_button:
                if (!isError && !toggleButton.isChecked()) {
                    webView.reload();
                }
                toggleButton.setChecked(!isError);
                break;
            case R.id.favourite_button:
                if (!isError) {
                    if (!toggleButton.isChecked()) {
                        propertiesHelper.getFavourites().remove(webView.getTitle());
                    } else {
                        propertiesHelper.getFavourites().setProperty(webView.getTitle(), webView.getUrl());
                    }
                } else toggleButton.setChecked(false);
                break;
        }
    }

    private void initSearch() {
        final EditText searchEditText = findViewById(R.id.search_text_input);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable searchString) {
                String searchText = searchString.toString();

                if (!searchText.isEmpty()) {
                    webView.findAllAsync(searchText);
                }
            }
        });
    }

    private void initToolbarNavigation() {
        Toolbar toolbar = findViewById(R.id.navigation_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
    }

    private void initWebBrowser() {
        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new SimpleWebViewClient());
        webView.loadUrl(propertiesHelper.getUserSettings().getProperty(String.valueOf(Settings.HOME_PAGE)));
    }

    private void addLongClickHandlers() {
        Button button = findViewById(R.id.home_button);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToggleButton toggleButton = (ToggleButton) view;
                if (webView.getUrl() != null && !toggleButton.isChecked()) {
                    propertiesHelper.getUserSettings().setProperty(Settings.HOME_PAGE.toString(), webView.getUrl());
                    toggleButton.setChecked(true);
                }
                return true;
            }
        });

        button = findViewById(R.id.favourite_button);
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
               showFavouritesList();
               return true;
            }
        });
    }

    private void showFavouritesList() {
        final CardView favouritesView = findViewById(R.id.favourites_view);

        List<String> data = new ArrayList<>(propertiesHelper.getFavourites().stringPropertyNames());

        final ListView favouritesList = findViewById(R.id.favourites_list);
        favouritesList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data.toArray(new String[]{})));

        favouritesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                webView.loadUrl(propertiesHelper.getFavourites().getProperty(textView.getText().toString()));

                favouritesView.setVisibility(View.GONE);
            }
        });

        favouritesView.setVisibility(View.VISIBLE);
    }

    private class SimpleWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            ToggleButton toggleButton = findViewById(R.id.home_button);
            toggleButton.setChecked(url.equals(propertiesHelper.getUserSettings().getProperty(Settings.HOME_PAGE.toString())));

            toggleButton = findViewById(R.id.back_button);
            toggleButton.setChecked(webView.canGoBack());

            toggleButton = findViewById(R.id.next_button);
            toggleButton.setChecked(webView.canGoForward());

            toggleButton = findViewById(R.id.favourite_button);
            toggleButton.setChecked(propertiesHelper.getFavourites().values().contains(url));

            isError = false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            isError = isError || !URLUtil.isNetworkUrl(url);

            ToggleButton toggleButton = findViewById(R.id.refresh_button);
            toggleButton.setChecked(!isError);

            if (!isError) {
                TextView webSiteInput = findViewById(R.id.website_address_input);
                webSiteInput.setText(webView.getUrl());
            }
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);

            isError = true;
        }
    }

}
