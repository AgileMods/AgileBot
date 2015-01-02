package agilemods.bot.management;

import com.google.common.collect.Maps;
import agilemods.bot.script.ChatScript;
import agilemods.bot.serialize.Serializer;

import java.util.Map;

public class Command {

    public static Map<String, ChatScript> commandMap = Maps.newHashMap();

    public static void reset() {
        commandMap.clear();
    }

    public static void addCommand(String name, ChatScript chatScript) {
        commandMap.put(name, chatScript);
        Serializer.saveCommands();
    }

    public static void removeCommand(String name) {
        commandMap.remove(name);
    }

    public static ChatScript getCommand(String name) {
        return commandMap.get(name);
    }

    public static int reload() {
        int count = 0;
        for (ChatScript chatScript : commandMap.values()) {
            try {
                chatScript.reload();
            } catch (Exception ignore) {}
            count++;
        }
        return count;
    }
}
