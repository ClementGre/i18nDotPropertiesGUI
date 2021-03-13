package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import javafx.geometry.Side;
import org.controlsfx.control.MasterDetailPane;
import java.util.HashMap;
import java.util.Map;

public class TranslationsPane extends MasterDetailPane {

    private TranslationsList masterNode;
    private TranslationsDetails detailsNode;

    private MainWindowController mainWindow;
    public TranslationsPane(MainWindowController mainWindow){
        this.mainWindow = mainWindow;
        prefHeightProperty().bind(mainWindow.contentPane.heightProperty());

        masterNode = new TranslationsList(mainWindow);
        detailsNode = new TranslationsDetails(mainWindow);
        setMasterNode(masterNode);
        setDetailNode(detailsNode);
        setDetailSide(Side.BOTTOM);
        setShowDetailNode(true);

        masterNode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            detailsNode.updateSelected(newValue);
        });

    }

    // SHORTCUTS TO MASTER NODE

    public void loadItems(HashMap<String, Translation> source, HashMap<String, Translation> target, HashMap<String, Translation> alternate){
        masterNode.loadItems(source, target, alternate);
    }
    public Map<String, Translation> getSourceTranslations(){
        return masterNode.getSourceTranslations();
    }
    public Map<String, Translation> getAlternativeTranslations(){
        return masterNode.getAlternativeTranslations();
    }
    public Map<String, Translation> getTargetTranslations(){
        return masterNode.getTargetTranslations();
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
