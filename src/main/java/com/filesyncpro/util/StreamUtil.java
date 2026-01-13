package main.java.com.filesyncpro.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    public static byte[] toByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] chunk = new byte[8192];
        int n;

        while ((n = is.read(chunk)) != -1) {
            buffer.write(chunk, 0, n);
        }

        return buffer.toByteArray();
    }
}
