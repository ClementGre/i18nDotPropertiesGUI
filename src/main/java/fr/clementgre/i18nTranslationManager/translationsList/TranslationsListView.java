package fr.clementgre.i18nTranslationManager.translationsList;

import fr.clementgre.i18nTranslationManager.MainWindowController;
import fr.clementgre.i18nTranslationManager.Translation;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.HashMap;

public class TranslationsListView extends ListView<String> {


    private MainWindowController mainWindow;

    public  TranslationsListView(MainWindowController mainWindow){
        this.mainWindow = mainWindow;

        setCellFactory(listView -> new TranslationCell(this));

        prefHeightProperty().bind(mainWindow.contentPane.heightProperty());

    }

    public void updateKeys(HashMap<String, Translation> sourceTranslations){
        setItems(FXCollections.observableArrayList(sourceTranslations.keySet()));
    }

    public MainWindowController getWindow(){
        return mainWindow;
    }

}
