package agilemods.bot.command;

import agilemods.bot.core.ScriptHandler;
import org.pircbotx.hooks.events.MessageEvent;

public class CommandListScripts extends BaseCommand {

    @Override
    public String getCommand() {
        return "scripts";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        event.getChannel().send().message(ScriptHandler.getScripts());
        return true;
    }
}
