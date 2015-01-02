package agilemods.bot.lua;

import agilemods.bot.lua.lib.CallbackLib;
import agilemods.bot.lua.lib.MessageLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;
import org.pircbotx.hooks.events.MessageEvent;

public class LuaHelper {

    public static Globals getGlobals() {
        Globals globals = JsePlatform.standardGlobals();

        // Global libraries
        globals.load(new CallbackLib());

        // Global variables
        globals.set("CALLBACK_MESSAGE", 0);

        return globals;
    }
}
