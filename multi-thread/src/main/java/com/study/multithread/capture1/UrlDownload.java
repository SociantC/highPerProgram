package com.study.multithread.capture1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UrlDownload {
    public static List<String> download() throws IOException{
        List<String> result = new ArrayList<>();
        HttpURLConnection urlConnection = (HttpURLConnection) new URL("http://www.baidu.com").openConnection();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.add(line);
            }
        }
        return result;
    }
}
