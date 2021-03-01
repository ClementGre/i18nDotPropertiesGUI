package fr.clementgre.i18nTranslationManager;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;

public class MainWindowController extends Stage {

    FilePanel sourceTranslation;
    FilePanel alternativeTranslation;
    FilePanel targetTranslation;

    //

    public MenuBar menuBar;
    public VBox contentPane;
    public HBox bottomBar;

    public Label sourceTranslationText;
    public TextField sourceTranslationField;
    public Button sourceTranslationBrowse;

    public Label alternativeTranslationText;
    public TextField alternativeTranslationField;
    public Button alternativeTranslationBrowse;

    public Label targetTranslationText;
    public TextField targetTranslationField;
    public Button targetTranslationBrowse;

    @FXML
    public void initialize(){

        new JMetro(menuBar, Style.DARK);
        new JMetro(bottomBar, Style.DARK);
        menuBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);
        bottomBar.getStyleClass().add(JMetroStyleClass.BACKGROUND);


        sourceTranslation = new FilePanel(sourceTranslationText, sourceTranslationField, sourceTranslationBrowse, FilePanel.TranslationFileType.SOURCE, this);
        alternativeTranslation = new FilePanel(alternativeTranslationText, alternativeTranslationField, alternativeTranslationBrowse, FilePanel.TranslationFileType.ALTERNATIVE, this);
        targetTranslation = new FilePanel(targetTranslationText, targetTranslationField, targetTranslationBrowse, FilePanel.TranslationFileType.TARGET, this);



    }


}
