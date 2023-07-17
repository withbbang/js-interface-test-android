package com.example.myapplication.web;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;
import com.example.myapplication.TestActivity;

public class JavascriptCallbackClient {

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
    public void test(final String message) {
        webView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent intent = new Intent(mContext, TestActivity.class);
                    mContext.startActivityForResult(intent, 0);
                } catch (Exception e) {
                    Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
