package fr.clementgre.i18nDotPropertiesGUI;

import fr.clementgre.i18nDotPropertiesGUI.translationsPane.TranslationsPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

import java.util.prefs.Preferences;

public class MainWindowController extends Stage {

    public Preferences prefs;

    //

    public FilePanel sourceTranslation;
    public FilePanel alternativeTranslation;
    public FilePanel targetTranslation;

    //

    public MenuBar menuBar;
    public VBox contentPane;
    public HBox bottomBar;

    // Menu Items

    public MenuItem menuNew;
    public MenuItem menuDelete;
    public MenuItem menuReload;
    public MenuItem menuSave;

    //

    public Label sourceTranslationText;
    public TextField sourceTranslationField;
    public Button sourceTranslationBrowse;

    public Label alternativeTranslationText;
    public TextField alternativeTranslationField;
    public Button alternativeTranslationBrowse;

    public Label targetTranslationText;
    public TextField targetTranslationField;
    public Button targetTranslationBrowse;
    public TranslationsPane translationsPane;

    // bottom bar

    public ComboBox<String> sortMode;
    public ComboBox<String> compactMode;

    public Label targetStatus;
    public Label sourceStatus;
    public Label alternativeStatus;



    @FXML
    public void initialize(){

        prefs = Preferences.userRoot().node("fr.clementgre.i18nDotPropertiesGUI.MainWindowController");

        new JMetro(menuBar, Style.DARK);
        new JMetro(bottomBar, Style.DARK);
        menuBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        bottomBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        Platform.runLater(() -> {

            // Translations loaders
            sourceTranslation = new FilePanel(sourceTranslationText, sourceTranslationField, sourceTranslationBrowse, sourceStatus, FilePanel.TranslationFileType.SOURCE, this);
            alternativeTranslation = new FilePanel(alternativeTranslationText, alternativeTranslationField, alternativeTranslationBrowse, alternativeStatus, FilePanel.TranslationFileType.ALTERNATIVE, this);
            targetTranslation = new FilePanel(targetTranslationText, targetTranslationField, targetTranslationBrowse, targetStatus, FilePanel.TranslationFileType.TARGET, this);

            // content Pane
            translationsPane = new TranslationsPane(this);
            contentPane.getChildren().add(translationsPane);

        });

        // SETTINGS

        sortMode.getSelectionModel().select(prefs.getInt("displayModes.sortMode", 0));
        compactMode.getSelectionModel().select(prefs.getInt("displayModes.compactMode", 0));

        sortMode.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            prefs.putInt("displayModes.sortMode", newValue.intValue());
        });
        compactMode.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            prefs.putInt("displayModes.compactMode", newValue.intValue());
        });

        // MENU BAR

        menuNew.setOnAction((e) -> {
            if(sourceTranslation.hasTranslations() && targetTranslation.hasTranslations()){
                String key = sourceTranslation.fileManager.addTranslation();
                Platform.runLater(() -> {
                    translationsPane.select(key);
                });

            }
        });
        menuDelete.setOnAction((e) -> {
            String key = translationsPane.getSelected().getKey();
            if(key != null && !key.isBlank()){
                sourceTranslation.fileManager.deleteTranslation(key);
            }
        });
        menuReload.setOnAction((e) -> {
            sourceTranslation.reloadFromDisk();
            alternativeTranslation.reloadFromDisk();
            targetTranslation.reloadFromDisk();
        });
        menuSave.setOnAction((e) -> {
            sourceTranslation.saveTranslations();
            alternativeTranslation.saveTranslations();
            targetTranslation.saveTranslations();
        });
    }


}
