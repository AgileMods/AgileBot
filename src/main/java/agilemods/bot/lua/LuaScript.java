package agilemods.bot.lua;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class LuaScript {

    public final Globals _g;

    public String function;

    private LuaValue compiledScript;

    public LuaScript(File file) {
        _g = LuaHelper.getGlobals();
        try {
            function = Files.toString(file, Charsets.UTF_8);
            compiledScript = _g.load(new FileReader(file), "script");
        } catch (IOException ex) {
            ex.printStackTrace();
            function = "";
            compiledScript = null;
        }
    }

    public LuaScript(String script) {
        _g = LuaHelper.getGlobals();
        function = script;
        compiledScript = _g.load(script, "script");
    }

    /**
     * Calls (or initializes) the compiled script. For callback scripts, this
     * is used to first register them as callbacks, or to run other initialization
     * code
     *
     * For chat scripts, this is fired whenever the command is used
     */
    public void call() {
        if (compiledScript != null) {
            compiledScript.call();
        }
    }

    public void setBinding(String key, LuaValue value) {
        _g.set(key, value);
    }

    public void clearBinding(String key) {
        _g.set(key, LuaValue.NIL);
    }

    @Override
    public String toString() {
        return compiledScript == null ? "null" : compiledScript.toString();
    }
}
