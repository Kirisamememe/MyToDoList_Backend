package com.example.mytodolist_backend.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureKeyGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] keyBytes = new byte[32];
        //256ビットの鍵を生成
        random.nextBytes(keyBytes);
        String base64Key = Base64.getEncoder().encodeToString(keyBytes);
        System.out.println("Secure key: " + base64Key);
    }
}
