package fr.clementgre.i18nDotPropertiesGUI;

import java.util.ArrayList;
import java.util.List;

public class Translation implements Comparable<Translation>{

    private String comments;
    private String key;
    private String value;

    public Translation(String comments, String key, String value) {
        this.comments = comments;
        this.key = key;
        this.value = value;
    }

    public Translation() {

    }

    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }

    public void addComment(String comment) {
        if(this.comments == null) this.comments = comment;
        else this.comments += "\n" + comment;
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
