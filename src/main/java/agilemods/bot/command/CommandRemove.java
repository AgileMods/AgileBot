package agilemods.bot.command;

import agilemods.bot.core.LogHandler;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandRemove extends BaseCommand {

    @Override
    public String getCommand() {
        return "remove_command";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        String name = args[0];
        if (CommandHandler.removeCommand(name)) {
            event.getChannel().send().message("Removed command: " + name);
            LogHandler.warn("Removed command: " + name);
            return true;
        } else {
            event.getChannel().send().message("Failed to remove command " + name + ": Not a Lua command");
            LogHandler.warn("Failed to remove command " + name + ": Not a Lua command");
            return false;
        }
    }
}
