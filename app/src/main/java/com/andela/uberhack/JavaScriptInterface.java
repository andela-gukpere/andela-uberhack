package com.andela.uberhack;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.andela.uberhack.models.User;
import com.google.gson.Gson;


/**
 * Created by goodson on 7/8/15.
 */
public class JavaScriptInterface {
    public JavaScriptInterface(Activity c, WebView v) {
        mc = c;
    }
    private Activity mc;

    @JavascriptInterface
    public void getJSONString(final String json) {

        Vars.user = new Gson().fromJson(json, User.class);
        if(Vars.user.response.uuid != null) {
            Vars.saveDB("user",json, mc);
            mc.finish();
            mc.startActivity(new Intent(mc, MainActivity.class));
        }
    }

    @JavascriptInterface
    public void getJSONCalendarString(final String json) {

        Vars.user = new Gson().fromJson(json, User.class);
        if(Vars.user.response.uuid != null) {
            Vars.saveDB("user",json, mc);
            mc.finish();
            mc.startActivity(new Intent(mc, MainActivity.class));
        }
    }
}
