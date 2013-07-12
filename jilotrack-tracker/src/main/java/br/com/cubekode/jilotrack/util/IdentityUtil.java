package br.com.cubekode.jilotrack.util;

import java.net.InetAddress;

public class IdentityUtil {

    private static String hostIdentity;

    static {
        try {
            // long host = 0;
            // byte[] address = InetAddress.getLocalHost().getAddress();
            // for (int i = 0; i < address.length; i++) {
            // host += ((long) address[i] & 0XFFL) << (8 * i);
            // }
            // hostIdentity = number(host);
            hostIdentity = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            // shhhh
            hostIdentity = "X";
        }
    }

    public static String host() {
        return hostIdentity;
    }

    public static String object(Object instance) {
        return number(System.identityHashCode(instance));
    }

    public static String time() {
        return number(System.currentTimeMillis());
    }

    public static String number(int i) {
        // return Integer.toString(i, Character.MAX_RADIX).toUpperCase();
        return Integer.toString(i);
    }

    public static String number(long i) {
        // return Long.toString(i, Character.MAX_RADIX).toUpperCase();
        return Long.toString(i);
    }
}
