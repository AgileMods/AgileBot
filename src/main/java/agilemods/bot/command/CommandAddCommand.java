package agilemods.bot.command;

import agilemods.bot.core.LogHandler;
import agilemods.bot.lua.LuaScript;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandAddCommand extends BaseCommand {

    @Override
    public String getCommand() {
        return "add_command";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        String name = args[0];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=1; i<args.length; i++ ) {
            stringBuilder.append(args[i]).append(" ");
        }
        String function = stringBuilder.toString().trim();

        CommandHandler.registerCommand(new LuaCommand(name, new LuaScript(function)));

        event.getChannel().send().message("Added command: " + name);
        LogHandler.warn("Added command: " + name);

        return true;
    }
}
