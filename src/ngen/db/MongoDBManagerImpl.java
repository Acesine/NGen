package ngen.db;

import com.mongodb.Block;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acesine on 11/14/15.
 */
public class MongoDBManagerImpl implements MongoDBManager {
    private MongoDatabase db;

    public MongoDBManagerImpl(final MongoDatabase db) {
        this.db = db;
    }

    @Override
    public long getCount(String tableName, Document filter) {
        return db.getCollection(tableName).count(filter);
    }

    @Override
    public List<Document> get(String tableName, Document filter) {
        List<Document> result = new ArrayList<>();
        FindIterable<Document> iterable = db.getCollection(tableName).find(filter);
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                result.add(document);
            }
        });
        return result;
    }

    @Override
    public void insert(final String tableName,
                       final Document document) {
        try {
            db.getCollection(tableName).insertOne(document);
        } catch (MongoWriteException e) {
            if (e.getCode() != 11000) {
                throw e;
            }
        }
    }

    @Override
    public void delete(final String tableName,
                       final Document document) {
        db.getCollection(tableName).deleteOne(document);
    }

    @Override
    public void update(final String tableName,
                       final Document oldDocument,
                       final Document newDocument) {
        db.getCollection(tableName).updateOne(oldDocument, newDocument);
    }
}
