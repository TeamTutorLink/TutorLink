package com.tutorlink.api.common.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

@SpringBootTest
class EncryptionTest {

    @Autowired
    private Encryption encryption;

    @Test
    void SHA256() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        System.out.println(encryption.SHA256("asd"));
    }
}