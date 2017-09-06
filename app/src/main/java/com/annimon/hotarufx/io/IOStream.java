package com.annimon.hotarufx.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.val;

public class IOStream {

    public static String readContent(InputStream is) throws IOException {
        val baos = new ByteArrayOutputStream();
        val bufferSize = 4096;
        val buffer = new byte[bufferSize];
        int read;
        while ((read = is.read(buffer, 0, bufferSize)) != -1)  {
            baos.write(buffer, 0, read);
        }
        return baos.toString("UTF-8");
    }
}
