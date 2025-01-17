package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.utils.StringUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

public class TranslationsDetails extends ScrollPane {

    FullTranslation translation = null;

    private final VBox content = new VBox();

    private final Label keyLabel = new Label();
    private final TranslationSemiInput sourceSemiInput = new TranslationSemiInput("source translation", true, false, true);
    private final TranslationSemiInput alternativeSemiInput = new TranslationSemiInput("alternative translation", false, true, false);
    private final TranslationSemiInput commentsSemiInput = new TranslationSemiInput("comments", false, true, false);
    private final TranslationInput targetInput = new TranslationInput(30, 14);

    private final MainWindowController mainWindow;
    public TranslationsDetails(MainWindowController mainWindow){
        this.mainWindow = mainWindow;

        setupGraphics();
        updateDarkTheme();
    }

    public void updateDarkTheme(){
        if(MainWindowController.prefs.getBoolean("displayModes.darkMode", true)){
            content.setStyle("-fx-background-color: #333333;");
        }else{
            content.setStyle("");
        }
        sourceSemiInput.updateText();
        alternativeSemiInput.updateText();
        commentsSemiInput.updateText();
    }

    private void setupGraphics(){

        keyLabel.setStyle("-fx-text-fill: #0097c0;");

        setFitToWidth(true);
        setFitToHeight(true);
        content.setFillWidth(true);
        content.setAlignment(Pos.TOP_CENTER);

        VBox.setMargin(sourceSemiInput, new Insets(10, 10, -3, 10));
        VBox.setMargin(alternativeSemiInput, new Insets(-3, 10, 0, 30));
        VBox.setMargin(commentsSemiInput, new Insets(0, 10, 1, 10));
        VBox.setMargin(targetInput, new Insets(0, 10, 5, 10));

        sourceSemiInput.setNextEvent(targetInput::requestFocus);
        alternativeSemiInput.setNextEvent(targetInput::requestFocus);
        commentsSemiInput.setNextEvent(targetInput::requestFocus);
        targetInput.setNextEvent(() -> {
            mainWindow.translationsPane.selectNext();
        });
        targetInput.addEventFilter(KeyEvent.KEY_PRESSED, (e) -> {
            if(e.getCode() == KeyCode.SPACE && e.isControlDown()){
                e.consume();
                addArg();
            }else if(e.getCode() == KeyCode.C && e.isShortcutDown() && e.isShiftDown()){
                e.consume();
                copySource();
            }
        });

        sourceSemiInput.setUpdateTextEvent((text) -> {
            if (translation != null) translation.setSourceTranslation(text);
        });
        alternativeSemiInput.setUpdateTextEvent((text) -> {
            if(translation != null) translation.setAlternativeTranslation(text);
        });
        commentsSemiInput.setUpdateTextEvent((text) -> {
            if(translation != null) translation.setComments(text);
        });
        targetInput.setUpdateTextEvent((text) -> {
            if(translation != null) translation.setTargetTranslation(text);
        });

        setContent(content);
        setHbarPolicy(ScrollBarPolicy.NEVER);

    }

    private void addArg(){
        String text = targetInput.getText();
        int i = 0;
        while(text.contains("{" + i + "}")) i++;
        if(sourceSemiInput.getText().contains("{" + i + "}")){
            targetInput.setText(text + "{" + i + "}");
            targetInput.end();
        }
    }
    private void copySource(){
        String text = targetInput.getText();
        int i = 0;
        while(text.contains("{" + i + "}")) i++;


        if(i != 0){
            targetInput.setText(text + StringUtils.removeBeforeLastRegex(sourceSemiInput.getText(), "{" + (i-1) + "}"));
        }else{
            targetInput.setText(text + sourceSemiInput.getText());
        }
        targetInput.end();
    }

    private void updateGraphics(){
        if(translation != null){
            keyLabel.setText(translation.getKey());
            sourceSemiInput.setText(translation.getSourceTranslation());
            alternativeSemiInput.setText(translation.getAlternativeTranslation());
            commentsSemiInput.setText(translation.getComments());
            targetInput.setValue(translation.getTargetTranslation());
            if(mainWindow.alternativeTranslation.hasTranslations()){
                content.getChildren().setAll(keyLabel, sourceSemiInput, alternativeSemiInput, commentsSemiInput, targetInput);
            }else content.getChildren().setAll(keyLabel, sourceSemiInput, commentsSemiInput, targetInput);
        }else{
            content.getChildren().clear();
        }
    }

    void saveValues(){
        if(sourceSemiInput.hasUnsavedValue()) translation.setSourceTranslation(sourceSemiInput.getText());
        if(alternativeSemiInput.hasUnsavedValue()) translation.setAlternativeTranslation(alternativeSemiInput.getText());
        if(commentsSemiInput.hasUnsavedValue()) translation.setComments(commentsSemiInput.getText());
        if(targetInput.hasUnsavedValue()) translation.setTargetTranslation(targetInput.getValue());
    }

    public void updateSelected(FullTranslation translation){
        saveValues();
        this.translation = translation;
        updateGraphics();
        Platform.runLater(() -> {
            targetInput.requestFocus();
            targetInput.end();
        });
    }

}
