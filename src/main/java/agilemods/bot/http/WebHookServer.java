package agilemods.bot.http;

import agilemods.bot.AgileBot;
import agilemods.bot.core.ScriptHandler;
import agilemods.bot.lua.arg.ChannelArg;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import fi.iki.elonen.NanoHTTPD;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.pircbotx.Channel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

public class WebHookServer extends NanoHTTPD {

    public WebHookServer() throws IOException {
        super(AgileBot.HTTP_PORT);
    }

    @Override
    public Response serve(IHTTPSession session) {
        LuaValue parameters = LuaValue.NIL;
        if (session.getMethod().equals(Method.GET)) {
            parameters = getParamaters(session.getQueryParameterString());
        } else if (session.getMethod().equals(Method.POST)) {
            try {
                Map<String, String> body = Maps.newHashMap();
                session.parseBody(body);
                parameters = getParamaters(session.getQueryParameterString());
            } catch (Exception ex) {
                parameters = LuaValue.NIL;
            }
        }

        return (Response) ScriptHandler.fireCallback(
                ScriptHandler.CALLBACK_HTTP_REQUEST,
                LuaValue.valueOf(session.getMethod().toString()),
                LuaValue.valueOf(session.getUri()),
                parameters,
                getChannels()
        );
    }

    private LuaTable getParamaters(String string) {
        LuaTable table = LuaTable.tableOf();
        if (string == null || string.isEmpty()) {
            return table;
        }

        try {
            string = URLDecoder.decode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String[] split = string.split("&");
        for (String str : split) {
            String[] split2 = str.split("=");
            table.set(split2[0], split2[1]);
        }

        return table;
    }

    private LuaTable getChannels() {
        LuaTable table = LuaValue.tableOf();
        ImmutableSet<Channel> channels = AgileBot.bot.getUserBot().getChannels();
        for (Channel channel : channels) {
            table.set(channel.getName(), new ChannelArg(channel).generateTable());
        }
        return table;
    }
}
