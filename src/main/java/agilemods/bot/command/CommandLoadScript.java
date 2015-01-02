package agilemods.bot.command;

import agilemods.bot.core.LogHandler;
import agilemods.bot.core.ScriptHandler;
import agilemods.bot.lua.LuaScript;
import agilemods.bot.lua.lib.HttpLib;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.IOException;

public class CommandLoadScript extends BaseCommand {

    @Override
    public String getCommand() {
        return "load_script";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            stringBuilder.append(args[i]).append(" ");
        }
        String function = "";

        try {
            function = HttpLib.http_get(stringBuilder.toString().trim());
        } catch (IOException ignore) {
        }

        if (!function.isEmpty()) {
            ScriptHandler.registerScript(new LuaScript(function));

            event.getChannel().send().message("Loaded script");
            LogHandler.warn("Loaded script");

            return true;
        } else {
            return false;
        }
    }
}
