package fr.clementgre.i18nTranslationManager.translationsList;

import fr.clementgre.i18nTranslationManager.MainWindowController;
import fr.clementgre.i18nTranslationManager.Translation;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TranslationsListView extends ListView<String> {


    private MainWindowController mainWindow;
    public boolean compactMode = false;

    public  TranslationsListView(MainWindowController mainWindow){
        this.mainWindow = mainWindow;

        setCellFactory(listView -> new TranslationCell(this));
        prefHeightProperty().bind(mainWindow.contentPane.heightProperty());

    }

    public void updateKeys(HashMap<String, Translation> sourceTranslations){
        if(getWindow().sourceTranslation.hasTranslations() && getWindow().targetTranslation.hasTranslations()){
            List<String> keys = new ArrayList<>(sourceTranslations.keySet());
            keys.sort(String::compareTo);
            setItems(FXCollections.observableArrayList(keys));
        }else{
            setItems(null);
        }
    }

    public MainWindowController getWindow(){
        return mainWindow;
    }

}
