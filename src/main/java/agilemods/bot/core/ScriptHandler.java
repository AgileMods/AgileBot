package agilemods.bot.core;

import agilemods.bot.lua.LuaCallbackScript;
import agilemods.bot.lua.LuaHelper;
import agilemods.bot.lua.LuaScript;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.util.Map;

public class ScriptHandler {

    private static final File SCRIPT_DIR = new File("scripts/");

    public static final int CALLBACK_CHANNEL_MESSAGE = 0;
    public static final int CALLBACK_PRIVATE_MESSAGE = 1;
    public static final int CALLBACK_USER_JOIN = 2;
    public static final int CALLBACK_USER_PART = 3;

    private static Map<String, LuaCallbackScript> callbackMap = Maps.newHashMap();

    private static boolean shouldSave = false;

    public static void loadScripts() {
        callbackMap.clear();

        shouldSave = false;
        if (SCRIPT_DIR.exists() && SCRIPT_DIR.isDirectory()) {
            for (File file : SCRIPT_DIR.listFiles(LuaHelper.LUA)) {
                String name = Files.getNameWithoutExtension(file.getName());
                registerScript(new LuaCallbackScript(name, new LuaScript(file)));
            }
        }
        shouldSave = true;
    }

    public static void registerScript(LuaCallbackScript callbackScript) {
        if (shouldSave) {
            if (!SCRIPT_DIR.exists()) {
                SCRIPT_DIR.mkdir();
            }

            IOHelper.saveToFile(
                    new File(SCRIPT_DIR, callbackScript.name + ".lua"),
                    callbackScript.getScript()
            );
        }

        callbackMap.put(callbackScript.name, callbackScript);
        callbackScript.call();
    }

    public static void removeScript(String name) {
        File file = new File(SCRIPT_DIR, name + ".lua");
        if (file.exists()) {
            file.delete();
        }

        callbackMap.remove(name);
    }

    public static String getScripts() {
        Joiner joiner = Joiner.on(",").useForNull("null");
        return joiner.join(callbackMap.keySet());
    }

    public static void fireCallback(int type, LuaValue ... args) {
        for (LuaCallbackScript callbackScript : callbackMap.values()) {
            callbackScript.fireCallback(type, args);
        }
    }
}
