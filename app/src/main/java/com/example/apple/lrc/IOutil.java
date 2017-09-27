package com.example.apple.lrc;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Created by apple on 17/4/28.
 */

public class IOutil {

    public static void close(InputStream io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void close(Reader io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
