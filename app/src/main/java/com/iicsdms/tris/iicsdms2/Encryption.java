package com.iicsdms.tris.iicsdms2;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Tris on 04/03/2018.
 */

public class Encryption {

    private byte [] key =
            {
                    'i','i','c','s','d','m','s','0',
                    't','h','e','s','i','s','0','3'
            };

    public String encrypt(String strToEncrypt)
    {
        String encryptedString = null;
        try
        {
            // ServletContext (web.xml)
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            //encryptedString = Base64.encodeBase64String(cipher.doFinal(strToEncrypt.getBytes()));
            byte [] data = cipher.doFinal(strToEncrypt.getBytes("UTF-8"));
            encryptedString = Base64.encodeToString(data, Base64.DEFAULT);

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return encryptedString;
    }
}
