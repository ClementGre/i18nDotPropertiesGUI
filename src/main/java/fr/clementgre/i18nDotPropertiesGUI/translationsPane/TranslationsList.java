package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TranslationsList extends ListView<FullTranslation> {

    MainWindowController mainWindow;
    public TranslationsList(MainWindowController mainWindow) {
        this.mainWindow = mainWindow;

        setCellFactory(param -> new TranslationsListCell(getWindow()));
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
    public ReadOnlyObjectProperty<FullTranslation> selectedProperty(){
        return getSelectionModel().selectedItemProperty();
    }


    public void loadItems(HashMap<String, Translation> source, HashMap<String, Translation> target, HashMap<String, Translation> alternate){
        if(target.isEmpty() || source.isEmpty()){
            setItems(null);
            return;
        }
        List<FullTranslation> translations = source.values().stream().map((tr) ->
            new FullTranslation(tr.getKey(), tr.getComments(), tr.getValue(),
                    alternate.containsKey(tr.getKey()) ? alternate.get(tr.getKey()).getValue() : "",
                    target.containsKey(tr.getKey()) ? target.get(tr.getKey()).getValue() : "")).collect(Collectors.toList());
        setItems(FXCollections.observableList(translations));
    }

    public void sortItems(){
        if(getSortMode() == 0 || getSortMode() == -1){
            getItems().sort((o1, o2) -> {
                int v1 = getTranslationSortValue(o1);
                int v2 = getTranslationSortValue(o2);
                if(v1 == v2){
                    return o1.getKey().compareTo(o2.getKey());
                }
                return v1 - v2;
            });
        }else if (getSortMode() == 1){
            getItems().sort(FullTranslation::compareTo);
        }
    }
    private int getTranslationSortValue(FullTranslation translation){
        int output = 0;
        output += translation.getSourceTranslation().isBlank() ? 0 : 10;
        output += translation.getTargetTranslation().isBlank() ? 0 : 5;
        output += translation.getAlternativeTranslation().isBlank() ? 0 : 1;
        return output;
    }
    public Map<String, Translation> getSourceTranslations(){
        return getItems().stream()
                .map((tr) -> new Translation(tr.getComments(), tr.getKey(), tr.getSourceTranslation()) )
                .collect(Collectors.toMap(Translation::getKey, tr -> tr));
    }
    public Map<String, Translation> getAlternativeTranslations(){
        return getItems().stream()
                .map((tr) -> new Translation(tr.getComments(), tr.getKey(), tr.getAlternativeTranslation()) )
                .collect(Collectors.toMap(Translation::getKey, tr -> tr));
    }
    public Map<String, Translation> getTargetTranslations(){
        return getItems().stream()
                .map((tr) -> new Translation(tr.getComments(), tr.getKey(), tr.getTargetTranslation()) )
                .collect(Collectors.toMap(Translation::getKey, tr -> tr));
    }

    private int getSortMode(){
        return mainWindow.sortMode.getSelectionModel().getSelectedIndex();
    }

    public MainWindowController getWindow(){
        return mainWindow;
    }

}
