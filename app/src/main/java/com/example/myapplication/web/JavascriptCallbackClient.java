package com.example.myapplication.web;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.SecondFragment;

public class JavascriptCallbackClient {

    private MainActivity activity;
    private Activity mContext;
    private WebView webView;

    public JavascriptCallbackClient(Activity activity, WebView webView) {
        this.mContext = activity;
        this.webView = webView;
    }

    private String publishEvent(String functionName, String data) {
        StringBuffer buffer = new StringBuffer()
                .append("window.dispatchEvent(\n")
                .append("   new CustomEvent(\"").append(functionName).append("\", {\n")
                .append("           detail: {\n")
                .append("               data: ").append(data).append("\n")
                .append("           }\n")
                .append("       }\n")
                .append("   )\n")
                .append(");");
        return buffer.toString();
    }

    @JavascriptInterface
    public void showToastMessage(final String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @JavascriptInterface
    public void showAlertMessage() {
        webView.postDelayed(() -> {
            webView.evaluateJavascript(publishEvent("showAlertMessage", "\"Hello, I'm message from Android\""),
                    (result) -> {
                        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                    }
            );
        }, 5000);
    }

    @JavascriptInterface
    public void showToastMessageIncludingData(final String message) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                String value = "\"" + message + " 그리고 K상남자~!" + "\"";

                webView.evaluateJavascript(publishEvent("showToastMessageIncludingData", value),
                        (result) -> {
                            Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
                        }
                );
            }
        });
    }

    @JavascriptInterface
    public void test() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @JavascriptInterface
    public void finish() {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mContext.finish();
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
