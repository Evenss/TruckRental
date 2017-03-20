package com.hdu.truckrental.tools;

import java.security.MessageDigest;

/**
 * Created by hanjianhao on 17/2/5.
 * 加密工具类
 */

public class Encrypt {

    public static final String KEY_MD = "MD5";

    public static String getEncryption(String inputStr){
        StringBuffer buf = null;
        try{
            MessageDigest md = MessageDigest.getInstance(KEY_MD);
            md.update(inputStr.getBytes());
            buf = new StringBuffer();
            byte[] bits = md.digest();
            for(int i=0;i<bits.length;i++){
                int a = bits[i];
                if(a<0) a+=256;
                if(a<16) buf.append("0");
                buf.append(Integer.toHexString(a));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return buf.toString();
    }
}
