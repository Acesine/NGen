package ngen.db;

import ngen.db.model.Link;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acesine on 11/19/15.
 */
public class LinkManagerImpl implements LinkManager{
    private final String TABLE_NAME = DBConstants.LINK_TABLE_NAME;
    private final MongoDBManager dbManager;

    public LinkManagerImpl(final MongoDBManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public long getLinkCount(Document filter) {
        return dbManager.getCount(TABLE_NAME, filter);
    }

    public List<Link> getLinks(final Document filter) {
        List<Document> documents = dbManager.get(TABLE_NAME, filter);
        List<Link> result = new ArrayList<>();
        for (Document doc : documents) {
            result.add(Link.fromDocument(doc));
        }
        return result;
    }

    public void putLink(final Link link) {
        dbManager.insert(TABLE_NAME, link.toDocument());
    }

    public void deleteLink(final Link link) {
        dbManager.delete(TABLE_NAME, link.toDocument());
    }
}
