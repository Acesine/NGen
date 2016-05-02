package ngen.db;

import ngen.db.model.Link;
import org.bson.Document;

import java.util.List;

/**
 * Created by Acesine on 11/19/15.
 */
public interface LinkManager {
    public long getLinkCount(Document filter);
    public List<Link> getLinks(Document filter);
    public void putLink(Link link);
    public void deleteLink(Link link);
}
