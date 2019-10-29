package com.annimon.hotarufx.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class IOStream {

    public static String readContent(InputStream is) throws IOException {
        final var baos = new ByteArrayOutputStream();
        final var bufferSize = 4096;
        final var buffer = new byte[bufferSize];
        int read;
        while ((read = is.read(buffer, 0, bufferSize)) != -1)  {
            baos.write(buffer, 0, read);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }
}
