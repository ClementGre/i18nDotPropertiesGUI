package fr.clementgre.i18nTranslationManager.translationsList;

import fr.clementgre.i18nTranslationManager.MainWindowController;
import fr.clementgre.i18nTranslationManager.Translation;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TranslationsListView extends ListView<String> {


    private MainWindowController mainWindow;
    public boolean editedSinceLastSort = false;
    public BooleanProperty needRefresh = new SimpleBooleanProperty(false);

    public TranslationsListView(MainWindowController mainWindow){
        this.mainWindow = mainWindow;

        setCellFactory(listView -> new TranslationCell(this, needRefresh));
        prefHeightProperty().bind(mainWindow.contentPane.heightProperty());

        mainWindow.sortMode.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            sortAuto();
            getWindow().prefs.putInt("displayModes.sortMode", newValue.intValue());
        });
        mainWindow.compactMode.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            setItems(null);
            Platform.runLater(() -> sortAuto());
            getWindow().prefs.putInt("displayModes.compactMode", newValue.intValue());
        });

        getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(editedSinceLastSort){
                editedSinceLastSort = false;
                sortAuto();
            }
        });

    }

    public boolean isCompactMode(){
        return mainWindow.compactMode.getSelectionModel().getSelectedIndex() == 1;
    }
    public boolean isOneLineMode(){
        return mainWindow.compactMode.getSelectionModel().getSelectedIndex() == 2;
    }

    public void updateKeys(HashMap<String, Translation> sourceTranslations){
        if(getWindow().sourceTranslation.hasTranslations() && getWindow().targetTranslation.hasTranslations()){
            List<String> keys = toKeyList( sortItems(sourceTranslations) );
            setItems(FXCollections.observableArrayList(keys));
        }else{
            setItems(null);
        }
        needRefresh.setValue(!needRefresh.getValue());
    }

    public void sortAuto(){
        updateKeys(getWindow().sourceTranslation.getTranslations());
    }

    private List<Translation> sortItems(HashMap<String, Translation> sourceTranslations){
        return sortItems(new ArrayList<>(sourceTranslations.values()));
    }
    private List<Translation> sortItems(List<Translation> translations){
        if(getSortMode() == 0 || getSortMode() == -1){
            translations.sort((o1, o2) -> {
                int v1 = getTranslationSortValue(o1);
                int v2 = getTranslationSortValue(o2);
                if(v1 == v2){
                    return o1.getKey().compareTo(o2.getKey());
                }
                return v1 - v2;
            });
        }else if (getSortMode() == 1){
            translations.sort(Translation::compareTo);
        }
        return translations;
    }
    private int getTranslationSortValue(Translation translation){

        int output = 0;
        output += getWindow().sourceTranslation.getTranslation(translation.getKey()).getValue().isBlank() ? 0 : 10;

        if(getWindow().targetTranslation.getTranslation(translation.getKey()) != null)
            output += getWindow().targetTranslation.getTranslation(translation.getKey()).getValue().isBlank() ? 0 : 5;
        if(getWindow().alternativeTranslation.hasTranslations())
            if(getWindow().alternativeTranslation.getTranslation(translation.getKey()) != null)
                output += getWindow().alternativeTranslation.getTranslation(translation.getKey()).getValue().isBlank() ? 0 : 1;

        return output;
    }
    private List<String> toKeyList(List<Translation> translations){
        return translations.stream().map(Translation::getKey).collect(Collectors.toList());
    }

    private int getSortMode(){
        return mainWindow.sortMode.getSelectionModel().getSelectedIndex();
    }

    public MainWindowController getWindow(){
        return mainWindow;
    }

}
