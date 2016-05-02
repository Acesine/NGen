package db;

import db.model.Node;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acesine on 11/20/15.
 */
public class NodeManagerImpl implements NodeManager{
    private final String TABLE_NAME = DBConstants.NODE_TABLE_NAME;
    private final MongoDBManager dbManager;

    public NodeManagerImpl(final MongoDBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public long getNodeCount(Document filter) {
        return dbManager.getCount(TABLE_NAME, filter);
    }

    @Override
    public List<Node> getNodes(Document filter) {
        List<Document> documents = dbManager.get(TABLE_NAME, filter);
        List<Node> result = new ArrayList<>();
        for (Document doc : documents) {
            result.add(Node.fromDocument(doc));
        }
        return result;
    }

    @Override
    public void putNode(Node node) {
        dbManager.insert(TABLE_NAME, node.toDocument());
    }

    @Override
    public void updateNode(Node oldNode, Node newNode) {
        dbManager.update(TABLE_NAME, oldNode.toDocument(), newNode.toDocument());
    }

    @Override
    public void deleteNode(Node node) {
        dbManager.delete(TABLE_NAME, node.toDocument());
    }
}
