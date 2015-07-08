package com.andela.uberhack;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Vars.user.response.google_token == null) {

        } else {
            findViewById(R.id.google_plus_button).setVisibility(View.GONE);
        }
    }

    private WebView webView;
    private Dialog authenticationDialog;
    public void authDialog (View view) {
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
    private void uberHackAuth (String url) {
        Uri uri = Uri.parse(url);
        String code = uri.getQueryParameter("code");
        Vars.Toaster("Auth code:"  + code, this, 0);
    }

    private void uberAuth () {
        final Activity self = this;
        webView = (WebView)authenticationDialog.findViewById(R.id.authWebView);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode,String description, String failingUrl) {
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
                    authenticationDialog.hide();
                    uberHackAuth(url);
                }
            }

        });
        webView = Vars.popUpWebView(webView, this);
        webView.loadUrl("https://accounts.google.com/o/oauth2/auth?access_type=offline&scope=https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fcalendar.readonly&response_type=code&client_id=453264479059-fc56k30fdhl07leahq5n489ct7ifk7md.apps.googleusercontent.com&redirect_uri=https%3A%2F%2Fandelahack.herokuapp.com%2Fcalendar%2Fcallback");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
