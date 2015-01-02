package agilemods.bot.core;

import agilemods.bot.lua.LuaHelper;
import agilemods.bot.lua.LuaScript;
import com.google.common.collect.Sets;

import java.io.File;
import java.util.Set;

public class ScriptHandler {

    private static final File scriptDir = new File("scripts/");

    private static Set<LuaScript> loadedScripts = Sets.newHashSet();

    public static boolean shouldSave = false;

    public static void loadScripts() {
        loadedScripts.clear();
        CallbackHandler.clear();

        shouldSave = false;

        if (scriptDir.exists() && scriptDir.isDirectory()) {
            for (File file : scriptDir.listFiles(LuaHelper.LUA)) {
                registerScript(new LuaScript(file));
            }
        }

        shouldSave = true;
    }

    public static void registerScript(LuaScript luaScript) {
        loadedScripts.add(luaScript);
        luaScript.call();

        if (shouldSave) {
            saveScript(luaScript);
        }
    }

    public static void saveScript(LuaScript luaScript) {
        if (!scriptDir.exists()) {
            scriptDir.mkdir();
        }

        IOHelper.saveToFile(new File(scriptDir, "generated_" + loadedScripts.size() + ".lua"), luaScript.function);
    }
}
