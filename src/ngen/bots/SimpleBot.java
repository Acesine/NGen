package ngen.bots;

import ngen.db.LinkManager;
import ngen.db.NodeManager;
import ngen.db.model.Link;
import ngen.db.model.Node;
import ngen.exceptions.UriException;
import ngen.structure.InfiniteConcurrentStringQueue;
import org.bson.Document;
import ngen.utils.UriUtils;
import ngen.utils.handlers.HtmlHandler;

import java.util.List;

/**
 * Created by xianggao on 5/1/16.
 */
public class SimpleBot implements Runnable{
    private final InfiniteConcurrentStringQueue queue;
    private final LinkManager linkManager;
    private final NodeManager nodeManager;

    public SimpleBot(final InfiniteConcurrentStringQueue queue,
                     final LinkManager linkManager,
                     final NodeManager nodeManager) {
        this.queue = queue;
        this.linkManager = linkManager;
        this.nodeManager = nodeManager;
    }

    @Override
    public void run() {
        final String seed = queue.poll();
        if (seed != null) {
            System.out.println("Working on: " + seed);
            String html;
            try {
                html = UriUtils.fetchUrl(seed);
            } catch (UriException e) {
                System.out.println("Invalid uri: "+seed);
                return;
            }
            List<String> allChildren = new HtmlHandler(html).fetchAllUris();
            for (String c : allChildren) {
                if (!queue.contains(c) && !isQueued(c)) {
                    linkManager.putLink(Link.createLink(seed, c));
                    nodeManager.putNode(Node.createNode(c, 0.0));
                    queue.add(c);
                }
            }
        }
    }

    private boolean isQueued(String uri) {
        return nodeManager.getNodeCount(new Document().append(Node.ID, uri)) != 0;
    }
}
