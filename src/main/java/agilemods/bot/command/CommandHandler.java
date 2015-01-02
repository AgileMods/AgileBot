package agilemods.bot.command;

import agilemods.bot.core.LogHandler;
import agilemods.bot.core.IOHelper;
import agilemods.bot.lua.LuaHelper;
import agilemods.bot.lua.LuaScript;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;

import java.io.File;
import java.util.Arrays;
import java.util.Map;

public class CommandHandler {

    private static Map<String, BaseCommand> commandMap = Maps.newHashMap();

    private static File commandDir = new File("commands/");

    /**
     * Global flag that if set, will save any LuaCommands registered to a file
     */
    private static boolean shouldSaveLua = false;

    public static void registerCommand(BaseCommand command) {
        if (shouldSaveLua && command instanceof LuaCommand) {
            IOHelper.saveToFile(
                    new File(commandDir, command.getCommand() + ".lua"),
                    ((LuaCommand) command).getScriptContents().getBytes()
            );
        }

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
            File file = new File(commandDir, command.getCommand() + ".lua");
            if (file.exists()) {
                file.delete();
            }

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

        shouldSaveLua = false;

        loadLuaCommands();

        shouldSaveLua = true;
    }

    private static void loadLuaCommands() {
        if (!commandDir.exists() || !commandDir.isDirectory()) {
            return;
        }

        for (File file : commandDir.listFiles(LuaHelper.LUA)) {
            CommandHandler.registerCommand(new LuaCommand(Files.getNameWithoutExtension(file.getName()), new LuaScript(file)));
        }
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
