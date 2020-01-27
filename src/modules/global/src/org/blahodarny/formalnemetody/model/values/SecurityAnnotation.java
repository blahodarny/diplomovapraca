package org.blahodarny.formalnemetody.model.values;

import java.util.*;

public class SecurityAnnotation {

    private String owner;
    private Set<String> readers;

    public SecurityAnnotation(String owner, Set<String> readers) {
        this.owner = owner;
        this.readers = readers;
    }

    public SecurityAnnotation(String owner, String... readers) {
        this.owner = owner;
        this.readers = new HashSet<>();
        this.readers.addAll(Arrays.asList(readers));
    }

    public SecurityAnnotation copy() {
        String newOwner = "" + owner;
        Set<String> newReaders = new HashSet<>();
        for (String reader : readers) {
            newReaders.add("" + reader);
        }
        return new SecurityAnnotation(newOwner, newReaders);
    }

    public boolean isLessRestrictive(SecurityAnnotation a) {
        if (a == null && readers.isEmpty()) {
            return false;
        }
        if (a == null && !readers.isEmpty()) {
            return true;
        }
        return !a.readers.containsAll(readers);
    }

    public String toString() {
        String result = "";
        result += "Annotation{ ";
        result += "owner: " + ((this.owner.equals("")) ? " no" : owner) + ", readers: ";
        if (this.readers.isEmpty()) {
            result += " no";
        } else {
            for (String reader : this.readers) {
                result += " " + reader;
            }
        }
        result += "}";

        return result;
    }
}
