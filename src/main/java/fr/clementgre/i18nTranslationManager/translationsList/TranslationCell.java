package fr.clementgre.i18nTranslationManager.translationsList;

import fr.clementgre.i18nTranslationManager.Translation;
import fr.clementgre.i18nTranslationManager.utils.FitTextArea;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class TranslationCell extends ListCell<String>{

    public enum TextType {
        KEY, COMMENT, SOURCE, ALTERNATIVE, TARGET
    }

    private String lastKey = "";

    public VBox pane = new VBox();

    private TranslationSemiInput keyText;
    private TranslationSemiInput commentsText;
    private TranslationSemiInput sourceText;
    private TranslationSemiInput alternativeText;

    private FitTextArea targetText;

    private Region bar = new Region();

    public TranslationsListView listView;
    public TranslationCell(TranslationsListView listView) {
        this.listView = listView;

        setupGraphic();

    }

    public void setupGraphic(){


        keyText = new TranslationSemiInput(this, TextType.KEY);
        commentsText = new TranslationSemiInput(this, TextType.COMMENT);
        sourceText = new TranslationSemiInput(this, TextType.SOURCE);
        alternativeText = new TranslationSemiInput(this, TextType.ALTERNATIVE);
        targetText = new FitTextArea(30);
        targetText.setStyle("-fx-font-size: 14;");

        bar.setPrefHeight(2);
        bar.setStyle("-fx-background-color: #0078D7");

        VBox.setMargin(bar, new Insets(0, 0, 5, 0));

        pane.getChildren().addAll(bar, keyText, commentsText, sourceText, alternativeText, targetText);

        targetText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            publishEditAuto(TranslationCell.TextType.TARGET, newValue.toString());
        });

    }

    @Override
    public void updateItem(String key, boolean empty){

        if(empty){
            setGraphic(null);
            setTooltip(null);
            setContextMenu(null);
            setOnMouseClicked(null);

            keyText.unloadTextArea();
            commentsText.unloadTextArea();
            sourceText.unloadTextArea();
            alternativeText.unloadTextArea();
            lastKey = null;

        }else{

            if(!key.equals(lastKey)){
                keyText.unloadTextArea();
                commentsText.unloadTextArea();
                sourceText.unloadTextArea();
                alternativeText.unloadTextArea();

                keyText.setText(key);
                commentsText.setText(getText(TextType.COMMENT, key));
                sourceText.setText(getText(TextType.SOURCE, key));
                alternativeText.setText(getText(TextType.ALTERNATIVE, key));
                targetText.setText(getText(TextType.TARGET, key));

            }

            setGraphic(pane);

            lastKey = key;
        }


    }


    public String getText(TextType type, String key){

        if(type == TextType.SOURCE || type == TextType.COMMENT){
            Translation sourceTranslation = listView.getWindow().sourceTranslation.getTranslation(key);

            if(type == TextType.SOURCE) return sourceTranslation.getValue() == null ? "" : sourceTranslation.getValue();
            else return String.join("\n", sourceTranslation.getComments());

        }else if(type == TextType.ALTERNATIVE){
            Translation alternativeTranslation = listView.getWindow().alternativeTranslation.getTranslation(key);
            if(alternativeTranslation == null) return "";
            return alternativeTranslation.getValue() == null ? "" : alternativeTranslation.getValue();

        }else if(type == TextType.TARGET){
            Translation targetTranslation = listView.getWindow().targetTranslation.getTranslation(key);
            if(targetTranslation == null) return "";
            return targetTranslation.getValue() == null ? "" : targetTranslation.getValue();
        }

        return key;
    }

    public void publishEditAuto(TextType type, String text){
        publishEdit(type, text, lastKey);
    }
    public void publishEdit(TranslationCell.TextType type, String value, String key){
        if(key == null){
            System.out.println("key == null, unable to edit the translation");
            return;
        }

    }
}
