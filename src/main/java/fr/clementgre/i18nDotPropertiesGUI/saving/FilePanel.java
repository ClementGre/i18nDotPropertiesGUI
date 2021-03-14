package fr.clementgre.i18nDotPropertiesGUI.saving;

import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

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
                mainWindow.prefs.put("translationFilePath." + type.name().toLowerCase(), file.getAbsolutePath());
            }else{
                fileName.setText("No file selected");
                fileManager.updateFile(null);
                mainWindow.prefs.put("translationFilePath." + type.name().toLowerCase(), "");
            }
            updateStatus();
        });

        Platform.runLater(() -> {
            field.setText(mainWindow.prefs.get("translationFilePath." + type.name().toLowerCase(), ""));
        });

    }

    public void reloadFromDisk(){
        fileManager.updateFile(fileManager.file);
    }

    public void updateStatus(){
        if(fileManager.hasTranslations()){
            status.setText(getStatusText());
        }else{
            status.setText(null);
        }
    }

    private String getStatusText(){
        HashMap<String, Translation> translations = getTranslations();
        int count = translations.size();
        int completed = (int) translations.values().stream().filter((t) -> !t.getValue().isBlank()).count();
        int percentage = 100 * completed/count;

        return type.name().toLowerCase() + " : " + completed + "/" + count + " (" + percentage + "%)";

    }

    public void saveTranslations(){
        fileManager.saveTranslations();
    }

    public void translationsListUpdated(){
        mainWindow.translationsPane.loadItems(getSourceTranslations(), getTargetTranslations(), getAlternativeTranslations());
    }

    public MainWindowController getWindow(){
        return mainWindow;
    }

    public boolean isSource(){
        return type == TranslationFileType.SOURCE;
    }

    public Translation getTranslation(String key){
        return fileManager.getTranslation(key);
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
