package com.epom.android.managers;

import android.content.Context;
import android.util.Log;
import com.epom.android.Util;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.*;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 4/23/12
 * Time: 9:53 AM
 */
public class CookiesManager {
    public static final String COOKIES_STORAGE = "cookies.storage";
    private final DefaultHttpClient httpClient;
    private Context context;

    public CookiesManager(DefaultHttpClient httpClient, Context context){
        this.httpClient = httpClient;
        this.context = context;
    }

    public void persistCookies() {
        int i = 0;
        Log.d(Util.EPOM_LOG_TAG, "Persisting cookies...");
        EpomCookieStore store = new EpomCookieStore();
        for (Cookie c : httpClient.getCookieStore().getCookies()) {
            Log.d(Util.EPOM_LOG_TAG, "Serializing cookie: " + c);
            store.addCookie(c);
            i++;
        }

        FileOutputStream fos;
        try {
            fos = context.openFileOutput(COOKIES_STORAGE, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            Log.d(Util.EPOM_LOG_TAG, "File not found: " + COOKIES_STORAGE);
            return;
        }
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(fos);
            os.writeObject(store);
        } catch (IOException e) {
            Log.d(Util.EPOM_LOG_TAG, "IO Exception occurred.");
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    Log.d(Util.EPOM_LOG_TAG, "Error while closing stream.");
                }
            }
        }
        Log.d(Util.EPOM_LOG_TAG, "Done persisting cookies. Cookies persisted: " + i);
    }

    public void restoreCookies() {
        Log.d(Util.EPOM_LOG_TAG, "Restoring cookies...");
        int i = 0;
        FileInputStream fis;
        try {
            fis = context.openFileInput(COOKIES_STORAGE);
        } catch (FileNotFoundException e) {
            Log.d(Util.EPOM_LOG_TAG, "File not found: " + COOKIES_STORAGE);
            return;
        }
        ObjectInputStream is = null;
        try {
            is = new ObjectInputStream(fis);
        } catch (IOException e) {
            Log.d(Util.EPOM_LOG_TAG, "IO Exception occurred");
        }
        EpomCookieStore store = null;
        if (is != null) {
            try {
                store = (EpomCookieStore) is.readObject();
            } catch (ClassNotFoundException e) {
                Log.d(Util.EPOM_LOG_TAG, "Class not found: " + EpomCookieStore.class);
            } catch (IOException e) {
                Log.d(Util.EPOM_LOG_TAG, "IOException occured");
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                Log.d(Util.EPOM_LOG_TAG, "IOException occured");
            }
        }
        if (store != null && store.getCookies() != null) {
            for (SerializableCookie c : store.getCookies()) {
                i++;
                httpClient.getCookieStore().addCookie(c.getCookie());
            }
        }
        Log.d(Util.EPOM_LOG_TAG, "Done restoring cookies. Restored cookies: " + i);
        httpClient.getCookieStore().clearExpired(new Date());
        Log.d(Util.EPOM_LOG_TAG, "Clearing expired cookies...");
    }
}
