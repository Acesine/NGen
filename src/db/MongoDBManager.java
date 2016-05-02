package db;

import org.bson.Document;

import java.util.List;

/**
 * Created by Acesine on 11/14/15.
 */
public interface MongoDBManager {
    public long getCount(String tableName, Document filter);

    public List<Document> get(String tableName, Document filter);

    public void insert(String tableName, Document document);

    public void delete(String tableName, Document document);

    public void update(String tableName, Document oldDocument, Document newDocument);
}
