package fr.clementgre.i18nTranslationManager.translationsList;

import fr.clementgre.i18nTranslationManager.Translation;
import fr.clementgre.i18nTranslationManager.utils.FitTextArea;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.util.Arrays;
import java.util.regex.Pattern;


public class TranslationCell extends ListCell<String>{

    public enum TextType {
        KEY, COMMENT, SOURCE, ALTERNATIVE, TARGET
    }

    private String style = "";

    private String lastKey = "";

    public VBox pane = new VBox();

    private TranslationSemiInput keyText;
    private TranslationSemiInput commentsText;
    private TranslationSemiInput sourceText;
    private TranslationSemiInput alternativeText;

    public FitTextArea targetText;

    private Region bar = new Region();

    private boolean pressed = false;

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
        bar.setStyle("-fx-background-color: #9f0000");

        if(listView.isCompactMode()){
            VBox.setMargin(bar, new Insets(0, 0, 2, 0));
        }else{
            VBox.setMargin(bar, new Insets(0, 0, 3, 0));
        }

        pane.setOnMouseClicked((e) -> {
            listView.getSelectionModel().select(lastKey);
        });

        targetText.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue){
                targetText.setText(targetText.getText().trim());
                publishEditAuto(TranslationCell.TextType.TARGET, targetText.getText());
            }else{
                listView.getSelectionModel().select(lastKey);
            }
        });

        setStyle("-fx-padding: 0 7 7 7;");
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && newValue.equals(lastKey)){
                targetText.requestFocus();
                setStyle("-fx-padding: 0 7 7 7; -fx-background-color: #f3f3f3;");
            }else{
                setStyle("-fx-padding: 0 7 7 7;");
            }
        });

        targetText.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
            if((e.isShiftDown() && (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.UP)) || e.getCode() == KeyCode.TAB){
                if(pressed){
                    if(e.getCode() == KeyCode.UP) selectPrevious(lastKey);
                    else selectNext(lastKey);
                    pressed = false;
                }
                e.consume();
            }
        });

        targetText.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
            if((e.isShiftDown() && (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.DOWN || e.getCode() == KeyCode.UP)) || e.getCode() == KeyCode.TAB){
                e.consume();
                pressed = true;
            }
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
            if(!listView.isCompactMode()){
                if(listView.getWindow().alternativeTranslation.hasTranslations()){
                    pane.getChildren().setAll(bar, keyText, sourceText, alternativeText, commentsText, targetText);
                }else{
                    pane.getChildren().setAll(bar, keyText, sourceText, commentsText, targetText);
                }

                keyText.label.setPadding(new Insets(3, 8, 5, 8));
                sourceText.label.setPadding(new Insets(6, 10, 3, 10));
                sourceText.labelStyle = "-fx-font-size: 14;";
                sourceText.fieldStyle = "-fx-font-size: 14;";
                sourceText.fieldDefaultHeight = 30;
                sourceText.setText(sourceText.getText());
            }else{ // compact mode
                pane.getChildren().setAll(bar, keyText, sourceText, targetText);
                keyText.label.setPadding(new Insets(0));
                sourceText.label.setPadding(new Insets(0));
                sourceText.labelStyle = "-fx-font-size: 12;";
                sourceText.fieldStyle = "-fx-font-size: 12;";
                sourceText.fieldDefaultHeight = 25;
                sourceText.setText(sourceText.getText());
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

        switch (type){
            case KEY -> listView.editedSinceLastSort = listView.getWindow().sourceTranslation.fileManager.updateTranslationKey(key, value);
            case SOURCE -> listView.editedSinceLastSort = listView.getWindow().sourceTranslation.fileManager.updateTranslationValue(key, value);
            case ALTERNATIVE -> listView.editedSinceLastSort = listView.getWindow().alternativeTranslation.fileManager.updateTranslationValue(key, value);
            case COMMENT -> listView.editedSinceLastSort = listView.getWindow().sourceTranslation.fileManager.updateTranslationComment(key, Arrays.asList(value.split(Pattern.quote("\n"))));
            case TARGET -> listView.editedSinceLastSort = listView.getWindow().targetTranslation.fileManager.updateTranslationValue(key, value);
        }

    }

    public void selectNext(String key){
        listView.getSelectionModel().select(key);
        Platform.runLater(() -> listView.scrollTo(listView.getSelectionModel().getSelectedIndex()));
        Platform.runLater(() -> listView.getSelectionModel().selectNext());
    }
    public void selectPrevious(String key){
        listView.getSelectionModel().select(key);
        Platform.runLater(() -> listView.scrollTo(listView.getSelectionModel().getSelectedIndex()));
        Platform.runLater(() -> listView.getSelectionModel().selectPrevious());
    }
}
