package fr.clementgre.i18nDotPropertiesGUI.saving;

import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import fr.clementgre.i18nDotPropertiesGUI.utils.StringUtils;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import org.controlsfx.control.action.Action;

import java.io.File;
import java.util.HashMap;

public class FilePanel {

    public enum TranslationFileType{
        SOURCE, ALTERNATIVE, TARGET
    }

    private Label fileName;
    private TextField field;
    private Button browse;
    private Label status;

    public TranslationFileType type;
    private MainWindowController mainWindow;
    public FileManager fileManager;

    public FilePanel(Label fileName, TextField field, Button browse, Label status, TranslationFileType type, MainWindowController mainWindow) {
        this.fileName = fileName;
        this.field = field;
        this.browse = browse;
        this.status = status;
        this.type = type;
        this.mainWindow = mainWindow;

        fileManager = new FileManager(this);

        setup();
    }


    private void setup(){

        browse.setOnMouseClicked((e) -> {
            final FileChooser chooser = new FileChooser();
            chooser.setTitle("Sélectionner a file");
            chooser.setInitialDirectory(new File(field.getText()).exists() ? new File(field.getText()).getParentFile() : new File(System.getProperty("user.home")));

            File file = chooser.showOpenDialog(mainWindow);
            if(file != null){
               field.setText(file.getAbsolutePath());
            }
        });

        field.textProperty().addListener((observable, oldValue, newValue) -> {
            File file = new File(field.getText());
            if(file.exists()){
                fileName.setText(file.getName());
                fileManager.updateFile(file);
                MainWindowController.prefs.put("translationFilePath." + type.name().toLowerCase(), file.getAbsolutePath());
            }else{
                fileName.setText("No file selected");
                fileManager.updateFile(null);
                MainWindowController.prefs.put("translationFilePath." + type.name().toLowerCase(), "");
            }
            updateStatus();
        });

        Platform.runLater(() -> field.setText(MainWindowController.prefs.get("translationFilePath." + type.name().toLowerCase(), "")));

    }

    public void reloadFromDisk(){
        fileManager.updateFile(fileManager.file);
    }

    public void updateStatus(){
        if(fileManager.hasTranslations()){
            status.setText(getStatusText());
            HBox.setMargin(status, new Insets(4, 10, 4, 10));
        }else{
            status.setText(null);
            HBox.setMargin(status, new Insets(4, 0, 4, 0));
        }
    }

    private String getStatusText(){

        int count = getTranslations().size();
        if(getWindow().sourceTranslation.hasTranslations()) count = getSourceTranslations().size();
        int completed = (int) getTranslations().values().stream().filter((t) -> !t.getValue().isBlank()).count();
        int percentage = 100 * completed/count;

        return StringUtils.upperCaseFirstChar(type.name().toLowerCase()) + " : " + completed + "/" + count + " (" + percentage + "%)";

    }

    public void translationsListUpdated(){
        mainWindow.translationsPane.loadItems(getSourceTranslations(), getAlternativeTranslations(), getTargetTranslations());
    }

    public void save(){
        fileManager.updateTranslationsFromUserEdits();
        fileManager.saveTranslations();
    }

    public MainWindowController getWindow(){
        return mainWindow;
    }

    public boolean isSource(){
        return type == TranslationFileType.SOURCE;
    }

    public HashMap<String, Translation> getTranslations(){
        return fileManager.getTranslations();
    }
    public HashMap<String, Translation> getSourceTranslations(){
        return mainWindow.sourceTranslation.fileManager.getTranslations();
    }
    public HashMap<String, Translation> getTargetTranslations(){
        return mainWindow.targetTranslation.fileManager.getTranslations();
    }
    public HashMap<String, Translation> getAlternativeTranslations(){
        return mainWindow.alternativeTranslation.fileManager.getTranslations();
    }

    public boolean hasTranslations(){
        return fileManager.hasTranslations();
    }
}
