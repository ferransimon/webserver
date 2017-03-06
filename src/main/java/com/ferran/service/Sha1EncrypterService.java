package com.ferran.service;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Sha1EncrypterService implements Encrypter {

    @Override
    public String encrypt(String plain) {
        return Hashing.sha1().hashString(plain, StandardCharsets.UTF_8).toString();
    }
}
