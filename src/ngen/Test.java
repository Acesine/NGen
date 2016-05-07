package ngen;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import ngen.db.DBConstants;
import ngen.db.model.Node;
import org.bson.Document;

/**
 * Created by xianggao on 5/2/16.
 */
public class Test {
    public static void main(String [] args) {
        MongoClient dbClient = new MongoClient("127.0.0.1", DBConstants.PORT);
        MongoDatabase db = dbClient.getDatabase(DBConstants.DATABASE_NAME);

        FindIterable<Document> iterable = db.getCollection(DBConstants.NODE_TABLE_NAME).find().limit(10);
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                System.out.println(document);
                //Node node = Node.fromDocument(document);
                //System.out.println(node.getName() + " : "+node.getWeight());
            }
        });
    }
}
