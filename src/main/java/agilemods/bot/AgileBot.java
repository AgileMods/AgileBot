package agilemods.bot;

import agilemods.bot.core.EventListener;
import agilemods.bot.lua.LuaScript;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

public class AgileBot {

    private static final String IRC_HOST = System.getProperty("agilebot.host", "irc.esper.net");
    private static final String IRC_CHANNEL = System.getProperty("agilebot.channel", "#agilemods");

    public static void main(String[] args) throws IOException, IrcException {
        startBot();
    }

    private static void startBot() throws IOException, IrcException {
        Configuration<PircBotX> configuration = new Configuration.Builder<PircBotX>()
                .setName("AgileBot")
                .setServerHostname(IRC_HOST)
                .addAutoJoinChannel(IRC_CHANNEL)
                .addListener(new EventListener())
                .buildConfiguration();

        PircBotX bot = new PircBotX(configuration);
        bot.startBot();
    }
}
