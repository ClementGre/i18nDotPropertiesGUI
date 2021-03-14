package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import javafx.geometry.Side;
import org.controlsfx.control.MasterDetailPane;
import java.util.HashMap;
import java.util.Map;

public class TranslationsPane extends MasterDetailPane {

    private final TranslationsList masterNode;
    private final TranslationsDetails detailsNode;

    private final MainWindowController mainWindow;
    public TranslationsPane(MainWindowController mainWindow){
        this.mainWindow = mainWindow;
        prefHeightProperty().bind(mainWindow.contentPane.heightProperty());

        masterNode = new TranslationsList(mainWindow);
        detailsNode = new TranslationsDetails(mainWindow);
        setMasterNode(masterNode);
        setDetailNode(detailsNode);

        setDetailSide(MainWindowController.prefs.getBoolean("displayModes.sideEditPane", false) ? Side.RIGHT : Side.BOTTOM);

        setShowDetailNode(true);

        masterNode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            detailsNode.updateSelected(newValue);
        });

        dividerPositionProperty().addListener((observable, oldValue, newValue) -> {
            MainWindowController.prefs.putDouble("displayModes.editPaneDivider", newValue.doubleValue());
        });
        setDividerPosition(MainWindowController.prefs.getDouble("displayModes.editPaneDivider", .6));

    }

    public void updateDarkMode(){
        detailsNode.updateDarkTheme();
    }

    // SHORTCUTS TO MASTER NODE

    public void loadItems(HashMap<String, Translation> source, HashMap<String, Translation> target, HashMap<String, Translation> alternate){
        masterNode.loadItems(source, target, alternate);
    }
    public void reloadList(){
        masterNode.refresh();
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
    public void scrollTo(FullTranslation tr){
        masterNode.scrollTo(tr);
    }
    public void select(FullTranslation tr){
        masterNode.select(tr);
    }
    public void selectNext(){
        masterNode.selectNext();
    }
    public FullTranslation getSelected(){
        return masterNode.getSelected();
    }
    public MainWindowController getWindow(){
        return mainWindow;
    }

}
