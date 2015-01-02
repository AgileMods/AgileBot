package agilemods.bot.core;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

public class IOHelper {

    public static void saveToFile(File file, byte[] data) {
        ThreadSaveFile threadSaveFile = new ThreadSaveFile(file, data);
        threadSaveFile.start();
    }

    public static class ThreadSaveFile extends Thread {

        public File file;

        public byte[] data;

        public ThreadSaveFile(File file, byte[] data) {
            this.file = file;
            this.data = data;
        }

        @Override
        public void run() {
            try {
                Files.write(data, file);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
