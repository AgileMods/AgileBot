package agilemods.bot.serialize.thread;

import com.google.common.collect.ImmutableMap;
import agilemods.bot.management.Command;
import agilemods.bot.script.ChatScript;
import agilemods.bot.serialize.Serializer;

public class ThreadSaveCommands extends Thread {

    private ImmutableMap<String, ChatScript> immutableMap;

    public ThreadSaveCommands() {
        immutableMap = ImmutableMap.copyOf(Command.commandMap);
    }

    @Override
    public void run() {
        Serializer.saveCommands(immutableMap);
    }
}
