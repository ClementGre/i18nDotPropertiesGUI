package fr.clementgre.i18nTranslationManager;

import java.util.ArrayList;

public class Translation implements Comparable<Translation>{

    private ArrayList<String> comments = new ArrayList<>();
    private String key;
    private String value;

    public Translation(ArrayList<String> comments, String key, String value) {
        this.comments = comments;
        this.key = key;
        this.value = value;
    }

    public Translation() {

    }

    public ArrayList<String> getComments() {
        return comments;
    }

    public void setComments(ArrayList<String> comments) {
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
