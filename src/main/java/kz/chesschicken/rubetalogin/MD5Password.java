package kz.chesschicken.rubetalogin;

import java.math.BigInteger;
import java.security.MessageDigest;

class MD5Password {
    public static String getMD5(String s)
    {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] data = s.getBytes();
            m.update(data, 0, data.length);
            BigInteger i = new BigInteger(1, m.digest());
            return (String.format("%1$032X".toLowerCase(), new Object[]{i}));
        }catch (Exception e) {
            return null;
        }
    }
}
