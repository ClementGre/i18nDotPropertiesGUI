package fr.clementgre.i18nTranslationManager.translationsList;


import fr.clementgre.i18nTranslationManager.utils.FitTextArea;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class TranslationSemiInput extends Pane {

    public final Label label = new Label();
    public FitTextArea textArea;

    private boolean hasText = false;

    public String labelStyle = "";
    public String fieldStyle = "";
    public int fieldDefaultHeight = 25;

    private final TranslationCell cell;
    private final TranslationCell.TextType type;
    public TranslationSemiInput(TranslationCell cell, TranslationCell.TextType type){
        this.cell = cell;
        this.type = type;

        label.setOnMouseClicked((e) -> {
            if(e.getClickCount() == 2) loadTextArea();
        });
        label.setWrapText(true);

        label.maxWidthProperty().bind(cell.listView.widthProperty().subtract(30));

        label.textProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if(textArea == null) setPrefHeight(label.getHeight());
            });
        });
        label.heightProperty().addListener((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if(textArea == null) setPrefHeight(label.getHeight());
            });
        });

        if(type == TranslationCell.TextType.KEY){
            labelStyle = "-fx-text-fill: #1b9dc9;";
            label.setPadding(new Insets(0));

        }else if(type == TranslationCell.TextType.COMMENT){
            labelStyle = "-fx-text-fill: #767676;";
            label.setPadding(new Insets(3, 8, 5, 8));

        }else if(type == TranslationCell.TextType.SOURCE){
            labelStyle = "-fx-font-size: 14;";
            fieldStyle = "-fx-font-size: 14;";
            fieldDefaultHeight = 30;
            label.setPadding(new Insets(6, 10, 3, 10));

        }else if(type == TranslationCell.TextType.ALTERNATIVE){
            labelStyle = "-fx-text-fill: #464646;";
            label.setPadding(new Insets(0, 8, 3, 38));
        }

        getChildren().add(label);
    }

    public void updateGraphics(){


        if(type == TranslationCell.TextType.KEY){
            if(cell.listView.isOneLineMode()){
                label.setPrefWidth(TranslationCell.KEY_PX);
                label.layoutXProperty().unbind();
                label.setLayoutX(0);
                label.setPadding(new Insets(0));

            }else{
                label.setPrefWidth(-1);
                label.layoutXProperty().bind(cell.pane.widthProperty().subtract(label.widthProperty()).divide(2));

                if(cell.listView.isCompactMode()){
                    label.setPadding(new Insets(0));
                }else{
                    label.setPadding(new Insets(3, 8, 5, 8));
                }
            }

        }else if(type == TranslationCell.TextType.SOURCE){
            if(cell.listView.isOneLineMode()){
                label.setPrefWidth(TranslationCell.SOURCE_PX);
            }else{
                label.setPrefWidth(-1);
            }

            if(cell.listView.isOneLineMode() || cell.listView.isCompactMode()){
                label.setPadding(new Insets(0));
                labelStyle = "-fx-font-size: 12;";
                fieldStyle = "-fx-font-size: 12;";
                fieldDefaultHeight = 25;
            }else{
                label.setPadding(new Insets(6, 10, 3, 10));
                labelStyle = "-fx-font-size: 14;";
                fieldStyle = "-fx-font-size: 14;";
                fieldDefaultHeight = 30;
            }

            setText(getText());

        }

        if(cell.listView.isOneLineMode()){
            label.layoutYProperty().bind(heightProperty().subtract(label.heightProperty()).divide(2));
        }else{
            label.layoutYProperty().unbind();
            label.setLayoutY(0);
        }

    }

    public void loadTextArea(){
        if(textArea != null) return;
        Platform.runLater(() -> {
            textArea = new FitTextArea(getText(), fieldDefaultHeight);
            textArea.setStyle(fieldStyle);
            textArea.setPadding(new Insets(-1));

            prefHeightProperty().unbind();
            textArea.heightProperty().addListener((observable, oldValue, newValue) -> {
                Platform.runLater(() -> setPrefHeight(newValue.doubleValue()));
            });

            if(cell.listView.isOneLineMode()) textArea.prefWidthProperty().bind(new SimpleIntegerProperty(TranslationCell.SOURCE_PX));
            else textArea.prefWidthProperty().bind(cell.pane.widthProperty().subtract(20));

            getChildren().setAll(textArea);
            textArea.requestFocus();

            textArea.textProperty().addListener((observable, oldValue, newValue) -> {
                if(type == TranslationCell.TextType.KEY){
                    String adjustedValue = newValue.trim().replaceAll("\\s+","").replace("\n", "");
                    if(adjustedValue != newValue){
                        textArea.setText(adjustedValue);
                        if(newValue.contains("\n")) cell.targetText.requestFocus();
                        return;
                    }
                }
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
        String value = hasText ? label.getText() : "";

        if(type == TranslationCell.TextType.KEY && !value.equals(cell.lastKey)){
            int i = 0;
            String newValue = value;
            while(newValue.isBlank() || cell.listView.getWindow().sourceTranslation.getTranslation(newValue) != null){ // check translation do not already exists
                i++;
                newValue = (value.isBlank() ? "unknown." : value+".") + i;
            }
            if(value != newValue){
                value = newValue;
                setText(value);
            }
        }

        cell.publishEditAuto(type, value);
    }

    public String getText(){
        if(hasText) return label.getText();
        else return "";
    }
    public StringProperty textProperty(){
        return label.textProperty();
    }
    public void setText(String text){
        if(text.isBlank()){
            hasText = false;
            label.setText("Add " + type.name().toLowerCase() + "...");

            if(type == TranslationCell.TextType.SOURCE){
                label.setStyle(labelStyle + "-fx-text-fill: #c20000;");
            }else{
                label.setStyle(labelStyle + "-fx-text-fill: #b0b0b0;");
            }

        }else{
            hasText = true;
            label.setText(text.trim());
            label.setStyle("-fx-text-fill: black; " + labelStyle);
        }
    }

}
