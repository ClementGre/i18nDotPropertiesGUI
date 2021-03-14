package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class TranslationSemiInput extends Pane {

    private final Label label = new Label();
    private final TranslationInput input;

    private Consumer<String> updateTextEvent;

    private final String labelStyle;
    private final String typeName;

    private final boolean lightFont;

    public TranslationSemiInput(String typeName, boolean bigFont, boolean lightFont){
        this.typeName = typeName;
        this.lightFont = lightFont;

        if(bigFont) input = new TranslationInput(30, 14);
        else input = new TranslationInput();

        labelStyle = "-fx-padding: 5 2 5 2; -fx-font-size: " + (bigFont ? 14 : 12) + ";";

        input.prefWidthProperty().bind(widthProperty());
        label.maxWidthProperty().bind(widthProperty());

        setup();
        updateText();

    }

    private void setup(){

        label.setWrapText(true);

        setOnMouseClicked((e) -> {
            loadTextArea();
        });
        input.focusedProperty().addListener((ov, oldVal, newVal)-> {
            if(!newVal) unloadTextArea();
        });
        input.setUpdateTextEvent((text) -> {
            setText(text);
            updateTextEvent();
        });

        loadLabel();

    }
    public void updateText(){
        if(input.getValue().isBlank()){
            label.setText("Add " + typeName + "...");
            if(MainWindowController.prefs.getBoolean("displayModes.darkMode", true)) label.setStyle(labelStyle + " -fx-text-fill: #c0c0c0;");
            else label.setStyle(labelStyle + " -fx-text-fill: #787878;");

        }else{
            label.setText(input.getValue());
            if(lightFont){
                if(MainWindowController.prefs.getBoolean("displayModes.darkMode", true)) label.setStyle(labelStyle + " -fx-text-fill: #d8d8d8;");
                else label.setStyle(labelStyle + " -fx-text-fill: #616161;");
            }
            else label.setStyle(labelStyle);
        }
    }
    public void setUpdateTextEvent(Consumer<String> updateTextEvent){
        this.updateTextEvent = updateTextEvent;
    }
    private void updateTextEvent(){
        if(updateTextEvent != null) updateTextEvent.accept(getText());
    }
    public void loadLabel(){
        getChildren().setAll(label);
        prefHeightProperty().unbind();
        prefHeightProperty().bind(label.heightProperty());
    }
    public void loadTextArea(){
        getChildren().setAll(input);
        input.requestFocus();
        prefHeightProperty().unbind();
        prefHeightProperty().bind(input.heightProperty());
    }

    private void unloadTextArea(){
        updateText();
        loadLabel();
    }

    public void setNextEvent(Runnable nextEvent){
        input.setNextEvent(nextEvent);
    }

    public boolean hasUnsavedValue(){
        return input.hasUnsavedValue();
    }
    public String getText(){
        return input.getValue();
    }
    public StringProperty textProperty(){
        return label.textProperty();
    }
    public void setText(String text){
        input.setValue(text);
        updateText();
    }

}
