package agilemods.bot.command;

import agilemods.bot.core.LogHandler;
import com.google.common.collect.Maps;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.Arrays;
import java.util.Map;

public class CommandHandler {

    private static Map<String, BaseCommand> commandMap = Maps.newHashMap();

    public static void registerCommand(BaseCommand command) {
        for (String string : command.getAliases()) {
            commandMap.put(string, command);
        }
    }

    public static BaseCommand getCommand(String name) {
        return commandMap.get(name);
    }

    public static boolean removeCommand(String name) {
        BaseCommand command = getCommand(name);
        if (command instanceof LuaCommand) {
            commandMap.remove(name);
            return true;
        } else {
            return false;
        }
    }

    static {
        loadCommands();
    }

    private static void loadCommands() {
        commandMap.clear();

        registerCommand(new CommandAdd());
        registerCommand(new CommandRemove());
        registerCommand(new CommandLoad());
        registerCommand(new CommandEval());
    }

    public static void handleMessage(MessageEvent<?> event) {
        Channel channel = event.getChannel();
        User user = event.getUser();
        String message = event.getMessage();

        String[] args = message.split(" ");
        String command = args[0].substring(1, args[0].length());

        if (command.equals("reload")) {
            loadCommands();
            channel.send().message("Reloaded " + commandMap.size() + " commands");
            return;
        }

        BaseCommand baseCommand = commandMap.get(command);

        if (baseCommand != null) {
            if (baseCommand.canUseCommand(user, channel)) {
                baseCommand.processCommand(event, Arrays.copyOfRange(args, 1, args.length));
            } else {
                event.respond("You do not have the required permissions to use this command");
                LogHandler.warn(user + " attempted to use command " + command + " but lacked permission");
            }
        } else {
            channel.send().message("Command not found: " + command);
            LogHandler.warn("Command not found: " + command);
        }
    }
}
