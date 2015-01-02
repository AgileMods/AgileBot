package agilemods.bot.command;

import agilemods.bot.lua.LuaScript;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandEval extends BaseCommand {

    @Override
    public String getCommand() {
        return "eval";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String arg : args) {
            stringBuilder.append(arg).append(" ");
        }
        String function = stringBuilder.toString().trim();

        LuaCommand luaCommand = new LuaCommand("", new LuaScript(function));
        luaCommand.processCommand(event, new String[0]);

        return true;
    }
}
