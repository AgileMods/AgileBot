package agilemods.bot.core;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;

import java.util.Map;
import java.util.Set;

public class CallbackHandler {

    public static final int CALLBACK_CHANNEL_MESSAGE = 0;
    public static final int CALLBACK_PRIVATE_MESSAGE = 1;
    public static final int CALLBACK_USER_JOIN = 2;
    public static final int CALLBACK_USER_PART = 3;

    private static Map<Integer, Set<LuaFunction>> callbackMap = Maps.newHashMap();

    private static Set<LuaFunction> getSet(int type) {
        Set<LuaFunction> set = callbackMap.get(type);
        if (set == null) {
            set = Sets.newHashSet();
            callbackMap.put(type, set);
        }
        return set;
    }

    public static void registerCalback(int type, LuaFunction function) {
        getSet(type).add(function);
    }

    public static void fireCallback(int type, LuaValue ... args) {
        Set<LuaFunction> set = getSet(type);
        for (LuaFunction luaFunction : set) {
            luaFunction.invoke(args);
        }
    }
}
