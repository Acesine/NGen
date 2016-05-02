package ngen.db.model;

import org.bson.Document;

import java.util.Objects;

/**
 * Created by Acesine on 11/19/15.
 */
public class Link {
    public static final String FROM = "from";
    public static final String TO = "to";

    private final String from;
    private final String to;

    private Link(String from, String to) {
        this.from = from;
        this.to = to;
    }

    public static Link createLink(String from, String to) {
        return new Link(from, to);
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Document toDocument() {
        return new Document()
                .append("_id", Objects.hash(from, to))
                .append(FROM, from)
                .append(TO, to);
    }

    public static Link fromDocument(Document doc) {
        return new Link((String) doc.get(FROM), (String) doc.get(TO));
    }
}
