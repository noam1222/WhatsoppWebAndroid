package com.example.whatsopwebandorid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Tools {

    private static final String apiStringPattern = "^http://(.*?):(\\d+)/([^/]+)/$";
    public static final String defaultApiConnectString ="http://10.0.2.2:5000/api/";

    public static String getApiConnectionString(Context appContext) {
        SharedPreferences connectionStringPref = appContext.
                getSharedPreferences("connectionString", Context.MODE_PRIVATE);
        String connectionString = connectionStringPref.getString("connectionString", null);
        if (connectionString == null)
            return defaultApiConnectString;
        return connectionString;
    }

    public static void setApiStringPattern(Context appContext, String connectionString) {
        SharedPreferences connectionStringPref = appContext.
                getSharedPreferences("connectionString", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = connectionStringPref.edit();
        editor.putString("connectionString", connectionString);
        editor.apply();
    }

    public static boolean isGoodConnectionString(String apiConnectString) {
        return apiConnectString.matches(apiStringPattern);
    }
    public static String extractImageViewBase64(String webBase64) {
        int start = webBase64.indexOf(",");
        return webBase64.substring(start +1);
    }

    public static String extractTimeFormat(String rawTime) {
        int tIndex = rawTime.indexOf("T");
        int  colonIndex = rawTime.lastIndexOf(":");
        return rawTime.substring(tIndex + 1, colonIndex);
    }

    public static Bitmap Base64ToBitmap(String img) {
        byte[] decodedBytes = Base64.decode(img, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
