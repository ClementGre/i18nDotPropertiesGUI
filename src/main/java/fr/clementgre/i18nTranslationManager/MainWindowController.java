package fr.clementgre.i18nTranslationManager;

import fr.clementgre.i18nTranslationManager.translationsList.TranslationsListView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

public class MainWindowController extends Stage {

    public FilePanel sourceTranslation;
    public FilePanel alternativeTranslation;
    public FilePanel targetTranslation;

    //

    public MenuBar menuBar;
    public VBox contentPane;
    public HBox bottomBar;

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


    public TranslationsListView translations;

    // bottom bar

    public ComboBox sortMode;
    public CheckBox smallMode;

    public Label targetStatus;
    public Label sourceStatus;
    public Label alternativeStatus;

    @FXML
    public void initialize(){

        new JMetro(menuBar, Style.DARK);
        new JMetro(bottomBar, Style.DARK);
        menuBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        bottomBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);


        Platform.runLater(() -> {
            sourceTranslation = new FilePanel(sourceTranslationText, sourceTranslationField, sourceTranslationBrowse, sourceStatus, FilePanel.TranslationFileType.SOURCE, this);
            alternativeTranslation = new FilePanel(alternativeTranslationText, alternativeTranslationField, alternativeTranslationBrowse, alternativeStatus, FilePanel.TranslationFileType.ALTERNATIVE, this);
            targetTranslation = new FilePanel(targetTranslationText, targetTranslationField, targetTranslationBrowse, targetStatus, FilePanel.TranslationFileType.TARGET, this);

            translations = new TranslationsListView(this);

            contentPane.getChildren().add(translations);

            sourceTranslationField.setText("C:\\Users\\Clement\\Developpement\\Java\\PDF4Teachers\\src\\main\\resources\\translations\\strings_fr_fr.properties");
            alternativeTranslationField.setText("C:\\Users\\Clement\\Developpement\\Java\\PDF4Teachers\\src\\main\\resources\\translations\\strings_fr_fr.properties");
            targetTranslationField.setText("C:\\Users\\Clement\\Developpement\\Java\\PDF4Teachers\\src\\main\\resources\\translations\\strings_en_us.properties");

        });



    }


}
