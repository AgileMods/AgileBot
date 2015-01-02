package agilemods.bot.command;

import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

public abstract class BaseCommand {

    public abstract String getCommand();

    public String[] getAliases() {
        return new String[] {getCommand()};
    }

    public boolean canUseCommand(User user, Channel channel) {
        return user.getChannelsOpIn().contains(channel);
    }

    /**
     * Basic bridge method for commands to process data
     *
     * @param event The core PircBotX message event
     * @param args A basic array formed from the data, and split on " "
     * @return Whether the command successfully processed or not
     */
    public abstract boolean processCommand(MessageEvent<?> event, String[] args);
}
