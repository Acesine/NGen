package db;

import db.model.Node;
import org.bson.Document;

import java.util.List;

/**
 * Created by Acesine on 11/20/15.
 */
public interface NodeManager {
    public long getNodeCount(Document filter);
    public List<Node> getNodes(Document filter);
    public void putNode(Node node);
    public void updateNode(Node oldNode, Node newNode);
    public void deleteNode(Node node);
}
