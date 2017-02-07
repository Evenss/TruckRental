package com.computer.hdu.truckrental.tools;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by hanjianhao on 17/2/5.
 * 加密工具类
 */

public class Encrypt {

    public static final String KEY_MD = "MD5";

    public static String getEncryption(String inputStr){
        BigInteger bigInteger = null;
        try{
            MessageDigest messageDigest = MessageDigest.getInstance(KEY_MD);
            byte[] inputData = inputStr.getBytes();
            messageDigest.update(inputData);
            bigInteger = new BigInteger(messageDigest.digest());
        }catch (Exception e){
            e.printStackTrace();
        }

        return bigInteger.toString();
    }
}
