package com.tutorlink.api.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Encryption {

    @Value("${salt}")
    private String salt;

    public String SHA256(String plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest((plainText + salt).getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
