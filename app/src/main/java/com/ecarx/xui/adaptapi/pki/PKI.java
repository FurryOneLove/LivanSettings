package com.ecarx.xui.adaptapi.pki;

import android.content.Context;
import android.util.Log;
import com.ecarx.xui.adaptapi.AdaptAPI;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;

/* loaded from: classes.dex */
public abstract class PKI extends AdaptAPI {
    public static final int SSL_AUTH_BOTH = 2;
    public static final int SSL_AUTH_NONE = 0;
    public static final int SSL_AUTH_SINGLE = 1;

    @Retention(RetentionPolicy.SOURCE)
    @interface SSLAuthType {
    }

    public abstract String getCertificateAlias();

    public abstract int getSSLAuthType();

    public static PKI create(Context context) {
        try {
            Class clazz = Class.forName("com.ecarx.xui.adaptapi.pki.impl.PKIImpl");
            Constructor c = clazz.getConstructor(Context.class);
            PKI pki = (PKI) c.newInstance(context);
            return pki;
        } catch (Exception e) {
            Log.e("PKI", "new PKI  ex:", e);
            return null;
        }
    }
}
