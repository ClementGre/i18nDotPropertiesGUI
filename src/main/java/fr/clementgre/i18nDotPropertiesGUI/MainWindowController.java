package fr.clementgre.i18nDotPropertiesGUI;

import fr.clementgre.i18nDotPropertiesGUI.saving.FilePanel;
import fr.clementgre.i18nDotPropertiesGUI.translationsPane.TranslationsPane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import org.controlsfx.control.ToggleSwitch;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.prefs.Preferences;

public class MainWindowController extends Stage {

    public static final Preferences prefs = Preferences.userRoot().node("fr.clementgre.i18nDotPropertiesGUI.MainWindowController");

    //

    public FilePanel sourceTranslation;
    public FilePanel alternativeTranslation;
    public FilePanel targetTranslation;

    //

    public AutoHideNotificationPane notificationPane = new AutoHideNotificationPane();
    public MenuBar menuBar;
    public VBox topBar;
    public VBox contentPane;
    public HBox bottomBar;

    // Menu Items

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

    public ToggleSwitch wrapText;
    public ToggleSwitch sideEditPane;
    public ToggleSwitch compactMode;
    public ToggleSwitch darkMode;

    public Label targetStatus;
    public Label sourceStatus;
    public Label alternativeStatus;

    @FXML
    public void initialize(){

        new JMetro(topBar, Style.DARK);
        new JMetro(bottomBar, Style.DARK);
        topBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        bottomBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        notificationPane.setShowFromTop(false);


        Platform.runLater(() -> {
            // Translations loaders
            sourceTranslation = new FilePanel(sourceTranslationText, sourceTranslationField, sourceTranslationBrowse, sourceStatus, FilePanel.TranslationFileType.SOURCE, this);
            alternativeTranslation = new FilePanel(alternativeTranslationText, alternativeTranslationField, alternativeTranslationBrowse, alternativeStatus, FilePanel.TranslationFileType.ALTERNATIVE, this);
            targetTranslation = new FilePanel(targetTranslationText, targetTranslationField, targetTranslationBrowse, targetStatus, FilePanel.TranslationFileType.TARGET, this);

            // content Pane
            translationsPane = new TranslationsPane(this);
            notificationPane.setContent(translationsPane);

            contentPane.getChildren().add(notificationPane);
        });

        // SETTINGS

        compactMode.setSelected(prefs.getBoolean("displayModes.compactMode", true));
        sideEditPane.setSelected(prefs.getBoolean("displayModes.sideEditPane", false));
        wrapText.setSelected(prefs.getBoolean("displayModes.wrapText", true));
        darkMode.setSelected(prefs.getBoolean("displayModes.darkMode", true));

        compactMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            prefs.putBoolean("displayModes.compactMode", newValue);
            translationsPane.reloadList();
        });
        sideEditPane.selectedProperty().addListener((observable, oldValue, newValue) -> {
            prefs.putBoolean("displayModes.sideEditPane", newValue);
            translationsPane.setDetailSide(newValue ? Side.RIGHT : Side.BOTTOM);
        });
        wrapText.selectedProperty().addListener((observable, oldValue, newValue) -> {
            prefs.putBoolean("displayModes.wrapText", newValue);
            translationsPane.reloadList();
        });
        darkMode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            prefs.putBoolean("displayModes.darkMode", newValue);
            new JMetro(Main.root, newValue ? Style.DARK : Style.LIGHT);
            translationsPane.updateDarkMode();
        });

        // MENU BAR

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


    public void showNotification(String type, String text, int autoHide){
        notificationPane.addToPending(text, type, autoHide);
    }


}
