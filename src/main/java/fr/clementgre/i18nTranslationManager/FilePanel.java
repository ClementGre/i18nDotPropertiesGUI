package fr.clementgre.i18nTranslationManager;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class FilePanel {

    public enum TranslationFileType{
        SOURCE, ALTERNATIVE, TARGET
    }

    private Label fileName;
    private TextField field;
    private Button browse;
    private TranslationFileType type;
    private MainWindowController mainWindow;

    private FileManager fileManager;

    public FilePanel(Label fileName, TextField field, Button browse, TranslationFileType type, MainWindowController mainWindow) {
        this.fileName = fileName;
        this.field = field;
        this.browse = browse;
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
            }else{
                fileName.setText("No file selected");
                fileManager.updateFile(null);
            }
        });

    }

    public void translationsListUpdated(){
        if(type == TranslationFileType.SOURCE){
            mainWindow.translations.updateKeys(fileManager.getTranslations());
        }else{
            System.out.println("updated");
            mainWindow.translations.updateKeys(mainWindow.sourceTranslation.fileManager.getTranslations());
        }
    }

    public Window getWindow(){
        return mainWindow;
    }

    public Translation getTranslation(String key){
        return fileManager.getTranslation(key);
    }

    public boolean hasTranslations(){
        return fileManager.hasTranslations();
    }
}
