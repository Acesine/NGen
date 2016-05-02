package db.model;

import org.apache.commons.lang3.StringUtils;
import org.bson.Document;

import java.util.Objects;

/**
 * Created by Acesine on 11/20/15.
 */
public class Node {
    public static String ID = "_id";
    public static String WEIGHT = "weight";

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
