package com.company;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Downloader {

    public static String downloadData(String urlS) {
        try {
            URL url = new URL(urlS);
            InputStream is = url.openStream();
            InputStreamReader rd = new InputStreamReader(is);

            String text = null;
            try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
                text = scanner.useDelimiter("\\A").next();
            }

            return text;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
