package dmillerw.agile.serialize.thread;

import com.google.common.collect.ImmutableMap;
import dmillerw.agile.management.Command;
import dmillerw.agile.script.ChatScript;
import dmillerw.agile.serialize.Serializer;

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
