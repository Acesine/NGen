import bots.SimpleBot;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import db.*;
import db.model.Node;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by xianggao on 5/1/16.
 */
public class Main {
    private final static String SEED = "news.ycombinator.com/news";
    private final static int THREAD_NUM = 5;
    private final static int TIME_OUT_IN_MINUTE = 1;

    public static void main(String [] args) {
        MongoClient dbClient = new MongoClient("127.0.0.1", DBConstants.PORT);
        MongoDatabase db = dbClient.getDatabase(DBConstants.DATABASE_NAME);

        // Create tables if necessary
        MongoIterable<String> collections = db.listCollectionNames();
        boolean linkTableExists = false;
        boolean nodeTableExists = false;
        for (String table : collections) {
            if (StringUtils.equalsIgnoreCase(table, DBConstants.LINK_TABLE_NAME)) {
                linkTableExists = true;
            }
            if (StringUtils.equalsIgnoreCase(table, DBConstants.NODE_TABLE_NAME)) {
                nodeTableExists = true;
            }
        }
        if (!linkTableExists) {
            db.createCollection(DBConstants.LINK_TABLE_NAME);
        }
        if (!nodeTableExists) {
            db.createCollection(DBConstants.NODE_TABLE_NAME);
        }

        MongoDBManager dbManager = new MongoDBManagerImpl(db);
        LinkManager linkManager = new LinkManagerImpl(dbManager);
        NodeManager nodeManager = new NodeManagerImpl(dbManager);

        File outputFile = new File("out/out.txt");
        try {
            if (!outputFile.exists()) outputFile.createNewFile();
            System.setOut(new PrintStream(outputFile));
        } catch (IOException e) {
            System.out.println("Failed to create out.txt");
            return;
        }

        final ConcurrentLinkedQueue<Node> queue = new ConcurrentLinkedQueue<Node>();
        queue.add(Node.createNode(SEED, 0.0));
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(THREAD_NUM);
        List<Future<?>> futures = new ArrayList<Future<?>>();
        Random rd = new Random();
        for (int i=0;i<THREAD_NUM;i++) {
            futures.add(pool.scheduleWithFixedDelay(new SimpleBot(queue, linkManager, nodeManager), rd.nextInt(10)+1, rd.nextInt(3)+1, TimeUnit.SECONDS));
        }

        try {
            for (int i = 0; i < THREAD_NUM; i++) {
                futures.get(i).get(TIME_OUT_IN_MINUTE, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            for (int i = 0; i < THREAD_NUM; i++) {
                futures.get(i).cancel(true);
            }
            e.printStackTrace();
        }
    }
}
