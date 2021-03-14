package fr.clementgre.i18nDotPropertiesGUI;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FullTranslation implements Comparable<FullTranslation>{

    private final StringProperty key = new SimpleStringProperty("");
    private final StringProperty comments = new SimpleStringProperty("");
    private final StringProperty sourceTranslation = new SimpleStringProperty("");
    private final StringProperty alternativeTranslation = new SimpleStringProperty("");
    private final StringProperty targetTranslation = new SimpleStringProperty("");

    public FullTranslation(String key, String comments, String sourceTranslation, String alternativeTranslation, String targetTranslation) {
        setKey(key);
        setComments(comments);
        setSourceTranslation(sourceTranslation);
        setAlternativeTranslation(alternativeTranslation);
        setTargetTranslation(targetTranslation);
    }

    public String getKey() {
        return key.get();
    }
    public StringProperty keyProperty() {
        return key;
    }
    public void setKey(String key) {
        this.key.set(key == null ? "" : key);
    }
    public String getComments() {
        return comments.get();
    }
    public StringProperty commentsProperty() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments.set(comments == null ? "" : comments);
    }
    public String getSourceTranslation() {
        return sourceTranslation.get();
    }
    public StringProperty sourceTranslationProperty() {
        return sourceTranslation;
    }
    public void setSourceTranslation(String sourceTranslation) {
        this.sourceTranslation.set(sourceTranslation == null ? "" : sourceTranslation);
    }
    public String getAlternativeTranslation() {
        return alternativeTranslation.get();
    }
    public StringProperty alternativeTranslationProperty() {
        return alternativeTranslation;
    }
    public void setAlternativeTranslation(String alternativeTranslation) {
        this.alternativeTranslation.set(alternativeTranslation == null ? "" : alternativeTranslation);
    }
    public String getTargetTranslation() {
        return targetTranslation.get();
    }
    public StringProperty targetTranslationProperty() {
        return targetTranslation;
    }
    public void setTargetTranslation(String targetTranslation) {
        this.targetTranslation.set(targetTranslation == null ? "" : targetTranslation);
    }

    @Override
    public int compareTo(FullTranslation translation) {
        return getKey().compareTo(translation.getKey());
    }
}
