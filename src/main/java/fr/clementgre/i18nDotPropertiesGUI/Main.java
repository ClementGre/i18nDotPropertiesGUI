package fr.clementgre.i18nDotPropertiesGUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.JMetroStyleClass;
import jfxtras.styles.jmetro.Style;
import org.controlsfx.control.NotificationPane;

import java.io.IOException;

public class Main extends Application {

    public static Parent root;
    public static Stage window;
    public static Scene scene;


    public static void main(String[] args){
        System.out.println("Starting i18nDotPropertiesGUI...");

        ///// START APP /////
        launch(args);
    }
    @Override
    public void start(Stage window) throws IOException {
        Main.window = window;

        root = FXMLLoader.load(getClass().getResource("/fxml/mainWindow.fxml"));
        new JMetro(root, MainWindowController.prefs.getBoolean("displayModes.darkMode", true) ? Style.DARK : Style.LIGHT);
        root.getStyleClass().add(JMetroStyleClass.BACKGROUND);

        scene = new Scene(root, MainWindowController.prefs.getDouble("windowSize.width", 1200), MainWindowController.prefs.getDouble("windowSize.height", 700));

        window.setMinHeight(400);
        window.setMinWidth(700);
        window.setMaximized(MainWindowController.prefs.getBoolean("windowSize.fullScreen", false));

        window.setTitle("i18nDotPropertiesGUI");
        window.setScene(scene);
        window.show();

        root.getStylesheets().add(Main.class.getResource("/css/main.css").toExternalForm());

    }



    public static boolean isWindows(){
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
    public static boolean isOSX(){
        return System.getProperty("os.name").toLowerCase().contains("mac os x");
    }
    public static boolean isLinux(){
        return !isWindows() && !isOSX();
    }

}
