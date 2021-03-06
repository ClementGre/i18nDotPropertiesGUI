package fr.clementgre.i18nTranslationManager.utils;

import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

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
        updateHeight();

        textProperty().addListener((ov, oldVal, newVal) -> {
            updateHeight();
        });
    }

    private void updateHeight(){
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
