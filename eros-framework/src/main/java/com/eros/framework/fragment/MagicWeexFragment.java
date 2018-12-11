package com.eros.framework.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.eros.framework.R;
import com.eros.framework.config.DWebviewConfig;

import wendu.dsbridge.DWebView;

/**
 * Created by liuyuanxiao on 2018/5/25.
 */

public class MagicWeexFragment extends MainWeexFragment {
    public static final String PAGE_URL = "rengerPageUrl";
    private View myLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (myLayout == null) {
            myLayout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_base_magic_main_layout, null);
            mContainer = (ViewGroup) myLayout.findViewById(R.id.layout_container);
        }
        return myLayout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        DWebView dwebView = initView(myLayout);
        WebView wv = (WebView) myLayout.findViewById(R.id.activity_wv);
        wv.setScrollbarFadingEnabled(true);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(wv.getContext().getCacheDir().getAbsolutePath());
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBlockNetworkImage(false);

        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                System.out.println("============onPageFinished");
                super.onPageFinished(view, url);
            }
        });
        wv.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);

            }

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                boolean b = super.onConsoleMessage(consoleMessage);
                return b;

            }
        });
        wv.loadUrl("http://hyd2.renrenyoupin.com/");
    }

    private DWebView initView(View myLayout) {
        DWebView dwebView = myLayout.findViewById(R.id.activity_wv);
        DWebviewConfig.init(dwebView);
        FragmentActivity activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        return dwebView;
    }
}
