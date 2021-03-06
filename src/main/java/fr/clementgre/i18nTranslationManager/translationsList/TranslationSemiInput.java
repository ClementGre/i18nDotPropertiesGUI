package fr.clementgre.i18nTranslationManager.translationsList;


import fr.clementgre.i18nTranslationManager.utils.FitTextArea;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

public class TranslationSemiInput extends Region {

    private final Label label = new Label();
    private FitTextArea textArea;

    private boolean hasText = false;

    private String labelStyle = "";
    private String fieldStyle = "";
    private int fieldDefaultHeight = 25;

    private final TranslationCell cell;
    private final TranslationCell.TextType type;
    public TranslationSemiInput(TranslationCell cell, TranslationCell.TextType type){
        this.cell = cell;
        this.type = type;

        label.setOnMousePressed((e) -> loadTextArea());
        label.setWrapText(true);
        label.maxWidthProperty().bind(cell.pane.widthProperty());

        label.setPadding(new Insets(4, 0, 4, 0));

        if(type == TranslationCell.TextType.KEY){
            label.layoutXProperty().bind(cell.pane.widthProperty().subtract(label.widthProperty()).divide(2));
            labelStyle = "-fx-text-fill: #1b9dc9;";
        }else if(type == TranslationCell.TextType.COMMENT){
            labelStyle = "-fx-text-fill: #464646;";
        }else if(type == TranslationCell.TextType.SOURCE){
            labelStyle = "-fx-font-size: 14;";
            fieldStyle = "-fx-font-size: 14;";
            fieldDefaultHeight = 30;
            label.setPadding(new Insets(5, 0, 5, 0));
        }else if(type == TranslationCell.TextType.ALTERNATIVE){
            labelStyle = "-fx-text-fill: #464646;";
        }

        getChildren().add(label);
    }

    public void loadTextArea(){
        if(textArea != null) return;
        Platform.runLater(() -> {
            textArea = new FitTextArea(getText(), fieldDefaultHeight);
            textArea.setStyle(fieldStyle);

            textArea.prefWidthProperty().bind(cell.pane.widthProperty());
            getChildren().setAll(textArea);
            textArea.requestFocus();

            textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                setText(newValue);
            });


            textArea.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if(!newValue){
                    unloadTextArea();
                }
            });
            textArea.setOnMouseClicked((e) -> {
                if(e.getClickCount() == 2){
                    unloadTextArea();
                }
            });


        });

    }

    public void unloadTextArea(){
        if(textArea == null) return;
        publishEdit();

        getChildren().setAll(label);
        textArea = null;
    }

    private void publishEdit(){
        cell.publishEditAuto(type, label.getText());
    }

    public String getText(){
        if(hasText) return label.getText();
        else return "";
    }
    public StringProperty textProperty(){
        return label.textProperty();
    }
    public void setText(String text){
        if(text.isEmpty()){
            hasText = false;
            label.setText("Add " + type.name().toLowerCase() + "...");
            label.setStyle("-fx-text-fill: #464646; " + labelStyle);
        }else{
            hasText = true;
            label.setText(text);
            label.setStyle("-fx-text-fill: black; " + labelStyle);
        }
    }

}
