package dmillerw.agile.wrapper;

import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Map;

public class HttpWrapper {

    public String post(String url, String data) throws IOException {
        URL u = new URL(url);

        Map<String, String> postData = Maps.newHashMap();
        String[] array = data.split("&");
        for (String string : array) {
            if (string.contains("=")) {
                String key = string.substring(0, string.indexOf("="));
                String val = string.substring(string.indexOf("=") + 1, string.length());
                postData.put(key, val);
            }
        }

        URLConnection httpConnection = u.openConnection();
        httpConnection.setDoInput(true);
        httpConnection.setDoOutput(true);
        httpConnection.setUseCaches(false);
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        DataOutputStream outputStream = new DataOutputStream(httpConnection.getOutputStream());

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : postData.entrySet()) {
            stringBuilder.append(entry.getKey());
            stringBuilder.append("=");
            stringBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            stringBuilder.append("&");
        }

        outputStream.writeBytes(stringBuilder.toString());
        outputStream.flush();
        outputStream.close();

        InputStream inputStream = httpConnection.getInputStream();
        return CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
    }
}
