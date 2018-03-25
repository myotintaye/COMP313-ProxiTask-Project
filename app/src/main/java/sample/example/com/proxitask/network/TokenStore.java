package sample.example.com.proxitask.network;

/**
 * Created by Myo on 3/24/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;

public class TokenStore {
    private final static String SHARED_PREF_NAME = "sample.example.com.proxitask.network.AUTH_TOKEN";
    private final static String TOKEN_KEY = "sample.example.com.proxitask.network.AUTH_TOKEN_KEY";

    public static String getToken(Context c) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, "");
    }

    public static void setToken(Context c, String token) {
        SharedPreferences prefs = c.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(TOKEN_KEY, token);
        editor.apply();
    }

}

