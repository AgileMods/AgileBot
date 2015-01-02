package agilemods.bot.listener;

import com.google.common.io.CharStreams;
import agilemods.bot.management.Command;
import agilemods.bot.management.Ownership;
import agilemods.bot.script.ChatScript;
import agilemods.bot.script.TemporaryScript;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import javax.script.ScriptException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;

public class ChatListener extends ListenerAdapter<PircBotX> {

    @Override
    public void onMessage(MessageEvent<PircBotX> event) {
        if (event.getMessage().startsWith("!")) {
            Channel channel = event.getChannel();
            User user = event.getUser();
            String messsage = event.getMessage();

            String[] args = messsage.substring(1, messsage.length()).split(" ");
            String command = args[0];

            if (Ownership.hasOwnership(user) || user.getNick().equals("agilemods")) {
                if (command.equals("add_command")) {
                    addCommand(Arrays.copyOfRange(args, 1, args.length));
                    reply(channel, "Added command: " + args[1]);
                    return;
                } else if (command.equals("load_command")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i=2; i<args.length; i++) {
                        stringBuilder.append(args[i]);
                    }

                    try {
                        URL url = new URL(stringBuilder.toString());
                        URLConnection urlConnection = url.openConnection();
                        InputStream inputStream = urlConnection.getInputStream();
                        String data = CharStreams.toString(new InputStreamReader(inputStream, "UTF-8"));

                        addCommand(args[1], data);
                        reply(channel, "Added command: " + args[1]);
                        return;
                    } catch (Exception ex) {
                        reply(channel, ex.toString());
                    }
                } else if (command.equals("eval")) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i=1; i<args.length; i++) {
                        stringBuilder.append(args[i]);
                        stringBuilder.append(" ");
                    }

                    try {
                        TemporaryScript temporaryScript = new TemporaryScript(channel, stringBuilder.toString().trim());
                        temporaryScript.evaluate();
                    } catch (ScriptException ex) {
                        ex.printStackTrace();
                    }
                } else if (command.equals("reload") && args.length == 1) {
                    reply(channel, "Reloaded " + Command.reload() + " commands");
                } else if (command.equals("give_ownership") && args.length == 2) {
                    User searchUser = getUser(event.getChannel(), args[1]);
                    if (searchUser != null) {
                        Ownership.giveOwnership(searchUser);
                        reply(channel, "Gave ownership to " + searchUser.getNick());
                    } else {
                        reply(channel, "Couldn't find user " + args[1]);
                    }
                } else if (command.equals("remove_ownership") && args.length == 2) {
                    User searchUser = getUser(event.getChannel(), args[1]);
                    if (searchUser != null) {
                        Ownership.removeOwnership(searchUser);
                        reply(channel, "Stripped ownership from " + searchUser.getNick());
                    } else {
                        reply(channel, "Couldn't find user " + args[1]);
                    }
                }
            }

            ChatScript chatScript = Command.getCommand(command);
            if (chatScript != null) {
                try {
                    chatScript.onMessage(event);
                } catch (ScriptException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private User getUser(Channel channel, String nickname) {
        for (User user : channel.getUsers()) {
            if (user.getNick().equals(nickname)) {
                return user;
            }
        }
        return null;
    }

    private void reply(Channel channel, String message) {
        channel.send().message(message);
    }

    private void addCommand(String[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=1; i<args.length; i++) {
            stringBuilder.append(args[i]);
            stringBuilder.append(" ");
        }

        addCommand(args[0], stringBuilder.toString().trim());
    }

    private void addCommand(String name, String function) {
        try {
            ChatScript chatScript = new ChatScript(function);
            chatScript.reload();

            Command.addCommand(name, chatScript);
        } catch (ScriptException ex) {
            ex.printStackTrace();
        }
    }
}
