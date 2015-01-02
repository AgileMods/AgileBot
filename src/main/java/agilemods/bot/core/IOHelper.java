package agilemods.bot.core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IOHelper {

    public static void saveToFile(File file, String data) {
        ThreadSaveFile threadSaveFile = new ThreadSaveFile(file, data);
        threadSaveFile.start();
    }

    public static class ThreadSaveFile extends Thread {

        public File file;

        public String data;

        public ThreadSaveFile(File file, String data) {
            this.file = file;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
                for (String string : data.split("\n")) {
                    bufferedWriter.write(string);
                    bufferedWriter.newLine();
                }
                bufferedWriter.flush();
                bufferedWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
