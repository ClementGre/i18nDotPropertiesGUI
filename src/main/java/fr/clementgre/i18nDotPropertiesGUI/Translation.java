package fr.clementgre.i18nDotPropertiesGUI;

import java.util.ArrayList;
import java.util.List;

public class Translation implements Comparable<Translation>{

    private List<String> comments = new ArrayList<>();
    private String key;
    private String value;

    public Translation(List<String> comments, String key, String value) {
        this.comments = comments;
        this.key = key;
        this.value = value;
    }

    public Translation() {

    }

    public List<String> getComments() {
        if(comments == null) return new ArrayList<>();
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public void addComment(String comment) {
        this.comments.add(comment);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(Translation translation) {
        return key.compareTo(translation.getKey());
    }
}
