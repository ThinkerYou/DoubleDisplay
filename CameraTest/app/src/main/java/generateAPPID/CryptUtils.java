package generateAPPID;


import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class CryptUtils {

    /**
     * "nemo1234".getBytes("UTF-8")
     */
    private static final byte[] BYTES = {110, 101, 109, 111, 49, 50, 51, 52};


    public static byte[] initCryptKey() {

        int keyLength = 256;
        byte[] keyBytes = new byte[keyLength / 8];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);

        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = BYTES;
        int length = passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length;
        System.arraycopy(passwordBytes, 0, keyBytes, 0, length);

        return keyBytes;
    }

    public static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }


    public static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return decrypted;
    }

}
