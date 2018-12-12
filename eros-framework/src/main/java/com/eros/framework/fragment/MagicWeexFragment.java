package com.eros.framework.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.CycleInterpolator;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eros.framework.R;
import com.eros.framework.activity.MainActivity;
import com.eros.framework.config.DWebviewConfig;
import com.eros.framework.manager.impl.ModalManager;
import com.renrenyoupin.activity.BuildConfig;
import com.renrenyoupin.activity.common.listener.OnBackPressedListener;
import com.renrenyoupin.activity.common.util.Usp;
import com.renrenyoupin.activity.h5.api.CallHandlerApi;
import com.renrenyoupin.activity.h5.api.JavascriptInterfaceApi;
import com.renrenyoupin.activity.h5.widget.DragFloatActionButton;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.Objects;

import wendu.dsbridge.DWebView;

/**
 * Created by liuyuanxiao on 2018/5/25.
 */

public class MagicWeexFragment extends MainWeexFragment implements OnBackPressedListener {
    public static final String PAGE_URL = "rengerPageUrl";
    private static final String KEY_URL = "SPECIFIED_URL";
    public static final String TEST_API_METHOD = "file:///android_asset/demo.html";
    public static final String TEST_API = "file:///android_asset/testAPI.html";
    private View mLayout;
    private DWebView mDwebView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mLayout == null) {
            mLayout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_base_magic_main_layout, null);
            mContainer = mLayout.findViewById(R.id.layout_container);
        }

        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.setOnBackPressedListener(this);
        }
        return mLayout;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDwebView = initView(mLayout);

        showDefaultUrl();

        new JavascriptInterfaceApi(mDwebView).init();
        CallHandlerApi callHandlerApi = new CallHandlerApi(mDwebView);
        callHandlerApi.addValue();
        initFloatButton(mDwebView);
    }


    private DWebView initView(View myLayout) {
        DWebView dwebView = myLayout.findViewById(R.id.activity_wv);
        DWebviewConfig.init(dwebView);
        FragmentActivity activity = getActivity();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity != null) {
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        ModalManager.BmLoading.showLoading(activity, "", true);
        dwebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                ModalManager.BmLoading.dismissLoading(activity);
            }
        });

        return dwebView;
    }

    @SuppressLint("RestrictedApi")
    private void initFloatButton(DWebView dwebView) {
        final FragmentActivity activity = getActivity();
        if (!BuildConfig.DEBUG || activity == null) {
            return;
        }

        DragFloatActionButton floatBtn = mLayout.findViewById(R.id.floatBtn);
        floatBtn.setVisibility(View.VISIBLE);

        floatBtn.setOnClickListener(v -> {
            PopupMenu popupMenu = new PopupMenu(activity, v);
            activity.getMenuInflater().inflate(R.menu.pop_item, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                int i = menuItem.getItemId();
                if (i == R.id.action_refresh) {
                    dwebView.clearCache(true);
                    dwebView.reload();
                    Toast.makeText(activity, "已发起刷新请求", Toast.LENGTH_SHORT).show();
                } else if (i == R.id.action_clear_all) {
                    Usp.init(activity).clear().commit();
                    dwebView.clearCache(true);
                    dwebView.reload();
                    Toast.makeText(activity, "已全清", Toast.LENGTH_SHORT).show();
                } else if (i == R.id.action_api_url) {
                    loadUrl(TEST_API);
                } else if (i == R.id.action_api_method) {
                    loadUrl(TEST_API_METHOD);
                } else if (i == R.id.action_custom_url) {
                    showCustomUrlDialog();
                }

                return false;
            });
            popupMenu.show();

        });
    }


    private void loadUrl(String url) {
        mDwebView.loadUrl(url);
    }

    private void showDefaultUrl() {
        String url = Usp.init(Objects.requireNonNull(getActivity())).getString(KEY_URL, "");
        if (TextUtils.isEmpty(url)) {
            url = TEST_API;
        }

        loadUrl(url);
    }

    private void showCustomUrlDialog() {
        Context context = getActivity();
        if (context == null) {
            return;
        }
        EditText input = new EditText(context);
        AlertDialog dialog = new AlertDialog
            .Builder(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth)
            .setTitle("请输入网址")
            .setView(input)
            .setPositiveButton(android.R.string.ok, null)
            .setCancelable(true)
            .create();
        // https://stackoverflow.com/questions/2620444/how-to-prevent-a-dialog-from-closing-when-a-button-is-clicked
        dialog.setOnShowListener(dialogInterface -> {
            Button positiveBtn = ((AlertDialog) dialogInterface).getButton(AlertDialog.BUTTON_POSITIVE);
            positiveBtn.setOnClickListener(v1 -> {
                String text = input.getText().toString();
                if (!URLUtil.isValidUrl(text)) {
                    Objects.requireNonNull(dialog.getWindow())
                        .getDecorView()
                        .animate()
                        .translationX(16f)
                        .setInterpolator(new CycleInterpolator(7f));
                    Toast.makeText(context, "网址无效", Toast.LENGTH_SHORT).show();
                    return;
                }

                Usp.init(context).putString(KEY_URL, text).commit();
                loadUrl(text);
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    @Override
    public boolean doBack() {
        if (mDwebView.canGoBack()) {
            mDwebView.goBack();
            return true;
        }
        return false;
    }
}
