package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import javafx.scene.control.ListCell;

public class TranslationsListCell extends ListCell<FullTranslation> {

    MainWindowController mainWindow;
    public TranslationsListCell(MainWindowController mainWindow){
        this.mainWindow = mainWindow;
    }

    @Override
    public void updateItem(FullTranslation item, boolean empty) {
        super.updateItem(item, empty);

        if (empty || item == null) {
            setText(null);
            setGraphic(null);

        }else{
            setText(item.getKey() + " | " + item.getSourceTranslation() + " --> " + item.getTargetTranslation());
        }

    }
}
