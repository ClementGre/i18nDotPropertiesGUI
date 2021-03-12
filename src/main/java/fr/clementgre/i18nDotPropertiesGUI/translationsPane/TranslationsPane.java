package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import org.controlsfx.control.MasterDetailPane;
import java.util.HashMap;
import java.util.List;

public class TranslationsPane extends MasterDetailPane {

    private TranslationsList masterNode;
    private TranslationsDetails detailsNode;

    private MainWindowController mainWindow;
    public TranslationsPane(MainWindowController mainWindow){
        this.mainWindow = mainWindow;
        prefHeightProperty().bind(mainWindow.contentPane.heightProperty());

        masterNode = new TranslationsList(mainWindow);
        detailsNode = new TranslationsDetails();
        setMasterNode(masterNode);
        setDetailNode(detailsNode);
        setDetailSide(Side.BOTTOM);
        setShowDetailNode(true);

    }

    public ObservableList<FullTranslation> getTranslations(){
        return masterNode.getItems();
    }
    public void setTranslations(ObservableList<FullTranslation> translations){
        masterNode.getItems().setAll(translations);
    }
    public void setTranslations(List<FullTranslation> translations){
        setTranslations(FXCollections.observableArrayList(translations));
    }

    public void updateKeys(HashMap<String, Translation> sourceTranslations){
        masterNode.updateKeys(sourceTranslations);
    }

    public void scrollTo(String key){
        masterNode.scrollTo(key);
    }
    public void scrollTo(Translation tr){
        masterNode.scrollTo(tr);
    }
    public void select(String key){
        masterNode.select(key);
    }
    public void select(Translation tr){
        masterNode.select(tr);
    }
    public FullTranslation getSelected(){
        return masterNode.getSelected();
    }

    public MainWindowController getWindow(){
        return mainWindow;
    }

}
