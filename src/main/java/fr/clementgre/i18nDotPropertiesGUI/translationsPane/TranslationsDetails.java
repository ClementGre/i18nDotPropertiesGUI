package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import javafx.scene.layout.VBox;

public class TranslationsDetails extends VBox {

    FullTranslation translation = null;

    private MainWindowController mainWindow;
    public TranslationsDetails(MainWindowController mainWindow){
        this.mainWindow = mainWindow;

        setupGraphics();

    }

    private void setupGraphics(){



    }

    private void updateGraphics(){



    }

    public void updateSelected(FullTranslation translation){
        this.translation = translation;
        updateGraphics();
    }

}
