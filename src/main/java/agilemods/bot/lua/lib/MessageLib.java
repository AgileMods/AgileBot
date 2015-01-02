package agilemods.bot.lua.lib;

import agilemods.bot.lua.arg.ChannelArg;
import agilemods.bot.lua.arg.MessageArg;
import agilemods.bot.lua.arg.UserArg;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;
import org.pircbotx.hooks.events.MessageEvent;

public class MessageLib extends TwoArgFunction {

    private MessageEvent<?> messageEvent;

    public MessageLib(MessageEvent<?> messageEvent) {
        this.messageEvent = messageEvent;
    }

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        new MessageArg(messageEvent).fillTable((LuaTable) env);
        env.set("user", new UserArg(messageEvent.getUser()).generateTable());
        env.set("channel", new ChannelArg(messageEvent.getChannel()).generateTable());
        return LuaValue.NIL;
    }
}
