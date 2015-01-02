package agilemods.bot.core;

import agilemods.bot.command.CommandHandler;
import agilemods.bot.lua.arg.ChannelArg;
import agilemods.bot.lua.arg.MessageArg;
import agilemods.bot.lua.arg.UserArg;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.*;

import static agilemods.bot.core.ScriptHandler.*;

public class EventListener extends ListenerAdapter<PircBotX> {

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        if (event.getMessage().startsWith("!")) {
            CommandHandler.handleMessage(event);
        }
        ScriptHandler.fireCallback(
                CALLBACK_CHANNEL_MESSAGE,
                new ChannelArg(event.getChannel()).generateTable(),
                new UserArg(event.getUser()).generateTable(),
                new MessageArg(event).generateTable()
        );
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
        ScriptHandler.fireCallback(
                CALLBACK_PRIVATE_MESSAGE,
                new UserArg(event.getUser()).generateTable(),
                new MessageArg(event).generateTable()
        );
    }

    @Override
    public void onJoin(JoinEvent<PircBotX> event) throws Exception {
        ScriptHandler.fireCallback(
                CALLBACK_USER_JOIN,
                new ChannelArg(event.getChannel()).generateTable(),
                new UserArg(event.getUser()).generateTable()
        );
    }

    @Override
    public void onPart(PartEvent<PircBotX> event) throws Exception {
        ScriptHandler.fireCallback(
                CALLBACK_USER_PART,
                new ChannelArg(event.getChannel()).generateTable(),
                new UserArg(event.getUser()).generateTable()
        );
    }
}
