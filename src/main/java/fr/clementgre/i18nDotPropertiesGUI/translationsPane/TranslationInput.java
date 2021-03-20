package fr.clementgre.i18nDotPropertiesGUI.translationsPane;


import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import java.util.function.Consumer;

public class TranslationInput extends TextArea {

    private String lastValue = "";
    private int defaultHeight = 25;
    private int fontSize = 12;
    private Consumer<String> updateTextEvent;
    private Runnable nextEvent;

    public TranslationInput(String text, int defaultHeight, int fontSize){
        super(text);
        this.defaultHeight = defaultHeight;
        this.fontSize = fontSize;
        setup();
    }
    public TranslationInput(int defaultHeight, int fontSize){
        super();
        this.defaultHeight = defaultHeight;
        this.fontSize = fontSize;
        setup();
    }
    public TranslationInput(String text){
        super(text);
        setup();
    }
    public TranslationInput(){
        super();
        setup();
    }

    // TEXT EVENT

    public void setUpdateTextEvent(Consumer<String> updateTextEvent){
        this.updateTextEvent = updateTextEvent;
    }
    private void updateTextEvent(){
       if(updateTextEvent != null) updateTextEvent.accept(lastValue);
    }
    public void updateText(){
        setText(getValue());
        if(hasUnsavedValue()){
            lastValue = getValue();
            updateTextEvent();
        }
    }
    public void setValue(String text){
        if(text == null) text = "";
        lastValue = text;
        setText(text);
    }
    public String getValue(){
        return getText() == null ? "" : getText().trim();
    }
    public boolean hasUnsavedValue(){
        return !lastValue.equals(getValue());
    }

    // NEXT EVENT

    public void setNextEvent(Runnable nextEvent){
        this.nextEvent = nextEvent;
    }
    private void nextEvent(){
        if(nextEvent != null) nextEvent.run();
    }

    private void setup(){

        setMinHeight(0);
        setWrapText(true);
        setStyle("-fx-font-size: " + fontSize + ";");
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

        focusedProperty().addListener((ov, oldVal, newVal)-> {
            if(!newVal) updateText();
        });

        addEventFilter(KeyEvent.KEY_PRESSED, (e) -> {
            if(e.getCode() == KeyCode.TAB ||
                    (e.getCode() == KeyCode.ENTER && e.isShiftDown())){

                e.consume();
                nextEvent();
            }
        });
    }



    public void updateHeight(){
        Text t = (Text) lookup(".text");
        if(t == null){
            setPrefHeight(defaultHeight);
        }else{
            double border = 2 * 2;
            double padding = t.getLayoutY() * 2;
            setPrefHeight(border + padding + t.getBoundsInLocal().getHeight());

        }
    }

}
