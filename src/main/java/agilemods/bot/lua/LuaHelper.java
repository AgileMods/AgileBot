package agilemods.bot.lua;

import agilemods.bot.lua.lib.CallbackLib;
import agilemods.bot.lua.lib.HttpLib;
import org.luaj.vm2.Globals;
import org.luaj.vm2.lib.jse.JsePlatform;

public class LuaHelper {

    public static Globals getGlobals() {
        Globals globals = JsePlatform.standardGlobals();

        // Global libraries
        globals.load(new CallbackLib());
        globals.load(new HttpLib());

        // Global variables
        globals.set("CALLBACK_CHANNEL_MESSAGE", 0);
        globals.set("CALLBACK_PRIVATE_MESSAGE", 1);
        globals.set("CALLBACK_USER_JOIN", 2);
        globals.set("CALLBACK_USER_PART", 3);

        return globals;
    }
}
