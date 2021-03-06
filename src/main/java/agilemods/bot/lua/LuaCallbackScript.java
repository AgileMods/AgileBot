package agilemods.bot.lua;

import agilemods.bot.core.ScriptHandler;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fi.iki.elonen.NanoHTTPD;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.Map;
import java.util.Set;

public class LuaCallbackScript {

    private Map<Integer, Set<LuaFunction>> callbackMap = Maps.newConcurrentMap();

    public String name;
    private LuaScript luaScript;

    public LuaCallbackScript(String name, LuaScript luaScript) {
        this.name = name;
        this.luaScript = luaScript;
        this.luaScript.setBinding("register_callback", new register_callback());
    }

    private Set<LuaFunction> getSet(int type) {
        Set<LuaFunction> set = callbackMap.get(type);
        if (set == null) {
            set = Sets.newHashSet();
            callbackMap.put(type, set);
        }
        return set;
    }

    public void registerCallback(int type, LuaFunction callback) {
        getSet(type).add(callback);
    }

    public synchronized Object fireCallback(int type, LuaValue... args) {
        Set<LuaFunction> set = getSet(type);
        for (LuaFunction luaFunction : set) {
            if (type == ScriptHandler.CALLBACK_HTTP_REQUEST) {
                return new NanoHTTPD.Response(luaFunction.invoke(args).toString());
            } else if (luaFunction.invoke(args).checkboolean(0)) {
                return true;
            }
        }
        return true;
    }

    public void call() {
        luaScript.call();
    }

    public String getScript() {
        return luaScript.function;
    }

    private class register_callback extends TwoArgFunction {
        public LuaValue call(LuaValue type, LuaValue callback) {
            if (!callback.typename().equals("function")) {
                argerror(2, "expected function, got " + callback.typename());
            }

            registerCallback(type.toint(), (LuaFunction) callback);

            return NIL;
        }
    }
}
