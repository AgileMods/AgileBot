package agilemods.bot.command;

import org.pircbotx.hooks.events.MessageEvent;

public class CommandListCommands extends BaseCommand {

    @Override
    public String getCommand() {
        return "commands";
    }

    @Override
    public boolean processCommand(MessageEvent<?> event, String[] args) {
        event.getChannel().send().message(CommandHandler.getCommands());
        return true;
    }
}
