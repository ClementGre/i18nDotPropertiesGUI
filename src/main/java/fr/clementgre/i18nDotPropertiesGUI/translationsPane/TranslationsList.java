package fr.clementgre.i18nDotPropertiesGUI.translationsPane;

import fr.clementgre.i18nDotPropertiesGUI.FullTranslation;
import fr.clementgre.i18nDotPropertiesGUI.MainWindowController;
import fr.clementgre.i18nDotPropertiesGUI.Translation;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import jfxtras.styles.jmetro.JMetroStyleClass;

import java.util.*;
import java.util.stream.Collectors;

public class TranslationsList extends TableView<FullTranslation> {

    private final TableColumn<FullTranslation, String> keyColumn = new TableColumn<>("Key");
    private final TableColumn<FullTranslation, String> sourceColumn = new TableColumn<>("Source Translation");
    private final TableColumn<FullTranslation, String> targetColumn = new TableColumn<>("Target Translation");

    MainWindowController mainWindow;
    public TranslationsList(MainWindowController mainWindow) {
        this.mainWindow = mainWindow;

        getStyleClass().add(JMetroStyleClass.ALTERNATING_ROW_COLORS);
        getStyleClass().add(JMetroStyleClass.TABLE_GRID_LINES);

        getColumns().add(keyColumn);
        getColumns().add(sourceColumn);
        getColumns().add(targetColumn);

        keyColumn.setCellValueFactory(param -> param.getValue().keyProperty());
        sourceColumn.setCellValueFactory(param -> param.getValue().sourceTranslationProperty());
        targetColumn.setCellValueFactory(param -> param.getValue().targetTranslationProperty());

        sourceColumn.setPrefWidth(550);
        targetColumn.setPrefWidth(550);

        keyColumn.setCellFactory(column -> getCellFactory(column, false));
        sourceColumn.setCellFactory(column -> getCellFactory(column, true));
        targetColumn.setCellFactory(column -> getCellFactory(column, true));

        setEditable(true);

        sortPolicyProperty().set(param -> {

            final List<TableColumn<FullTranslation, ?>> sortOrder = new ArrayList<>(getSortOrder());
            final ObservableList<FullTranslation> itemsList = getItems();
            if(itemsList == null || itemsList.isEmpty()) return true;

            TableColumn.SortType sortType = TableColumn.SortType.DESCENDING;
            TableColumn<FullTranslation, ?> sortColumn = keyColumn;
            if(!sortOrder.isEmpty() && sortOrder.get(0) != null){
                sortType = sortOrder.get(0).getSortType();
                sortColumn = sortOrder.get(0);
            }

            final TableColumn.SortType finalSortType = sortType;
            final Comparator<FullTranslation> tableComparator = getComparator();
            TableColumn<FullTranslation, ?> finalSortColumn = sortColumn;
            Comparator<FullTranslation> comparator = tableComparator == null ? null : (o1, o2) -> {

                if(finalSortColumn.equals(sourceColumn)){
                    int v1 = getTranslationSourceSortValue(o1);
                    int v2 = getTranslationSourceSortValue(o2);
                    if(v1 != v2) return finalSortType == TableColumn.SortType.ASCENDING ? v1 - v2 : v2 - v1;
                    else return o1.getKey().compareTo(o2.getKey());
                }else if(finalSortColumn.equals(targetColumn)){
                    int v1 = getTranslationTargetSortValue(o1);
                    int v2 = getTranslationTargetSortValue(o2);
                    if(v1 != v2) return finalSortType == TableColumn.SortType.ASCENDING ? v1 - v2 : v2 - v1;
                    else return o1.getKey().compareTo(o2.getKey());
                }else{
                    int output = o1.getKey().compareTo(o2.getKey());
                    return finalSortType == TableColumn.SortType.ASCENDING ? output : -output;
                }


            };
            setItems(getItems().sorted(comparator));
            return true;
        });

    }

    private TableCell<FullTranslation, String> getCellFactory(TableColumn<FullTranslation, String> column, boolean allowWrap){
        return new TableCell<>(){
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);
                if(item == null || empty){
                    setText(null);
                    setGraphic(null);
                }else{
                    if(MainWindowController.prefs.getBoolean("displayModes.compactMode", true))
                        setStyle("-fx-padding: 0 4;");
                    if(MainWindowController.prefs.getBoolean("displayModes.wrapText", true) && allowWrap){
                        Text text = new Text(item);
                        text.wrappingWidthProperty().bind(column.widthProperty().subtract(25));
                        setGraphic(text);
                        setText(null);
                    }else{
                        setGraphic(null);
                        setText(item);
                    }
                }
            }
        };
    }

    public void selectNext(){
        scrollTo(Math.max(getSelectionModel().getSelectedIndex() - 2, 0));
        Platform.runLater(() -> getSelectionModel().selectNext());
    }

    public void select(FullTranslation tr){
        scrollTo(Math.max(getItems().indexOf(tr) - 3, 0));
        Platform.runLater(() -> getSelectionModel().select(tr));
    }
    public FullTranslation getSelected(){
        return getSelectionModel().getSelectedItem();
    }
    public ReadOnlyObjectProperty<FullTranslation> selectedProperty(){
        return getSelectionModel().selectedItemProperty();
    }

    private TableColumn<FullTranslation, ?> oldSortColumn = sourceColumn;
    private TableColumn.SortType oldSortType = TableColumn.SortType.ASCENDING;
    private String oldKey = null;
    public void loadItems(HashMap<String, Translation> source, HashMap<String, Translation> alternate, HashMap<String, Translation> target){
        if(getItems() != null){
            if(getSelected() != null) oldKey = getSelected().getKey();
            oldSortColumn = getSortOrder().isEmpty() ? null : getSortOrder().get(0);
            oldSortType = getSortOrder().isEmpty() ? null : getSortOrder().get(0).getSortType();
        }

        if(target.isEmpty() || source.isEmpty()){
            setItems(null); return;
        }

        List<FullTranslation> translations = source.values().stream().map((tr) ->
            new FullTranslation(tr.getKey(), tr.getComments(), tr.getValue(),
                    alternate.containsKey(tr.getKey()) ? alternate.get(tr.getKey()).getValue() : "",
                    target.containsKey(tr.getKey()) ? target.get(tr.getKey()).getValue() : "")).collect(Collectors.toList());

        setItems(null);
        Platform.runLater(() -> {
            setItems(FXCollections.observableList(translations));
            getSortOrder().clear();
            if(oldSortColumn != null){
                if(oldSortType != null) oldSortColumn.setSortType(oldSortType);
                getSortOrder().add(oldSortColumn);
            }

            sort();

            if(oldKey != null){
                for(FullTranslation translation : translations){
                    if(translation.getKey().equals(oldKey)){
                        Platform.runLater(() -> select(translation));
                        break;
                    }
                }
            }
        });



    }
    private int getTranslationSourceSortValue(FullTranslation translation){
        int output = 0;
        output += translation.getSourceTranslation().isBlank() ? 0 : 10;
        output += translation.getTargetTranslation().isBlank() ? 0 : 5;
        output += translation.getAlternativeTranslation().isBlank() ? 0 : 1;
        return output;
    }
    private int getTranslationTargetSortValue(FullTranslation translation){
        int output = 0;
        output += translation.getSourceTranslation().isBlank() ? 0 : 5;
        output += translation.getTargetTranslation().isBlank() ? 0 : 10;
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

    public MainWindowController getWindow(){
        return mainWindow;
    }

}
