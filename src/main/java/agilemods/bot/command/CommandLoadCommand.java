package agilemods.bot.command;

import agilemods.bot.core.LogHandler;
import agilemods.bot.lua.LuaScript;
import agilemods.bot.lua.lib.HttpLib;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class CommandLoadCommand extends BaseCommand {

    @Override
    public String getCommand() {
        return "load_command";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        String name = args[0];
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=1; i<args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        String function = "";

        try {
            function = HttpLib.http_get(stringBuilder.toString().trim());
        } catch (IOException ignore) {}

        if (!function.isEmpty()) {
            CommandHandler.registerCommand(new LuaCommand(name, new LuaScript(function)));

            event.getChannel().send().message("Added command: " + name);
            LogHandler.warn("Added command: " + name);

            return true;
        } else {
            return false;
        }
    }
}
