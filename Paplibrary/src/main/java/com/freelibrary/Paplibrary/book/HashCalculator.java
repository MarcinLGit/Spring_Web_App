package com.freelibrary.Paplibrary.book;

import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

public class HashCalculator {

    public static String calculateFileHash(MultipartFile file) {
        try {
            return DigestUtils.sha256Hex(file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
