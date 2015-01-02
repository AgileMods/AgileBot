package agilemods.bot.command;

import agilemods.bot.core.LogHandler;
import agilemods.bot.core.ScriptHandler;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandRemoveScript extends BaseCommand {

    @Override
    public String getCommand() {
        return "remove_script";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        String name = args[0];
        ScriptHandler.removeScript(name);
        event.getChannel().send().message("Removed command: " + name);
        LogHandler.warn("Removed command: " + name);
        return true;
    }
}
