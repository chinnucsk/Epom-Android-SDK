package com.epom.android.managers;

import org.apache.http.cookie.Cookie;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/13/12
 * Time: 12:38 PM
 */

public class EpomCookieStore implements Serializable {

    private List<SerializableCookie> cookies = new ArrayList<SerializableCookie>();

    public EpomCookieStore() {
    }

    public EpomCookieStore(List<SerializableCookie> cookies) {
        this.cookies = cookies;
    }

    public List<SerializableCookie> getCookies() {
        return cookies;
    }

    public void addCookie(Cookie cookie) {
        cookies.add(new SerializableCookie(cookie));
    }

    public void emptyStore() {
        cookies.clear();
    }
}
