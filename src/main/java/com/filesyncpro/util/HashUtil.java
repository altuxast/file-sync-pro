package main.java.com.filesyncpro.util;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class HashUtil {
    
    public static String hashFile(Path file, String algorithm) throws Exception{
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        byte[] bytes = Files.readAllBytes(file);
        byte[] hashed = digest.digest(bytes);
        return bytesToHex(hashed);        
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
