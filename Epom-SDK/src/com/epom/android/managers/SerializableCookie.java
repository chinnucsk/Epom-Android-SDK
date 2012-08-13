package com.epom.android.managers;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: grim
 * Date: 3/13/12
 * Time: 12:16 PM
 */
public class SerializableCookie implements Serializable {
    private transient Cookie cookie;

    public SerializableCookie(Cookie cookie) {
        this.cookie = cookie;
    }

    public Cookie getCookie() {
        return cookie;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(cookie.getName());
        oos.writeObject(cookie.getValue());
        oos.writeObject(cookie.getPath());
        oos.writeObject(cookie.getDomain());
        oos.writeObject(cookie.getExpiryDate());
        oos.writeInt(cookie.getVersion());
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        cookie = new BasicClientCookie((String) ois.readObject(), (String) ois.readObject());

        ((BasicClientCookie) cookie).setPath((String) ois.readObject());
        ((BasicClientCookie) cookie).setDomain((String) ois.readObject());
        ((BasicClientCookie) cookie).setExpiryDate((Date) ois.readObject());
        ((BasicClientCookie) cookie).setVersion(ois.readInt());
    }

}
