package ngen.structure;

import ngen.exceptions.QueueException;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xianggao on 5/2/16.
 */
public class InfiniteConcurrentStringQueue {
    private final static int MAX_SIZE = 1000;

    private final ConcurrentLinkedQueue<String> queue;
    private final String rescueFileName;

    public InfiniteConcurrentStringQueue() {
        queue = new ConcurrentLinkedQueue<>();
        rescueFileName = "InfiniteConcurrentQueue-"+ UUID.randomUUID();
        try {
            if (!new File(rescueFileName).createNewFile()) {
                throw new QueueException("Failed to create InfiniteConcurrentStringQueue");
            }
        } catch (IOException e) {
            throw new QueueException(e);
        }
    }

    public String poll() {
        if (queue.size() > 0) {
            return queue.poll();
        }
        synchronized (this) {
            // pull from file
            try (
                    FileReader fin = new FileReader(rescueFileName);
                    FileWriter fout = new FileWriter(rescueFileName+".tmp")
            ) {
                BufferedReader reader = new BufferedReader(fin);
                BufferedWriter writer = new BufferedWriter(fout);
                int i = 0;
                String line = reader.readLine();
                while (i < MAX_SIZE && line != null) {
                    if (!StringUtils.isEmpty(line)) {
                        queue.add(line);
                        i ++;
                    }
                    line = reader.readLine();
                }
                while (line != null) {
                    writer.write(line);
                    writer.newLine();
                    line = reader.readLine();
                }
                writer.flush();
                writer.close();
                reader.close();

                // Swap
                Files.move(FileSystems.getDefault().getPath(rescueFileName+".tmp"), FileSystems.getDefault().getPath(rescueFileName), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new QueueException(e);
            }
        }
        return queue.poll();
    }

    public void add(String s) {
        if (queue.size() < MAX_SIZE) {
            queue.add(s);
            return;
        }
        synchronized (this) {
            try {
                FileWriter fout = new FileWriter(rescueFileName, true);
                fout.write(s);
                fout.write("\n");
                fout.flush();
                fout.close();
            } catch (IOException e) {
                throw new QueueException(e);
            }
        }
    }

    public boolean contains(String s) {
        if (queue.contains(s)) return true;
        synchronized (this) {
            try (
                    FileReader fin = new FileReader(rescueFileName);
            ) {
                BufferedReader reader = new BufferedReader(fin);
                String line = reader.readLine();
                while (line != null) {
                    if (StringUtils.equals(s, line)) return true;
                    line = reader.readLine();
                }
            } catch (IOException e) {
                throw new QueueException(e);
            }
            return false;
        }
    }

    public void close() {
        // best effort delete
        try {
            Files.deleteIfExists(FileSystems.getDefault().getPath(rescueFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
