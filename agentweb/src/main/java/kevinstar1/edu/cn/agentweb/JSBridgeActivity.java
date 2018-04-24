package kevinstar1.edu.cn.agentweb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import kevinstar1.edu.cn.agentweb.jsbridge.BridgeImpl;
import kevinstar1.edu.cn.agentweb.jsbridge.JSBridge;
import kevinstar1.edu.cn.agentweb.jsbridge.JSBridgeWebChromeClient;

public class JSBridgeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsbridge);
        WebView webView = findViewById(R.id.wv_jsbridge);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebChromeClient(new JSBridgeWebChromeClient());
        webView.loadUrl("file:///android_asset/index.html");
        JSBridge.register("bridge", BridgeImpl.class);
    }
}
