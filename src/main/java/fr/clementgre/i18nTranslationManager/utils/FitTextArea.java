package fr.clementgre.i18nTranslationManager.utils;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import javax.swing.event.ChangeListener;

public class FitTextArea extends TextArea {

    private int defaultHeight = 25;
    public FitTextArea(String text, int defaultHeight){
        super(text);
        this.defaultHeight = defaultHeight;
        setup();
    }
    public FitTextArea(int defaultHeight){
        super();
        this.defaultHeight = defaultHeight;
        setup();
    }
    public FitTextArea(String text){
        super(text);
        setup();
    }
    public FitTextArea(){
        super();
        setup();
    }

    private void setup(){

        setMinHeight(0);
        setWrapText(true);
        Platform.runLater(this::updateHeight);

        textProperty().addListener((ov, oldVal, newVal) -> {
            Platform.runLater(this::updateHeight);
        });
        styleProperty().addListener((ov, oldVal, newVal) -> {
            Platform.runLater(this::updateHeight);
        });
        getChildren().addListener((ListChangeListener<Node>) c -> {
            Platform.runLater(this::updateHeight);
        });
        widthProperty().addListener((ov, oldVal, newVal)-> {
            Platform.runLater(this::updateHeight);
        });
    }

    public void updateHeight(){
        Text t = (Text) lookup(".text");
        if(t == null){
            setHeightAuto(defaultHeight);
        }else{
            double border = 2 * 2;
            double padding = t.getLayoutY() * 2;
            setHeightAuto(border + padding + t.getBoundsInLocal().getHeight());

        }
    }
    private void setHeightAuto(double height){
        setHeightAuto((int) height);
    }
    private void setHeightAuto(int height){
        if(height != getHeight()){
            setPrefHeight(height);
        }
    }

}
