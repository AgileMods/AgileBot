package agilemods.bot.lua.lib;

import agilemods.bot.core.CallbackHandler;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class CallbackLib extends TwoArgFunction {

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        env.set("register_callback", new register_callback());
        return NIL;
    }

    public static class register_callback extends LibFunction {
        // If called via callback.register_callback
        public LuaValue call(LuaValue a, LuaValue b) {
            if (!b.typename().equals("function")) {
                argerror(2, "expected function, got " + b.typename());
            }

            CallbackHandler.registerCalback(a.toint(), (LuaFunction) b);

            return NIL;
        }

        // If called via callback:register_callback
        public LuaValue call(LuaValue a, LuaValue b, LuaValue c) {
            call(b, c);
            return NIL;
        }
    }
}
