package kevinstar1.edu.cn.openlayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWebView = findViewById(R.id.wv_map);
        initWebView();

        Button btnCallJS = findViewById(R.id.btn_calljs);
        btnCallJS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //java调用js方法
                mWebView.loadUrl("javascript:javacalljs()");
            }
        });
        Button btnCallJsAlert = findViewById(R.id.btn_jsalert);
        btnCallJsAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.loadUrl("javascript:javacalljsalert()");
            }
        });
    }

    private void initWebView() {
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置允许调用js对话框
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.loadUrl("file:///android_asset/"+"openlayer.html");
        mWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        mWebView.addJavascriptInterface(MainActivity.this,"android");
    }
    @JavascriptInterface
    public void startToast(){
        Toast.makeText(this, "Hello Toast", Toast.LENGTH_SHORT).show();
    }
}
