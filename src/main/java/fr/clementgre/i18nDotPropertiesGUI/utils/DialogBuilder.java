package fr.clementgre.i18nDotPropertiesGUI.utils;

import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.List;
import java.util.Optional;

public class DialogBuilder {

    public static Alert getAlert(Window owner, Alert.AlertType type, String title){
        return getAlert(owner, type, title, null, null);
    }
    public static Alert getAlert(Window owner, Alert.AlertType type, String title, String header){
        return getAlert(owner, type, title, header, null);
    }
    public static Alert getAlertBooth(Window owner, Alert.AlertType type, String titleHeader){
        return getAlert(owner, type, titleHeader, titleHeader, null);
    }
    public static Alert getAlert(Window owner, Alert.AlertType type, String title, String header, String content){
        Alert alert = new Alert(type);
        alert.setTitle(title);
        new JMetro(alert.getDialogPane(), Style.LIGHT);

        if(owner != null) alert.initOwner(owner);

        if(header != null){
            alert.setHeaderText(header);
        }
        if(content != null){
            alert.setContentText(content);
        }

        return alert;
    }
    public static <T> ChoiceDialog<T> getChoiceDialog(Window owner, T selected, List<T> values){
        ChoiceDialog<T> alert = new ChoiceDialog<>(selected, values);

        if(owner != null) alert.initOwner(owner);
        new JMetro(alert.getDialogPane(), Style.LIGHT);
        return alert;
    }

    public static boolean showErrorAlert(Window owner, String headerText, String error, boolean continueAsk){
        Alert alert = getAlert(owner, Alert.AlertType.ERROR, "An error occured");
        alert.setHeaderText(headerText);

        TextArea textArea = new TextArea(error);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(new Label("Error message :"), 0, 0);
        expContent.add(textArea, 0, 1);
        alert.getDialogPane().setExpandableContent(expContent);

        if(continueAsk){
            ButtonType stopAll = new ButtonType("Stop all", ButtonBar.ButtonData.NO);
            ButtonType continueRender = new ButtonType("Continue", ButtonBar.ButtonData.YES);
            alert.getButtonTypes().setAll(stopAll, continueRender);

            Optional<ButtonType> option = alert.showAndWait();
            if(option.get() == stopAll) return true;
        }else{
            alert.show();
        }
        return false;
    }

}
