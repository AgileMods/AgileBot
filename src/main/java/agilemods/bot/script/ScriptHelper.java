package agilemods.bot.script;

import agilemods.bot.wrapper.HttpWrapper;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class ScriptHelper {

    public static Bindings getBindings(ScriptEngine scriptEngine) {
        Bindings bindings = scriptEngine.createBindings();
        bindings.put("http", new HttpWrapper());
        return bindings;
    }

    public static ScriptEngine getLuaEngine() {
        ScriptEngineManager sem = new ScriptEngineManager();
        return sem.getEngineByExtension(".lua");
    }
}