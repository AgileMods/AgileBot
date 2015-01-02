package agilemods.bot.lua.lib;

import com.google.common.collect.Maps;
import com.google.common.io.CharStreams;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;

public class HttpLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable table = LuaValue.tableOf();
        table.set("post", new post());
        table.set("get", new get());
        env.set("http", table);
        return NIL;
    }

    public class post extends TwoArgFunction {
        public LuaValue call(LuaValue url, LuaValue data) {
            if (!url.isstring()) {
                argerror(1, "Expected string, got " + url.typename());
            }

            if (data.isstring()) {
                try {
                    HttpLib.post(url.toString(), data.toString());
                } catch (IOException ex) {
                    error(ex.getMessage());
                }
            } else if (data.typename().equals("table")) {
                LuaTable table = (LuaTable) data;
                Map<String, String> postData = Maps.newHashMap();

                for (LuaValue key : table.keys()) {
                    postData.put(key.toString(), table.get(key).toString());
                }

                try {
                    return LuaValue.valueOf(HttpLib.post(url.toString(), postData));
                } catch (IOException ex) {
                    error(ex.getMessage());
                }
            } else {
                argerror(2, "Expected table or string, got " + data.typename());
            }

            return NIL;
        }
    }

    public class get extends OneArgFunction {
        public LuaValue call(LuaValue request) {
            try {
                return LuaValue.valueOf(HttpLib.http_get(request.toString()));
            } catch (IOException ex) {
                error(ex.getMessage());
            }
            return NIL;
        }
    }

    public static String post(String url, String data) throws IOException {
        Map<String, String> postData = Maps.newHashMap();
        String[] array = data.split("&");
        for (String string : array) {
            if (string.contains("=")) {
                String key = string.substring(0, string.indexOf("="));
                String val = string.substring(string.indexOf("=") + 1, string.length());
                postData.put(key, val);
            }
        }

        return post(url, postData);
    }

    public static String post(String url, Map<String, String> data) throws IOException {
        URL u = new URL(url);

        URLConnection httpConnection = u.openConnection();
        httpConnection.setDoInput(true);
        httpConnection.setDoOutput(true);
        httpConnection.setUseCaches(false);
        httpConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        DataOutputStream outputStream = new DataOutputStream(httpConnection.getOutputStream());

        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
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

    public static String http_get(String request) throws IOException {
        URL u = new URL(request);

        URLConnection httpConnection = u.openConnection();
        httpConnection.setDoOutput(true);
        httpConnection.setUseCaches(false);

        InputStream inputStream = httpConnection.getInputStream();
        return CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));
    }
}
