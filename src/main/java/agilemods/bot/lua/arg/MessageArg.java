package agilemods.bot.lua.arg;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.util.Arrays;

public class MessageArg extends JavaArg {

    private GenericMessageEvent<?> messageEvent;

    public MessageArg(GenericMessageEvent<?> messageEvent) {
        this.messageEvent = messageEvent;
    }

    @Override
    public String getType() {
        return "message";
    }

    @Override
    public void fillTable(LuaTable table) {
        String message = messageEvent.getMessage();
        String[] splitMessage = message.split(" ");
        String[] splitArgs = Arrays.copyOfRange(splitMessage, 1, splitMessage.length);
        StringBuilder args = new StringBuilder();
        for (String arg : splitArgs) {
            args.append(arg).append(" ");
        }

        table.set("message", LuaValue.valueOf(message));
        table.set("split_message", CoerceJavaToLua.coerce(splitMessage));
        table.set("args", LuaValue.valueOf(args.toString().trim()));
        table.set("split_args", CoerceJavaToLua.coerce(splitArgs));
    }


}
