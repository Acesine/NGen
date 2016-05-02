package bots;

import db.LinkManager;
import db.NodeManager;
import db.model.Link;
import db.model.Node;
import exceptions.UriException;
import org.bson.Document;
import utils.UriUtils;
import utils.handlers.HtmlHandler;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by xianggao on 5/1/16.
 */
public class SimpleBot implements Runnable{
    private final ConcurrentLinkedQueue<Node> queue;
    private final LinkManager linkManager;
    private final NodeManager nodeManager;

    public SimpleBot(final ConcurrentLinkedQueue<Node> queue,
                     final LinkManager linkManager,
                     final NodeManager nodeManager) {
        this.queue = queue;
        this.linkManager = linkManager;
        this.nodeManager = nodeManager;
    }

    @Override
    public void run() {
        final Node seed = queue.poll();
        if (seed != null) {
            System.out.println("Working on: " + seed.getName());
            String html;
            try {
                html = UriUtils.fetchUrl(seed.getName());
            } catch (UriException e) {
                System.out.println("Invalid uri: "+seed.getName());
                return;
            }
            List<String> allChildren = new HtmlHandler(html).fetchAllUris();
            for (String c : allChildren) {
                Node newNode = Node.createNode(c, seed.getWeight() + 1);
                if (!queue.contains(newNode) && !isChecked(c)) {
                    linkManager.putLink(Link.createLink(seed.getName(), c));
                    queue.add(newNode);
                }
            }
        }
    }

    private boolean isChecked(String uri) {
        return nodeManager.getNodeCount(new Document().append(Node.ID, uri)) != 0;
    }
}
