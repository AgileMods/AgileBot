package agilemods.bot;

import agilemods.bot.core.EventListener;
import agilemods.bot.core.ScriptHandler;
import agilemods.bot.http.WebHookServer;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.exception.IrcException;

import java.io.IOException;

public class AgileBot {

    private static final String IRC_HOST = System.getProperty("agilebot.host", "irc.esper.net");
    private static final String IRC_CHANNEL = System.getProperty("agilebot.channel", "#agilemods");
    public static final int HTTP_PORT = Integer.getInteger("agilebot.http_port", 8080);

    public static PircBotX bot;

    public static void main(String[] args) throws IOException, IrcException {
        startServer();
        startBot();
    }

    private static void startBot() throws IOException, IrcException {
        ScriptHandler.loadScripts();

        Configuration<PircBotX> configuration = new Configuration.Builder<PircBotX>()
                .setName("AgileBot")
                .setServerHostname(IRC_HOST)
                .addAutoJoinChannel(IRC_CHANNEL)
                .addListener(new EventListener())
                .buildConfiguration();

        bot = new PircBotX(configuration);
        bot.startBot();
    }

    private static void startServer() {
        WebHookServer server = null;
        try {
            server = new WebHookServer();
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
