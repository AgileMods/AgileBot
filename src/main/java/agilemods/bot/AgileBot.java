package agilemods.bot;

import agilemods.bot.listener.ChatListener;
import agilemods.bot.serialize.Serializer;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

public class AgileBot {

    public static void main(String[] args) throws IOException, IrcException {
        Serializer.loadCommands();
        Serializer.loadCommands();

        Configuration<PircBotX> configuration = new Configuration.Builder<PircBotX>()
                .setName("AgileBot")
                .setServerHostname("irc.esper.net")
                .addAutoJoinChannel("#agilemods")
                .addListener(new ChatListener())
                .buildConfiguration();

        PircBotX bot = new PircBotX(configuration);
        bot.startBot();
    }
}
