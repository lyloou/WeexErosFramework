package com.eros.framework.config;

import android.content.Context;
import android.os.Build;

import com.tencent.smtt.export.external.extension.interfaces.IX5WebViewExtension;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import wendu.dsbridge.DWebView;

public class DWebviewConfig {
    public static void init(DWebView dwebView) {
        Context context = dwebView.getContext();

        IX5WebViewExtension x5WebViewExtension = dwebView.getX5WebViewExtension();
        if (x5WebViewExtension != null) {
            x5WebViewExtension.setScrollBarFadingEnabled(false);
        }
        WebSettings settings = dwebView.getSettings();
        settings.setUseWideViewPort(true);
        // 允许js弹出窗口
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置编码
        settings.setDefaultTextEncodingName("UTF-8");
        //设置支持DomStorage
        settings.setDomStorageEnabled(true);
        // 实现8倍缓存
        settings.setAppCacheMaxSize(Long.MAX_VALUE);
        settings.setAllowFileAccess(true);
        // 开启Application Cache功能
        settings.setAppCacheEnabled(true);
        //取得缓存路径
        String appCachePath = context.getCacheDir().getAbsolutePath();
//        String chejusPath = getFilesDir().getAbsolutePath()+ APP_CACHE_DIRNAME;
        //设置路径
        //API 19 deprecated
        settings.setDatabasePath(appCachePath);
        // 设置Application caches缓存目录
        settings.setAppCachePath(appCachePath);
        //是否启用数据库
        settings.setDatabaseEnabled(true);
        //设置存储模式 建议缓存策略为，判断是否有网络，有的话，使用LOAD_DEFAULT,无网络时，使用LOAD_CACHE_ELSE_NETWORK
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        //设置不支持字体缩放
        settings.setSupportZoom(true);
        //设置对应的cookie具体设置有子类重写该方法来实现
        //还有一种是加载https的URL时在5.0以上加载不了，5.0以下可以加载，这种情况可能是网页中存在非https得资源，在5.0以上是默认关闭，需要设置，
//		loadWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowUniversalAccessFromFileURLs(true);
        }

        dwebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                return false;
            }
        });
    }
}
