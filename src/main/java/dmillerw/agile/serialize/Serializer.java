package dmillerw.agile.serialize;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import dmillerw.agile.management.Command;
import dmillerw.agile.management.Ownership;
import dmillerw.agile.script.ChatScript;
import dmillerw.agile.serialize.thread.ThreadSaveCommands;
import dmillerw.agile.serialize.thread.ThreadSaveOwnership;

import javax.script.ScriptException;
import java.io.*;
import java.util.List;
import java.util.Map;

public class Serializer {

    public static final FilenameFilter FILENAME_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".lua");
        }
    };

    public static File commandDir = new File("commands/");
    public static File ownershipDir = new File("ownership/");

    public static void loadCommands() {
        if (!commandDir.exists() || !commandDir.isDirectory()) {
            return;
        }

        Command.reset();

        try {
            for (File file : commandDir.listFiles(FILENAME_FILTER)) {
                String name = file.getName().substring(0, file.getName().lastIndexOf("."));
                String function = Files.toString(file, Charsets.UTF_8);

                try {
                    ChatScript chatScript = new ChatScript(function);
                    chatScript.reload();

                    Command.addCommand(name, chatScript);
                } catch (ScriptException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void saveCommands() {
        ThreadSaveCommands thread = new ThreadSaveCommands();
        thread.start();
    }

    public static void saveCommands(Map<String, ChatScript> commandMap) {
        if (!commandDir.exists()) {
            commandDir.mkdir();
        }

        for (Map.Entry<String, ChatScript> entry : commandMap.entrySet()) {
            try {
                File file = new File(commandDir, entry.getKey() + ".lua");

                if (file.exists()) {
                    file.delete();
                }

                Files.write(entry.getValue().function.getBytes(), file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void loadOwnership() {
        if (!ownershipDir.exists()) {
            ownershipDir.mkdir();
        }

        File nicks = new File(ownershipDir, "nicks.list");
        File masks = new File(ownershipDir, "masks.list");

        if (nicks.exists()) {
            Ownership.allowedUsers.clear();

            List<String> list = Lists.newArrayList();
            try {
                list = Files.readLines(nicks, Charsets.UTF_8);
            } catch (IOException ignore) {}

            for (String string : list) {
                Ownership.allowedUsers.add(string);
            }
        }

        if (masks.exists()) {
            Ownership.allowedMasks.clear();

            List<String> list = Lists.newArrayList();
            try {
                list = Files.readLines(masks, Charsets.UTF_8);
            } catch (IOException ignore) {}

            for (String string : list) {
                Ownership.allowedMasks.add(string);
            }
        }
    }

    public static void saveOwnership() {
        ThreadSaveOwnership thread = new ThreadSaveOwnership();
        thread.start();
    }

    public static void saveOwnership(List<String> nickList, List<String> maskList) {
        if (!ownershipDir.exists()) {
            ownershipDir.mkdir();
        }

        File nicks = new File(ownershipDir, "nicks.list");
        File masks = new File(ownershipDir, "masks.list");

        if (nicks.exists()) {
            nicks.delete();
        }

        if (masks.exists()) {
            masks.delete();
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(nicks));
            for (String str : nickList) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(masks));
            for (String str : maskList) {
                bufferedWriter.write(str);
                bufferedWriter.newLine();
            }
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
