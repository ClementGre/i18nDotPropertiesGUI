package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TranslationsList extends ListView<FullTranslation> {

    MainWindowController mainWindow;
    public TranslationsList(MainWindowController mainWindow) {
        this.mainWindow = mainWindow;
    }

    public ObservableList<FullTranslation> getTranslations(){
        return getItems();
    }
    public void setTranslations(ObservableList<FullTranslation> translations){
        getItems().setAll(translations);
    }
    public void setTranslations(List<FullTranslation> translations){
        setTranslations(FXCollections.observableArrayList(translations));
    }

    public void scrollTo(String key){

    }
    public void scrollTo(Translation tr){

    }
    public void select(String key){

    }
    public void select(Translation tr){

    }
    public FullTranslation getSelected(){
        return getSelectionModel().getSelectedItem();
    }

    public void updateKeys(HashMap<String, Translation> sourceTranslations){
        if(getWindow().sourceTranslation.hasTranslations() && getWindow().targetTranslation.hasTranslations()){
            List<String> keys = toKeyList( sortItems(sourceTranslations) );
            setItems(FXCollections.observableArrayList(keys));
        }else{
            setItems(null);
        }
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
