package agilemods.bot.command;

import agilemods.bot.lua.LuaScript;
import agilemods.bot.lua.lib.MessageLib;
import org.pircbotx.hooks.events.MessageEvent;

public class LuaCommand extends BaseCommand {

    private String name;
    private LuaScript luaScript;

    public LuaCommand(String name, LuaScript luaScript) {
        this.name = name;
        this.luaScript = luaScript;
    }

    @Override
    public String getCommand() {
        return name;
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        luaScript._g.load(new MessageLib(event));
        luaScript.call();
        return true;
    }
}
