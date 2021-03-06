package com.andela.uberhack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.andela.uberhack.models.User;
import com.google.gson.Gson;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String user = Vars.getDB(this, "user", "user");

        if (!user.equals("user")) {
            Vars.user = new Gson().fromJson(user, User.class);
            if (Vars.user.response.uuid != null) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
        } else {
            setContentView(R.layout.activity_login);
        }

        Instance = this;
    }

    public static Activity Instance;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }


    private WebView webView;
    private Dialog authenticationDialog;

    public void authDialog(View view) {
        try {
            if (authenticationDialog == null) {
                authenticationDialog = new Dialog(this);
                authenticationDialog.setCancelable(true);
                authenticationDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                authenticationDialog.setContentView(R.layout.dialog_webview);
            }

            if (authenticationDialog != null) {
                try {
                    if (!authenticationDialog.isShowing()) {
                        authenticationDialog.show();
                        uberAuth();
                    } else {
                        authenticationDialog.cancel();
                        authenticationDialog = null;
                    }
                } catch (Exception e0) {
                    e0.printStackTrace();
                }

            }
        } catch (Exception e0) {
            e0.printStackTrace();
        }
    }

    private void uberHackAuth(String url) {
        Uri uri = Uri.parse(url);
        String code = uri.getQueryParameter("code");
        Vars.Toaster("Auth code:" + code, this, 0);
    }

    private void uberAuth() {
        final Activity self = this;
        webView = (WebView) authenticationDialog.findViewById(R.id.authWebView);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Vars.Toaster(url, self, 0);
                if (url.contains("code=")) {
                    webView.loadUrl("javascript:uber.getJSONString(document.body.innerText);");
                    authenticationDialog.hide();
                }
            }

        });
        webView = Vars.popUpWebView(webView, this);
        webView.loadUrl("https://login.uber.com/oauth/authorize?response_type=code&redirect_uri=https%3A%2F%2Fandelahack.herokuapp.com%2Fuber%2Fcallback&scope=profile&client_id=rr2NzvHi69QJalUHz0ImU1KidoE1KGc5");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
