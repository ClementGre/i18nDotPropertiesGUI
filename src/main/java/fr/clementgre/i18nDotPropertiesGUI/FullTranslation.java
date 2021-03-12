package fr.clementgre.i18nDotPropertiesGUI;

public class FullTranslation {

    private String key;
    private String comments;
    private String sourceTranslation;
    private String alternativeTranslation;
    private String targetTranslation;

    public FullTranslation(String key, String comments, String sourceTranslation, String alternativeTranslation, String targetTranslation) {
        this.key = key;
        this.comments = comments;
        this.sourceTranslation = sourceTranslation;
        this.alternativeTranslation = alternativeTranslation;
        this.targetTranslation = targetTranslation;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSourceTranslation() {
        return sourceTranslation;
    }

    public void setSourceTranslation(String sourceTranslation) {
        this.sourceTranslation = sourceTranslation;
    }

    public String getAlternativeTranslation() {
        return alternativeTranslation;
    }

    public void setAlternativeTranslation(String alternativeTranslation) {
        this.alternativeTranslation = alternativeTranslation;
    }

    public String getTargetTranslation() {
        return targetTranslation;
    }

    public void setTargetTranslation(String targetTranslation) {
        this.targetTranslation = targetTranslation;
    }
}
