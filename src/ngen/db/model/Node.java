package ngen.db.model;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.Objects;

/**
 * Created by Acesine on 11/20/15.
 */
public class Node {
    private final static String SPLITTER = "|";

    public final static String ID = "_id";
    public final static String WEIGHT = "weight";

    private final String name;
    private final Double weight;

    private Node(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public static Node createNode(String name, Double weight) {
        return new Node(name, weight);
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public Document toDocument() {
        return new Document()
                .append(ID, name)
                .append(WEIGHT, weight);
    }

    public static Node fromString(String str) {
        String [] s = str.split(SPLITTER);
        return new Node(s[0], new Double(s[1]));
    }

    @Override
    public String toString() {
        return name+SPLITTER+weight;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Node)) {
            return false;
        }

        return StringUtils.equals(name, ((Node)obj).getName());
    }

    public static Node fromDocument(Document doc) {
        return new Node((String) doc.get(ID), (Double) doc.get(WEIGHT));
    }
}
